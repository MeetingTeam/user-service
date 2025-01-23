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
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    private Random rand = new Random();

    @Value("${s3.bucket-name}")
    private String bucketName;

    public String generatePreSignedUrl(String folder,String newFile, String oldUrl){
        String filename= newFile.substring(0,newFile.lastIndexOf("."));
        String filetype=newFile.substring(newFile.lastIndexOf(".")+1);
        String objectKey = filename+"_"+rand.nextInt(10000)+"."+filetype;

        if(oldUrl!=null) CompletableFuture.runAsync(()->deleteFile(oldUrl));

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(r -> r
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(15)));
        return presignedRequest.url().toString();
    }

    public void deleteFile(String fileUrl){
        DeleteObjectRequest deleteRequest= DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileUrl)
                .build();
        s3Client.deleteObject(deleteRequest);
    }
}
