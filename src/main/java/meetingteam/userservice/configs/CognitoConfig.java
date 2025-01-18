package meetingteam.userservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class CognitoConfig {
    @Value("${aws.access-key}")
    private String awsAccessKey;

    @Value("${aws.access-secret}")
    private String awsAccessSecret;

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        AwsBasicCredentials awsCreds= AwsBasicCredentials.create(awsAccessKey, awsAccessSecret);
        return CognitoIdentityProviderClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.US_EAST_1)
                .build();
    }
}
