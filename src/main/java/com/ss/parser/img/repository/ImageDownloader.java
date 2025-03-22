package com.ss.parser.img.repository;

import com.ss.Except4SupportDocumented;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageDownloader implements Runnable {
    private final String imageUrl;
    private final String fileName;

    public ImageDownloader(String imageUrl, String fileName) {
        this.imageUrl = imageUrl;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try (InputStream in = new URL(imageUrl).openStream();
             FileOutputStream out = new FileOutputStream(fileName)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new Except4SupportDocumented(
                    "IMAGE_DOWNLOAD_ERROR",
                    "Error downloading image",
                    "Error downloading image from URL: " + imageUrl,
                    e
            );
        }
    }
}
