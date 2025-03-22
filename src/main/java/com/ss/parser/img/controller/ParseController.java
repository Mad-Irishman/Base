package com.ss.parser.img.controller;

import com.ss.parser.img.service.FileStorageService;
import com.ss.parser.img.service.ImageService;
import com.ss.parser.img.validation.ValidationUrlFoleder;
import com.ss.parser.img.exception.ImageProcessingException;
import com.ss.Except4SupportDocumented;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ParseController {

    private final ImageService imageService;
    private final ValidationUrlFoleder validationUrlFoleder;
    private final FileStorageService fileStorageService;

    @Autowired
    public ParseController(ImageService imageService, ValidationUrlFoleder validationUrlFoleder, FileStorageService fileStorageService) {
        this.imageService = imageService;
        this.validationUrlFoleder = validationUrlFoleder;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping(path = "/download/img")
    public String showFormForUrl() {
        return "formForUrl";
    }

    @PostMapping(path = "submit")
    public String handleUrlSubmission(@RequestParam String url, @RequestParam String folderName, Model model) {
        try {
            if (!validationUrlFoleder.isValidUrl(url)) {
                model.addAttribute("error", "Invalid URL. Please enter a valid URL.");
                return "formForUrl";
            }

            if (!validationUrlFoleder.isValidFolderName(folderName)) {
                model.addAttribute("error", "Invalid folder name. Please avoid special characters: /\\:*?\"<>|");
                return "formForUrl";
            }

            fileStorageService.createFolder(folderName);

            imageService.processWebsiteImages(url, "downloads/" + folderName);

            model.addAttribute("message", "Images downloaded successfully to the folder: " + folderName);
            return "formForUrl";

        } catch (ImageProcessingException e) {
            model.addAttribute("error", "An error occurred while processing the images: " + e.getMessage());
            return "formForUrl";
        } catch (Except4SupportDocumented e) {
            System.err.println("Support error: " + e.getMessage4Support());
            return "formForUrl";
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "formForUrl";
        }
    }

}
