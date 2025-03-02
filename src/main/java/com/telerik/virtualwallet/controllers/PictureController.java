package com.telerik.virtualwallet.controllers;


import com.telerik.virtualwallet.services.picture.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
public class PictureController {

    private final PictureService pictureService;

    @Autowired
    public PictureController(PictureService pictureService){
        this.pictureService = pictureService;
    }

    @PostMapping("/{username}")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<String> handleFileUpload(@RequestParam("picture") MultipartFile [] pictures, @PathVariable String username) {

        pictureService.upload(pictures, username);

        return ResponseEntity.ok("Successfully uploaded pictures");

    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> retrieveUserFiles(@PathVariable String username) {

            return ResponseEntity.ok(pictureService.retrieveAll(username));

    }

    @ResponseBody
    @GetMapping("/pictures/{filename}")
    public ResponseEntity<Resource> retrieveSpecificPicture(@PathVariable String filename) {

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(pictureService.retrieveSingle(filename));

    }

}
