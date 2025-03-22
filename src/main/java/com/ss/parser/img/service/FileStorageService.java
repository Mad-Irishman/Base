package com.ss.parser.img.service;

import com.ss.Except4SupportDocumented;
import com.ss.ExceptInfoUser;
import com.ss.parser.img.exception.ImageProcessingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@Service
public class FileStorageService {

    public void createFolder(String folderName) throws ExceptInfoUser {
        Path directoryPath = Paths.get("downloads", folderName);

        if (Files.exists(directoryPath)) {
            throw new ExceptInfoUser(Collections.singletonMap("error", "The folder already exists"));
        }

        try {
            Files.createDirectories(directoryPath);
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
