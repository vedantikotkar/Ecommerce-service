////package com.example.auth.config;
////
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.config.http.SessionCreationPolicy;
////import org.springframework.security.oauth2.jwt.JwtDecoder;
////import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
////import org.springframework.security.web.SecurityFilterChain;
////
////@Configuration
////@EnableWebSecurity
////public class SecurityConfig {
////
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf(csrf -> csrf.disable())
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/auth/login", "/auth/signup", "/auth/forgot-password","/products/bulk-upload" ,"/products/create","/products/all" ,"/products/upload-image" ,"products/images/{filename}" ,"/categories/bulk-upload","/products/{id}","products/category/{categoryId}","/wishlist/add","/send-email","/orders" ,"/products/search","/auth/save-address/{username}","/categories","/categories/{id}","/like/liked" ,"/like/","/like/unlike","/like/**","/reviewed-products/review","/reviewed-products/","/rated-products/rate","/rated-products/").permitAll()
////                        .anyRequest().authenticated()
////                )
////                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder()))); // Updated JWT config
////
////        return http.build();
////    }
////
////    @Bean
////    public JwtDecoder jwtDecoder() {
////        return NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/realms/master/protocol/openid-connect/certs").build();
////    }
////}
//
//
//package com.example.auth.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private static final String[] PUBLIC_API_PATHS = {
//            "/auth/login", "/auth/signup", "/auth/forgot-password","auth/logout"
//            ,"/auth/verify-password","/auth/upload","auth/verify-email",
//            "/auth/remove/{filename}","/auth/{filename}",
//            "/products/bulk-upload", "/products/create", "/products/all",
//            "/products/upload-image", "/products/images/{filename}",
//            "/categories/bulk-upload", "/products/{id}", "/products/category/{categoryId}",
//            "/wishlist/add", "/send-email", "/orders", "/products/search",
//            "/auth/save-address/{username}", "/categories", "/categories/{id}",
//            "/like/liked", "/like/","/like/unlike",
//            "/like/unlike/**",
//            "/like/**",
//            "/reviewed-products/review", "/reviewed-products/",
//            "/rated-products/rate", "/rated-products/","/cart-products/add","/cart-products/remove","/cart-products/"
//    };
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // Ensure CSRF is disabled for DELETE requests
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(PUBLIC_API_PATHS).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));
//
//        return http.build();
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/realms/master/protocol/openid-connect/certs").build();
//    }
//}
package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_API_PATHS = {
            "/auth/login", "/auth/signup", "/auth/forgot-password", "/auth/logout",
            "/auth/verify-password", "/auth/upload", "/auth/verify-email",
            "/auth/remove/{filename}", "/auth/{filename}","/auth/{profilePhotoPath}",
            "/products/bulk-upload", "/products/create", "/products/all",
            "/products/upload-image", "/products/images/{filename}",
            "/categories/bulk-upload", "/products/{id}", "/products/category/{categoryId}",
            "/wishlist/add", "/send-email", "/orders","/orders/user/{userId}", "/products/search","wishlist/{userId}",
            "/auth/save-address/{username}", "/categories", "/categories/{id}",
            "/like/liked", "/like/", "/like/unlike", "/orders/{id}",
            "/like/unlike/**", "/like/**",
            "/reviewed-products/review", "/reviewed-products/",
            "/rated-products/rate", "/rated-products/",
            "/cart-products/add", "/cart-products/remove", "/cart-products/add"
    };

    private static final String[] PUBLIC_DELETE_PATHS = {
            "/products/delete/**", "/categories/delete/**", "/orders/{id}",
            "auth/**" ,"/wishlist/remove/{wishlistId}","/cart-products/add" ,"like/unlike"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF to allow DELETE requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_API_PATHS).permitAll() // Public APIs
                        .requestMatchers(PUBLIC_DELETE_PATHS).permitAll() // Allow DELETE requests publicly
                        .anyRequest().authenticated() // Require authentication for other requests
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/realms/master/protocol/openid-connect/certs").build();
    }
}
