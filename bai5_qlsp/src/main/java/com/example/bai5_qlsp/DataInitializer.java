package com.example.bai5_qlsp;

import com.example.bai5_qlsp.model.Account;
import com.example.bai5_qlsp.model.Category;
import com.example.bai5_qlsp.model.Product;
import com.example.bai5_qlsp.model.Role;
import com.example.bai5_qlsp.repository.AccountRepository;
import com.example.bai5_qlsp.repository.CategoryRepository;
import com.example.bai5_qlsp.repository.ProductRepository;
import com.example.bai5_qlsp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Khởi tạo Role
        if (roleRepository.count() == 0) {
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            // 2. Khởi tạo Account
            if (accountRepository.count() == 0) {
                // Admin account
                Account admin = new Account();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(adminRole);
                admin.setRoles(adminRoles);
                accountRepository.save(admin);

                // User account
                Account user = new Account();
                user.setUsername("user1");
                user.setPassword(passwordEncoder.encode("123456"));
                Set<Role> userRoles = new HashSet<>();
                userRoles.add(userRole);
                user.setRoles(userRoles);
                accountRepository.save(user);
            }
        }

        // 3. Khởi tạo Category & Product (nếu chưa có)
        if (categoryRepository.count() == 0) {
            Category c1 = new Category(); c1.setName("Điện thoại");
            categoryRepository.save(c1);

            if (productRepository.count() == 0) {
                Product p1 = new Product(); 
                p1.setName("iPhone 15 Pro Max"); 
                p1.setPrice(30000000L); 
                p1.setImage("iphone15.jpg"); 
                p1.setCategory(c1);
                productRepository.save(p1);
            }
        }
    }
}
