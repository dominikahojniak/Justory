package com.justory.backend.service;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

@Slf4j
public class FileUtils {

    public static byte[] readFileFromLocation(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            return null;
        }
        try {
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.warn("No file found in the path {}", fileUrl);
        }
        return null;
    }
    public static boolean saveImage(String filePath, byte[] imageData) {
        if (StringUtils.isBlank(filePath) || imageData == null) {
            log.warn("File path or image data is invalid");
            return false;
        }
        try {
            Path path = new File(filePath).toPath();
            Files.createDirectories(path.getParent());
            Files.write(path, imageData, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            log.error("Failed to save image to path {}: {}", filePath, e.getMessage());
            return false;
        }
    }
    public static boolean downloadImageFromUrl(String imageUrl, String destinationPath) {
        try (InputStream in = new URL(imageUrl).openStream()) {
            Path targetPath = new File(destinationPath).toPath();
            Files.createDirectories(targetPath.getParent());
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            log.error("Failed to download image from URL {}: {}", imageUrl, e.getMessage());
            return false;
        }
    }
}