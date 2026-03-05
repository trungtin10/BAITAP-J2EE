package com.example.bai5_qlsp.controller;

import com.example.bai5_qlsp.model.Product;
import com.example.bai5_qlsp.service.CategoryService;
import com.example.bai5_qlsp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Đường dẫn lưu ảnh (bạn có thể thay đổi đường dẫn này)
    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "product/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/create";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/create";
        }

        // Xử lý upload ảnh
        if (!imageFile.isEmpty()) {
            try {
                String fileName = saveImage(imageFile);
                product.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productService.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                Model model) {
        if (result.hasErrors()) {
            product.setId(id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/edit";
        }

        Product existingProduct = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        // Xử lý upload ảnh mới nếu có
        if (!imageFile.isEmpty()) {
            try {
                String fileName = saveImage(imageFile);
                product.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Giữ nguyên ảnh cũ nếu không upload ảnh mới
            product.setImage(existingProduct.getImage());
        }

        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    // Hàm hỗ trợ lưu ảnh
    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Tạo tên file duy nhất để tránh trùng lặp
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
