package com.ss.parser.img.service;

import com.ss.Except4SupportDocumented;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    public void createFolder(String folderName) {
        Path directoryPath = Paths.get("downloads", folderName);

        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
        } catch (IOException e) {
            throw new Except4SupportDocumented(
                    "FILE_STORAGE_ERROR",
                    "Error creating folders",
                    "Error while trying to create folder: " + directoryPath.toString(),
                    e
            );
        }
    }
}
