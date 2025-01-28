package meetingteam.userservice.services;

public interface FileService {
    String generatePreSignedUrl(String newFile, String oldUrl);
    void deleteFile(String fileUrl);
}
