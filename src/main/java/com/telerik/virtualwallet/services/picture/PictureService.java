package com.telerik.virtualwallet.services.picture;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PictureService {

    void upload(MultipartFile[] pictures, String username);

    Map<String, String> retrieveAll(String username);

    Resource retrieveSingle(String filename);

    void delete(String username);
}
