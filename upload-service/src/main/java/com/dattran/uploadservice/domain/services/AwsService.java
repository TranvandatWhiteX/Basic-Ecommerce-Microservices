package com.dattran.uploadservice.domain.services;

import com.dattran.enums.ResponseStatus;
import com.dattran.exceptions.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedUpload;
import software.amazon.awssdk.transfer.s3.model.Upload;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AwsService {
    @NonFinal
    @Value("${aws.s3.bucket-name}")
    String bucketName;
    S3TransferManager s3TransferManager;
    S3AsyncClient s3AsyncClient;
    ExecutorService executor = Executors.newFixedThreadPool(10);

    public List<String> uploadFiles(com.dattran.uploadservice.app.requests.UploadRequest uploadRequest) {
        if (uploadRequest.getFiles().isEmpty()) {
            throw new AppException(ResponseStatus.NO_FILES_FOUND);
        }
        List<CompletableFuture<String>> futures = uploadRequest.getFiles().stream()
                .map(file -> CompletableFuture.supplyAsync(() -> uploadFile(file, uploadRequest.getFolder(), uploadRequest.getGroupName())))
                .toList();
        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private String uploadFile(MultipartFile file, String folder, String groupName) {
        String fileName = UUID.randomUUID() + "_" + Paths.get(file.getOriginalFilename()).getFileName().toString();
        String keyName = new StringJoiner("/").add(folder).add(groupName).add(fileName).toString();
        try {
            AsyncRequestBody requestBody = AsyncRequestBody.fromInputStream(file.getInputStream(), file.getSize(), executor);
            UploadRequest uploadRequest = UploadRequest.builder()
                    .putObjectRequest(b -> b.bucket(bucketName).key(keyName))
                    .requestBody(requestBody)
                    .build();
            Upload upload = s3TransferManager.upload(uploadRequest);
            CompletableFuture<CompletedUpload> future = upload.completionFuture();
            future.join(); // Wait for the upload to complete
            return generateUrl(keyName);
        } catch (S3Exception | IOException e) {
            log.warn("Upload Exception {}", e.getMessage());
            throw new AppException(ResponseStatus.CANNOT_UPLOAD_FILE);
        }
    }

    public CompletableFuture<Void> deleteFile(String fileKey) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();
        return s3AsyncClient.deleteObject(deleteObjectRequest)
                .thenApply(DeleteObjectResponse::deleteMarker)
                .thenAccept(marker -> log.info("Deleted file: {}", fileKey))
                .exceptionally(throwable -> {
                    log.warn("Failed to delete file: {}", fileKey, throwable);
                    throw new AppException(ResponseStatus.CANNOT_DELETE_FILE);
                });
    }

    private String generateUrl(String objectKey) {
        try {
            GetUrlRequest request = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            return s3AsyncClient.utilities().getUrl(request).toString();
        } catch (S3Exception e) {
            log.warn("Generate Url Exception {}",e.getMessage());
            throw new AppException(ResponseStatus.CANNOT_UPLOAD_FILE);
        }
    }
}
