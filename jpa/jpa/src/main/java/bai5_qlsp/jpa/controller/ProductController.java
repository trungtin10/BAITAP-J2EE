package bai5_qlsp.jpa.controller;

import bai5_qlsp.jpa.model.Product;
import bai5_qlsp.jpa.service.CategoryService;
import bai5_qlsp.jpa.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // 1. Hiển thị danh sách sản phẩm
    @GetMapping
    public String listProducts(Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("products", productList);
        return "product/list"; // Trỏ đến file list.html trong templates/product
    }

    // 2. Hiển thị form thêm mới sản phẩm
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add"; // Trỏ đến file add.html trong templates/product
    }

    // 3. Lưu sản phẩm (dùng chung cho cả thêm mới và cập nhật)
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalFilename = imageFile.getOriginalFilename();
                String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                
                String uploadDir = "src/main/resources/static/images/";
                Path uploadPath = Paths.get(uploadDir);
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                Path filePath = uploadPath.resolve(uniqueFilename);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                product.setImage(uniqueFilename);
            } else if (product.getId() != null) {
                // Keep the old image if not updating
                Product existingProduct = productService.getProductById(product.getId());
                if (existingProduct != null) {
                    product.setImage(existingProduct.getImage());
                }
            }
            
            productService.saveProduct(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/products";
    }

    // 4. Hiển thị form chỉnh sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        // Lấy thông tin sản phẩm dựa trên ID
        Product product = productService.getProductById(id);

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());

        // Bạn có thể tạo riêng 1 file edit.html hoặc tái sử dụng file add.html (nếu form giống nhau)
        return "product/edit";
    }

    // 5. Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        // Xóa sản phẩm theo ID
        productService.deleteProduct(id);

        return "redirect:/products"; // Chuyển hướng về trang danh sách sau khi xóa thành công
    }
}
