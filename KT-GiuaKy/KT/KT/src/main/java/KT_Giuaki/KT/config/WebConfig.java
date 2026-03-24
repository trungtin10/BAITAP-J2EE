package KT_Giuaki.KT.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.image-dir:images}")
    private String imageDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path imagePath = Paths.get(imageDir).toAbsolutePath().normalize();
        String location = "file:" + imagePath + "/";
        registry.addResourceHandler("/images/**").addResourceLocations(location);
        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
    }
}
