package com.example.bai5_qlsp.service;

import com.example.bai5_qlsp.model.Product;
import com.example.bai5_qlsp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    private static final int PAGE_SIZE = 5;

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword, Long categoryId, int page, Sort sort) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), PAGE_SIZE, sort);
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasCategory = categoryId != null;

        if (hasKeyword && hasCategory) {
            return productRepository.findByNameContainingIgnoreCaseAndCategory_Id(
                    keyword.trim(), categoryId, pageable);
        }
        if (hasKeyword) {
            return productRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
        }
        if (hasCategory) {
            return productRepository.findByCategory_Id(categoryId, pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
