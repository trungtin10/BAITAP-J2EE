package KT_Giuaki.KT.controller;

import KT_Giuaki.KT.entity.Course;
import KT_Giuaki.KT.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CourseService courseService;

    @GetMapping({"/", "/home", "/courses"})
    public String home(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        if (page < 0) page = 0;
        Page<Course> coursePage = search != null && !search.isBlank()
                ? courseService.searchByName(search, page, 5)
                : courseService.findAll(page, 5);

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("search", search);
        return "home";
    }
}
