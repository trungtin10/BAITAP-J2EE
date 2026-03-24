package com.example.bai5_qlsp.config;

import com.example.bai5_qlsp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AccountService accountService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests((requests) -> requests
                // 1. Cho phép truy cập các tài nguyên tĩnh
                .requestMatchers("/images/**", "/css/**", "/js/**").permitAll()
                
                // 2. Chỉ ADMIN mới được Thêm, Sửa, Xóa
                .requestMatchers("/products/create/**", "/products/edit/**", "/products/delete/**").hasAuthority("ROLE_ADMIN")
                
                // 3. Cả USER và ADMIN đều được xem danh sách sản phẩm và giỏ hàng
                .requestMatchers("/products", "/products/", "/cart", "/cart/**", "/checkout", "/checkout/**")
                    .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                
                // 4. Mọi yêu cầu khác phải đăng nhập
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .defaultSuccessUrl("/products", true)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Chuyển hướng về trang login sau khi đăng xuất thành công
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/products?error=403")
            );

        return http.build();
    }
}
