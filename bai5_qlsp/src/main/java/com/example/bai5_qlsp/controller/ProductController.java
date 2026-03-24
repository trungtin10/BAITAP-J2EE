package com.example.bai5_qlsp.controller;

import com.example.bai5_qlsp.config.ImageUploadPaths;
import com.example.bai5_qlsp.model.Product;
import com.example.bai5_qlsp.service.CategoryService;
import com.example.bai5_qlsp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ImageUploadPaths imageUploadPaths;

    @GetMapping
    public String showProductList(
            @RequestParam(required = false) String keyword,
            @RequestParam(name = "categoryId", required = false) String categoryIdParam,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id") String sort,
            Model model) {

        Long categoryId = parseCategoryId(categoryIdParam);

        Sort sortSpec = resolveProductSort(sort);
        Page<Product> productPage =
                productService.searchProducts(keyword, categoryId, page, sortSpec);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalElements", productPage.getTotalElements());
        model.addAttribute("hasNext", productPage.hasNext());
        model.addAttribute("hasPrevious", productPage.hasPrevious());
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/list";
    }

    private static Long parseCategoryId(String categoryIdParam) {
        if (categoryIdParam == null || categoryIdParam.isBlank()) {
            return null;
        }
        try {
            return Long.valueOf(categoryIdParam.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Sort resolveProductSort(String sort) {
        if ("priceAsc".equalsIgnoreCase(sort)) {
            return Sort.by("price").ascending();
        }
        if ("priceDesc".equalsIgnoreCase(sort)) {
            return Sort.by("price").descending();
        }
        return Sort.by("id").ascending();
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

        // Cập nhật các thông tin cơ bản
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());

        // Xử lý upload ảnh mới nếu có
        if (!imageFile.isEmpty()) {
            try {
                String fileName = saveImage(imageFile);
                existingProduct.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Nếu không chọn ảnh mới, existingProduct vẫn giữ nguyên ảnh cũ

        productService.updateProduct(existingProduct);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    private String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = imageUploadPaths.getDirectory();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String original = file.getOriginalFilename();
        if (original == null || original.isBlank()) {
            original = "image.jpg";
        }
        String safeName = original.trim()
                .replaceAll("\\s+", "_")
                .replaceAll("[^a-zA-Z0-9._-]", "_");
        if (safeName.isBlank()) {
            safeName = "image.jpg";
        }
        String fileName = UUID.randomUUID().toString() + "_" + safeName;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
