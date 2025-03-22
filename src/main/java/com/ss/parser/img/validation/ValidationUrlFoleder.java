package com.ss.parser.img.validation;

import org.springframework.stereotype.Component;

@Component
public class ValidationUrlFoleder {

    public boolean isValidUrl(String url) {
        String urlRegex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        return url != null && url.matches(urlRegex);
    }

    public boolean isValidFolderName(String folderName) {
        String folderNameRegex = "^[^\\\\/:*?\"<>|]+$";
        return folderName != null && folderName.matches(folderNameRegex) && !folderName.isEmpty();
    }
}
