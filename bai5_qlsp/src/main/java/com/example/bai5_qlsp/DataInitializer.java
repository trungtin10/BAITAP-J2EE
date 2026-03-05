package com.example.bai5_qlsp;

import com.example.bai5_qlsp.repository.CategoryRepository;
import com.example.bai5_qlsp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Dữ liệu đã được khởi tạo. Không cần chạy lại để tránh lỗi.
        // Nếu muốn reset dữ liệu, hãy xóa bảng trong database thủ công hoặc dùng ddl-auto=create một lần.
    }
}
