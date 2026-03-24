package KT_Giuaki.KT.controller;

import KT_Giuaki.KT.entity.Category;
import KT_Giuaki.KT.entity.Course;
import KT_Giuaki.KT.service.CategoryService;
import KT_Giuaki.KT.service.CourseService;
import KT_Giuaki.KT.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final CourseService courseService;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.findAll(0, 100).getContent());
        return "admin/courses";
    }

    @GetMapping("/courses/new")
    public String createForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/course-form";
    }

    @PostMapping("/courses")
    public String create(@Valid @ModelAttribute Course course, BindingResult result,
                         @RequestParam(required = false) Long categoryId,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/course-form";
        }
        Category category = categoryId != null ? categoryService.findById(categoryId) : null;
        if (category == null) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("error", "Vui lòng chọn danh mục hợp lệ.");
            return "admin/course-form";
        }
        course.setCategory(category);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = fileStorageService.store(imageFile);
                if (imageUrl != null) course.setImage(imageUrl);
            } catch (IOException ignored) {}
        }
        courseService.save(course);
        redirect.addFlashAttribute("message", "Thêm học phần thành công!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/courses/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Course course = courseService.findByIdWithCategory(id);
        if (course == null) return "redirect:/admin/courses";
        model.addAttribute("course", course);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/course-form";
    }

    @PostMapping("/courses/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Course course,
                        BindingResult result, @RequestParam(required = false) Long categoryId,
                        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                        Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            course.setId(id);
            Course existingForImage = courseService.findByIdWithCategory(id);
            if (existingForImage != null) {
                course.setImage(existingForImage.getImage());
                course.setCategory(existingForImage.getCategory());
            }
            model.addAttribute("course", course);
            model.addAttribute("categories", categoryService.findAll());
            return "admin/course-form";
        }
        Course existing = courseService.findById(id);
        if (existing == null) return "redirect:/admin/courses";
        Category category = categoryId != null ? categoryService.findById(categoryId) : null;
        if (category == null) {
            course.setId(id);
            Course existingForForm = courseService.findByIdWithCategory(id);
            if (existingForForm != null) {
                course.setImage(existingForForm.getImage());
                course.setCategory(existingForForm.getCategory());
            }
            model.addAttribute("course", course);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("error", "Vui lòng chọn danh mục hợp lệ.");
            return "admin/course-form";
        }
        existing.setName(course.getName());
        existing.setCredits(course.getCredits());
        existing.setLecturer(course.getLecturer());
        existing.setCategory(category);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = fileStorageService.store(imageFile);
                if (imageUrl != null) existing.setImage(imageUrl);
            } catch (IOException ignored) {}
        }
        courseService.save(existing);
        redirect.addFlashAttribute("message", "Cập nhật học phần thành công!");
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        courseService.deleteById(id);
        redirect.addFlashAttribute("message", "Xóa học phần thành công!");
        return "redirect:/admin/courses";
    }
}
