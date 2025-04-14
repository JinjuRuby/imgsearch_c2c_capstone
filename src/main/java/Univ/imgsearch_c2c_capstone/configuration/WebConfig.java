package Univ.imgsearch_c2c_capstone.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ✅ CORS 설정 (React에서 접근 허용)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*");
    }

    // ✅ 정적 리소스 경로 매핑 설정 (이미지 파일 접근 가능하게)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads");  // 실제 저장 폴더
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/images/**")  // URL 경로
                .addResourceLocations("file:///" + uploadPath + "/"); // 실제 파일 경로
    }
}
