package meetingteam.userservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
    @Value("${aws.access-key}")
    private String awsAccessKey;

    @Value("${aws.access-secret}")
    private String awsAccessSecret;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCreds= AwsBasicCredentials.create(awsAccessKey, awsAccessSecret);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(software.amazon.awssdk.regions.Region.US_EAST_1)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials awsCreds= AwsBasicCredentials.create(awsAccessKey, awsAccessSecret);

        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(software.amazon.awssdk.regions.Region.US_EAST_1)
                .build();
    }
}
