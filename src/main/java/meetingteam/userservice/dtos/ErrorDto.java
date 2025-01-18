package meetingteam.userservice.dtos;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ErrorDto(
        HttpStatus statusCode,
        String title,
        String detail,
        Map<String, String> fieldErrors
) {
    public ErrorDto(HttpStatus statusCode, String title, String detail){
        this(statusCode, title, detail, null);
    }
}
