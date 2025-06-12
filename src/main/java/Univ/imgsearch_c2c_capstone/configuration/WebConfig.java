/*package Univ.imgsearch_c2c_capstone.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        Path uploadDir = Paths.get("C:/Users/user/Desktop/건국대_글로컬/4학년_2학기/소프트웨어종합설계/스냅쇼핑/imgsearch/uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + uploadPath + "/");
    }
}*/






package Univ.imgsearch_c2c_capstone.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ✅ 모든 Origin에 대해 CORS 완전 허용
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000") // 모든 도메인 허용
                .allowedMethods("*")        // GET, POST, PUT, DELETE 등 허용
                .allowedHeaders("*");        // 모든 헤더 허용
    }

    // ✅ 정적 리소스 경로 매핑 설정 (이미지 경로 접근 허용)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("C:/Users/user/Desktop/건국대_글로컬/4학년_2학기/소프트웨어종합설계/스냅쇼핑/imgsearch/uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + uploadPath + "/");
    }
}
