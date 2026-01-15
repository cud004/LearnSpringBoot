package duc.demo.configuration;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;


//Cach 1
//@Configuration
//public class AppConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // scope: Tất cả đường dẫn
//                .allowCredentials(true) // cho phép gửi thông tin xác thực
//                .allowedOrigins("http://localhost:5173") // xác định nguồn được gọi
//                .allowedMethods("*") // các method được sử dụng
//                .allowedHeaders("*"); // các header được gửi lên
//
//    }
//}
//@Configuration
//public class AppConfig {
//    @Bean
//    public WebMvcConfigurer corsConfigurer(){
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:5173");
//            }
//        };
//    }

//    @Bean
//    public FilterRegistrationBean<CorsFilter>  corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // tạo source để quản lí URL nào dùng cái cài đặt của config
//        CorsConfiguration config = new CorsConfiguration(); // tạo config để cài đặt
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:5173");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source)); // Khai báo - Khởi tạo 1 filter để quản lí source
//        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // đưa lên độ ưu tiên cao nhất, xử lí trước
//        return bean;
@Component
public class AppConfig extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        filterChain.doFilter(request, response);

    }
}

