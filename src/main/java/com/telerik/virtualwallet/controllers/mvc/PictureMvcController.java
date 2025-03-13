package com.telerik.virtualwallet.controllers.mvc;


import com.telerik.virtualwallet.services.picture.PictureService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/uploads")
public class PictureMvcController {

    private final PictureService pictureService;

    @Autowired
    public PictureMvcController(PictureService pictureService){
        this.pictureService = pictureService;
    }

    @PostMapping("/verification/{username}")
    @PreAuthorize("#username == authentication.name")
    public String handleVerificationPictureUpload(@RequestParam("picture") MultipartFile [] pictures, @PathVariable String username, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        try {
            pictureService.uploadVerificationPictures(pictures, username);

            redirectAttributes.addFlashAttribute("successUpload", true);
            return "redirect:" + request.getHeader("Referer");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("failUpload", true);
            return "redirect:" + request.getHeader("Referer");
        }

    }


    @PostMapping("/profile/{username}")
    @PreAuthorize("#username == authentication.name")
    public String handleProfilePictureUpload(@RequestParam("picture") MultipartFile pictures, @PathVariable String username, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        try {
            pictureService.uploadProfilePicture(pictures, username);

            redirectAttributes.addFlashAttribute("successUpload", true);
            return "redirect:" + request.getHeader("Referer");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("failUpload", true);
            return "redirect:" + request.getHeader("Referer");
        }

    }


    @ResponseBody
    @GetMapping("/{filename}")
    @PreAuthorize("hasRole('ADMIN')")
    public Resource retrieveSpecificPicture(@PathVariable String filename) {

        try {
            return pictureService.retrieveSingle(filename);
        }catch(Exception e){
            return null;
        }

    }

}