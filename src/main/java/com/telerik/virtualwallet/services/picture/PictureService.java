package com.telerik.virtualwallet.services.picture;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PictureService {

    void uploadVerificationPictures(MultipartFile[] pictures, String username);

    void uploadProfilePicture(MultipartFile picture, String username);

    Map<String, String> retrieveAll(String username);

    Resource retrieveSingle(String filename);

    void delete(String username);
}
