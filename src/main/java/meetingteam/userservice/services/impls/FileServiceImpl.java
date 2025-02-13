package meetingteam.userservice.services.impls;

import lombok.RequiredArgsConstructor;
import meetingteam.userservice.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final S3Client s3Client;

    @Value("${s3.bucket-name}")
    private String bucketName;

    public void deleteFile(String fileUrl){
        String[] strs= fileUrl.split("/");
        String key= strs[strs.length-1];

        DeleteObjectRequest deleteRequest= DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteRequest);
    }
}
