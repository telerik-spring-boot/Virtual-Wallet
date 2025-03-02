package com.telerik.virtualwallet.services.picture;

import com.telerik.virtualwallet.exceptions.EntityNotFoundException;
import com.telerik.virtualwallet.exceptions.PictureOperationException;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class PictureServiceImpl implements PictureService{

    private final String uploadDirectory;

    @Autowired
    public PictureServiceImpl(Environment env){
        this.uploadDirectory = env.getProperty("uploads.location", "uploads");
    }

    @Override
    public void upload(MultipartFile[] pictures, String username){

        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        if(pictures.length != 2){
            throw new PictureOperationException("You have to upload exactly 2 pictures.");
        }

        Path filePathFirst = Paths.get(uploadDirectory, "picture" + username + "1.jpg");
        Path filePathSecond = Paths.get(uploadDirectory, "picture" + username + "2.jpg");

        try {
            pictures[0].transferTo(filePathFirst);
            pictures[1].transferTo(filePathSecond);
        } catch (IOException e){
            throw new PictureOperationException("Failed to upload pictures for user with username: " + username);
        }

    }

    @Override
    public Map<String, String> retrieveAll(String username) {

        Path filePathFirst = Paths.get(uploadDirectory, "picture" + username + "1.jpg");
        Path filePathSecond = Paths.get(uploadDirectory, "picture" + username + "2.jpg");

        Map<String, String> fileUrls = new HashMap<>();

        if (Files.exists(filePathFirst)) {
            fileUrls.put("picture1", "/api/uploads/pictures/" + "picture" + username + "1.jpg");
        }

        if (Files.exists(filePathSecond)) {
            fileUrls.put("picture2", "/api/uploads/pictures/" + "picture" + username + "2.jpg");
        }

        if (fileUrls.isEmpty()) {
            throw new EntityNotFoundException(String.format("Pictures for user with username %s not found.", username));
        }

        return fileUrls;
    }

    @Override
    public Resource retrieveSingle(String filename){

            Path filePath = Paths.get(uploadDirectory).resolve(filename);
            File file = filePath.toFile();

            try {
                if (file.exists() && file.isFile()) {
                    return new UrlResource(file.toURI());
                } else {
                    throw new EntityNotFoundException("File", "filename", filename);
                }
            } catch(IOException e){
                throw new PictureOperationException("Failed to retrieve picture with filename: " + filename);
            }


    }

    @Override
    public void delete(String username){
        Path filePathFirst = Paths.get(uploadDirectory, "picture" + username + "1.jpg");
        Path filePathSecond = Paths.get(uploadDirectory, "picture" + username + "2.jpg");

        try {
            if (Files.exists(filePathFirst)) {
                Files.delete(filePathFirst);
            }

            if (Files.exists(filePathSecond)) {
                Files.delete(filePathSecond);
            }
        }catch(IOException e){
            throw new PictureOperationException("Failed to delete pictures for user with username: " + username);
        }
    }
}
