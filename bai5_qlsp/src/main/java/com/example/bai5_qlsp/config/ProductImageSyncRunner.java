package com.example.bai5_qlsp.config;

import com.example.bai5_qlsp.model.Product;
import com.example.bai5_qlsp.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Khi khoi dong: neu file anh trong DB chua co trong thu muc upload nhung co trong
 * classpath (static/images) hoac trung &quot;phan ten sau UUID_&quot;, copy vao upload
 * dung ten luu trong DB de link /images/... luon hop le.
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnProperty(name = "app.image.sync.enabled", havingValue = "true", matchIfMissing = true)
public class ProductImageSyncRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ProductImageSyncRunner.class);
    private static final String CLASSPATH_IMAGES = "classpath:/static/images/*";

    private final ProductRepository productRepository;
    private final ImageUploadPaths imageUploadPaths;
    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public ProductImageSyncRunner(ProductRepository productRepository, ImageUploadPaths imageUploadPaths) {
        this.productRepository = productRepository;
        this.imageUploadPaths = imageUploadPaths;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        Path uploadDir = imageUploadPaths.getDirectory().normalize();
        Resource[] classpathImages = safeListClasspathImages();

        for (Product product : productRepository.findAll()) {
            String image = product.getImage();
            if (image == null || image.isBlank()) {
                continue;
            }
            String name = image.trim();
            if (name.contains("..") || name.contains("/") || name.contains("\\")) {
                log.warn("Bo qua ten file anh khong hop le cho san pham id={}: {}", product.getId(), name);
                continue;
            }

            Path dest = uploadDir.resolve(name).normalize();
            if (!dest.startsWith(uploadDir)) {
                continue;
            }
            if (Files.exists(dest)) {
                continue;
            }

            Resource source = resolveSource(name, classpathImages);
            if (source == null) {
                log.warn("Khong tim thay anh cho san pham id={}, image={}", product.getId(), name);
                continue;
            }

            Files.createDirectories(dest.getParent());
            try (InputStream in = source.getInputStream()) {
                Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
            }
            log.info("Dong bo anh san pham id={} -> {}", product.getId(), dest);
        }
    }

    private Resource[] safeListClasspathImages() {
        try {
            return resolver.getResources(CLASSPATH_IMAGES);
        } catch (IOException e) {
            log.debug("Khong doc duoc classpath static/images: {}", e.getMessage());
            return new Resource[0];
        }
    }

    private Resource resolveSource(String dbFileName, Resource[] classpathImages) throws IOException {
        Resource exact = resolver.getResource("classpath:/static/images/" + dbFileName);
        if (exact.exists() && exact.isReadable()) {
            return exact;
        }

        String tail = tailAfterFirstUnderscore(dbFileName);
        List<Resource> matches = new ArrayList<>();
        for (Resource r : classpathImages) {
            if (!r.exists() || !r.isReadable()) {
                continue;
            }
            String fn = r.getFilename();
            if (fn == null) {
                continue;
            }
            if (tail.equals(tailAfterFirstUnderscore(fn))) {
                matches.add(r);
            }
        }
        if (matches.isEmpty()) {
            return null;
        }
        if (matches.size() > 1) {
            log.warn("Nhieu file trung phan ten '{}' trong static/images, dung file dau tien", tail);
        }
        return matches.get(0);
    }

    /**
     * Vi du: uuid_OIP (2).jpg -> OIP (2).jpg ; iphone.jpg -> iphone.jpg
     */
    static String tailAfterFirstUnderscore(String fileName) {
        int i = fileName.indexOf('_');
        return i >= 0 ? fileName.substring(i + 1) : fileName;
    }
}
