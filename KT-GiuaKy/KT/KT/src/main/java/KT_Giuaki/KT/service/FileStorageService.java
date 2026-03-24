package KT_Giuaki.KT.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path imageDir;

    public FileStorageService(@Value("${app.image-dir:images}") String imageDir) throws IOException {
        this.imageDir = Paths.get(imageDir).toAbsolutePath().normalize();
        Files.createDirectories(this.imageDir);
    }

    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        String ext = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext != null ? "." + ext : "");
        Path target = imageDir.resolve(filename);
        Files.copy(file.getInputStream(), target);
        return "/images/" + filename;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return null;
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
