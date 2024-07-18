package com.dattran.identity_service;

import com.dattran.identity_service.domain.entities.Role;
import com.dattran.identity_service.domain.enums.AccountRole;
import com.dattran.identity_service.domain.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class IdentityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			List<Role> roles = roleRepository.findAll();
			if (CollectionUtils.isEmpty(roles)) {
				Role customerRole = Role.builder()
						.name(AccountRole.CUSTOMER.name())
						.description("Đây là role cho người dùng, có thể thực hiện các thao tác xem, mua hàng.")
						.build();
				Role adminRole = Role.builder()
						.name(AccountRole.ADMIN.name())
						.description("Đây là role cho người quản trị, có thể quản lý sản phẩm, người dùng, shop.")
						.build();
				Role shopRole = Role.builder()
						.name(AccountRole.SHOP.name())
						.description("Đây là role cho người bán, có thể đăng bán các sản phẩm, quản lý các đơn hàng.")
						.build();
				roles.add(customerRole);
				roles.add(adminRole);
				roles.add(shopRole);
				roleRepository.saveAll(roles);
			}
		};
	}
}
