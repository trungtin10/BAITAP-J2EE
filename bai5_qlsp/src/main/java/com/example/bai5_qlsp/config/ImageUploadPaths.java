package com.example.bai5_qlsp.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Thu muc luu file anh upload, on dinh khi chay tu IDE (khong phu thuoc target/classes).
 */
@Component
public class ImageUploadPaths {

    private final Path directory;

    public ImageUploadPaths(@Value("${app.upload.images-dir:uploads/images}") String relativeOrAbsolute) {
        Path p = Paths.get(relativeOrAbsolute.trim());
        if (!p.isAbsolute()) {
            p = Paths.get(System.getProperty("user.dir")).resolve(p);
        }
        this.directory = p.normalize().toAbsolutePath();
    }

    @PostConstruct
    public void ensureDirectoryExists() throws IOException {
        Files.createDirectories(directory);
    }

    public Path getDirectory() {
        return directory;
    }
}
