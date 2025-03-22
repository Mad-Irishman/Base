package com.ss.parser.img.service;

import com.ss.parser.img.exception.ImageProcessingException;
import com.ss.parser.img.repository.ImageDownloader;
import com.ss.Except4SupportDocumented;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ImageService {
    private final ExecutorService executorService;

    public ImageService() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public List<String> fetchImageUrls(String websiteUrl) throws ImageProcessingException {
        try {
            List<String> imageUrls = new ArrayList<>();
            Document doc = Jsoup.connect(websiteUrl).get();
            for (Element imgElement : doc.select("img")) {
                String imgUrl = imgElement.absUrl("src");
                if (!imgUrl.isEmpty()) {
                    imageUrls.add(imgUrl);
                }
            }
            return imageUrls;
        } catch (IOException e) {
            throw new Except4SupportDocumented(
                    "IMAGE_FETCH_ERROR",
                    "Ошибка при получении изображений с сайта",
                    "Ошибка при парсинге изображений с сайта: " + websiteUrl,
                    e
            );
        }
    }

    public void downloadImages(List<String> imageUrls, String downloadDir) throws Except4SupportDocumented {
        for (String imageUrl : imageUrls) {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            executorService.execute(new ImageDownloader(imageUrl, downloadDir + "/" + fileName));
        }

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                throw new Except4SupportDocumented(
                        "IMAGE_DOWNLOAD_TIMEOUT",
                        "Error downloading images",
                        "Some images did not load within 30 seconds.",
                        null
                );
            }
        } catch (InterruptedException e) {
            throw new Except4SupportDocumented(
                    "IMAGE_DOWNLOAD_INTERRUPTED",
                    "Error waiting for images to finish loading",
                    "Error waiting for images to finish loading.",
                    e
            );
        }

    }

    public void processWebsiteImages(String websiteUrl, String downloadDir) throws ImageProcessingException {
        List<String> imageUrls = fetchImageUrls(websiteUrl);
        if (imageUrls.isEmpty()) {
            throw new Except4SupportDocumented(
                    "NO_IMAGES_FOUND",
                    "No images found!",
                    "On the website" + websiteUrl + " no images found.",
                    null
            );
        }
        downloadImages(imageUrls, downloadDir);
    }
}
