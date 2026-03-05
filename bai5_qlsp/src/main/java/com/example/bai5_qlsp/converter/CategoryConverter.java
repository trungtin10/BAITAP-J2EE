package com.example.bai5_qlsp.converter;

import com.example.bai5_qlsp.model.Category;
import com.example.bai5_qlsp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<String, Category> {

    @Autowired
    private CategoryService categoryService;

    @Override
    public Category convert(String source) {
        // Nếu source là rỗng hoặc null, không chuyển đổi
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            // Chuyển chuỗi ID thành Long và tìm Category trong DB
            Long id = Long.parseLong(source);
            return categoryService.getCategoryById(id).orElse(null);
        } catch (NumberFormatException e) {
            // Nếu source không phải là số, trả về null
            return null;
        }
    }
}
