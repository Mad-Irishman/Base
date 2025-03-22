package com.ss.parser.img.service;

import com.ss.ExceptInfoUser;
import com.ss.parser.img.repository.ImageDownloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ImageService {
    private ExecutorService executorService;

    public ImageService() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public List<String> fetchImageUrls(String websiteUrl) throws ExceptInfoUser {
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
            throw new ExceptInfoUser(
                    Collections.singletonMap("error", "Ошибка при получении изображений с сайта: " + websiteUrl),
                    e
            );
        }
    }

    public void downloadImages(List<String> imageUrls, String downloadDir) throws ExceptInfoUser {
        restartExecutor();
        for (String imageUrl : imageUrls) {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            executorService.execute(new ImageDownloader(imageUrl, downloadDir + "/" + fileName));
        }

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                throw new ExceptInfoUser(
                        Collections.singletonMap("error", "Некоторые изображения не были загружены в течение 30 секунд.")
                );
            }
        } catch (InterruptedException e) {
            throw new ExceptInfoUser(
                    Collections.singletonMap("error", "Ошибка при ожидании загрузки изображений.")
            );
        }
    }

    public void processWebsiteImages(String websiteUrl, String downloadDir) throws ExceptInfoUser {
        List<String> imageUrls = fetchImageUrls(websiteUrl);
        if (imageUrls.isEmpty()) {
            throw new ExceptInfoUser(Collections.singletonMap("error", "Не удалось получить изображения с сайта."));
        }
        downloadImages(imageUrls, downloadDir);
    }

    private void restartExecutor() {
        if (executorService.isShutdown() || executorService.isTerminated()) {
            executorService = Executors.newFixedThreadPool(10);
        }
    }
}
