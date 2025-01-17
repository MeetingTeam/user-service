package meetingteam.userservice.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class CognitoConfig {
    @Value("${cognito.client-id}")
    private String cognitoClientId;

    @Value("${cognito.secret}")
    private String cognitoSecret;

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                cognitoClientId,
                cognitoSecret
        );
        return CognitoIdentityProviderClient.builder()
                .region(Region.of("us-east-1")) // Replace with your region
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
}
