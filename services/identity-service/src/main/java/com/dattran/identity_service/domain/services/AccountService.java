package com.dattran.identity_service.domain.services;

import com.dattran.identity_service.app.dtos.AccountDTO;
import com.dattran.identity_service.app.dtos.AuthenticationDTO;
import com.dattran.identity_service.app.dtos.CustomerDTO;
import com.dattran.identity_service.app.dtos.VerifyDTO;
import com.dattran.identity_service.app.responses.AccountResponse;
import com.dattran.identity_service.app.responses.ApiResponse;
import com.dattran.identity_service.app.responses.AuthenticationResponse;
import com.dattran.identity_service.domain.entities.Account;
import com.dattran.identity_service.domain.entities.Customer;
import com.dattran.identity_service.domain.entities.Role;
import com.dattran.identity_service.domain.entities.Token;
import com.dattran.identity_service.domain.enums.AccountState;
import com.dattran.identity_service.domain.enums.ResponseStatus;
import com.dattran.identity_service.domain.enums.TokenType;
import com.dattran.identity_service.domain.exceptions.AppException;
import com.dattran.identity_service.domain.repositories.AccountRepository;
import com.dattran.identity_service.domain.repositories.CustomerClient;
import com.dattran.identity_service.domain.repositories.RoleRepository;
import com.dattran.identity_service.domain.repositories.TokenRepository;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountService {
    PasswordEncoder passwordEncoder;
    AccountRepository accountRepository;
    OtpService otpService;
    EmailService emailService;
    JwtService jwtService;
    CustomerClient customerClient;
    RoleRepository roleRepository;
    TokenRepository tokenRepository;
    AuthenticationManager authenticationManager;
    String template = "send-otp.html";
    RedisTemplate<String, Object> redisTemplate;
    @NonFinal
    @Value("${otp.expiration}")
    long OTP_EXPIRATION_MINUTES;

    @Transactional
    public AccountResponse createAccount(AccountDTO accountDTO) {
        if (accountRepository.existsByUsernameOrEmail(accountDTO.getUsername(), accountDTO.getUsername())) {
            throw new AppException(ResponseStatus.USERNAME_OR_EMAIL_EXISTED);
        }
        Account account = toAccount(accountDTO);
        account.setAccountState(AccountState.PENDING);
        Account savedAccount = accountRepository.save(account);
        String otp = otpService.generateOTP(6);
        otpService.storeOtp(savedAccount.getId(), otp);
        Map<String, Object> variables = new HashMap<>();
        variables.put("otp", otp);
        variables.put("username", savedAccount.getUsername());
        CompletableFuture<Void> emailSendingFuture =
                emailService.sendEmail(savedAccount.getEmail(), "OTP Verification", template, variables);
        emailSendingFuture.exceptionally(ex -> {
                    throw new AppException(ResponseStatus.SEND_OTP_FAILED);
                });
        CustomerDTO customerDTO = CustomerDTO.builder()
                .address(accountDTO.getAddress())
                .age(accountDTO.getAge())
                .dob(accountDTO.getDob())
                .email(accountDTO.getEmail())
                .userId(savedAccount.getId())
                .fullName(accountDTO.getFullName())
                .build();
        ApiResponse<Customer> customer = customerClient.createCustomer(customerDTO);
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(savedAccount.getId()+"-"+savedAccount.getUsername(),
                customer.getResult().getId(), OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        return toAccountResponse(savedAccount);
    }

    public String verifyAccount(VerifyDTO verifyDTO) {
        Account account = accountRepository.findById(verifyDTO.getAccountId())
                .orElseThrow(()-> new AppException(ResponseStatus.ACCOUNT_NOT_FOUND));
        if (otpService.validateOtp(verifyDTO.getAccountId(), verifyDTO.getOtp())) {
            account.setAccountState(AccountState.ACTIVE);
            Account savedAccount = accountRepository.save(account);
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            String customerId = (String) ops.get(savedAccount.getId()+"-"+savedAccount.getUsername());
            customerClient.verifyCustomer(customerId);
            return "Verify account success!";
        } else {
            if (!otpService.isOtpExpired(verifyDTO.getAccountId())) {
                return "Wrong OTP!";
            }
            String newOtp = otpService.generateOTP(6);
            otpService.storeOtp(account.getId(), newOtp);
            Map<String, Object> variables = new HashMap<>();
            variables.put("otp", newOtp);
            variables.put("username", account.getUsername());
            CompletableFuture<Void> emailSendingFuture =
                    emailService.sendEmail(account.getEmail(), "Resend OTP Verification", template, variables);
            emailSendingFuture.exceptionally(ex -> {
                throw new AppException(ResponseStatus.SEND_OTP_FAILED);
            });
            return "OTP is expired! New OTP was sent to your email!";
        }
    }

    private Account toAccount(AccountDTO accountDTO) {
        Set<Role> roles = new HashSet<>();
        accountDTO.getRoles().forEach(accountRole -> {
            Role role = roleRepository.findByName(accountRole.name())
                    .orElseThrow(()->new AppException(ResponseStatus.ROLE_NOT_FOUND));
            roles.add(role);
        });
        return Account.builder()
                .email(accountDTO.getEmail())
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .username(accountDTO.getUsername())
                .roles(roles)
                .build();
    }

    private AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .accountState(account.getAccountState())
                .roles(account.getRoles())
                .username(account.getUsername())
                .build();
    }
}
