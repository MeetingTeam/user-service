package meetingteam.userservice.services;

public interface FileService {
    String generatePreSignedUrl(String folder,String filename,boolean addPrefix);
    void deleteFile(String fileUrl);
}
