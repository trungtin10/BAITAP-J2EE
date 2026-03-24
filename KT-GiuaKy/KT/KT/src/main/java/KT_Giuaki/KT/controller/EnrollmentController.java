package KT_Giuaki.KT.controller;

import KT_Giuaki.KT.entity.Course;
import KT_Giuaki.KT.entity.Student;
import KT_Giuaki.KT.service.CourseService;
import KT_Giuaki.KT.service.EnrollmentService;
import KT_Giuaki.KT.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final StudentService studentService;

    private String getUsername(Object principal) {
        if (principal instanceof User u) return u.getUsername();
        if (principal instanceof OAuth2User o) return o.getName();
        return principal != null ? principal.toString() : null;
    }

    @PostMapping("/enroll/{courseId}")
    public String enroll(@PathVariable Long courseId,
                        @AuthenticationPrincipal Object principal,
                        RedirectAttributes redirect) {
        String username = getUsername(principal);
        Student student = username != null ? studentService.findByUsername(username) : null;
        Course course = courseService.findById(courseId);
        if (student == null || course == null) {
            redirect.addFlashAttribute("error", "Không thể đăng ký học phần.");
            return "redirect:/home";
        }
        if (enrollmentService.enroll(student, course)) {
            redirect.addFlashAttribute("message", "Đăng ký học phần thành công!");
        } else {
            redirect.addFlashAttribute("error", "Bạn đã đăng ký học phần này rồi.");
        }
        return "redirect:/home";
    }

    @GetMapping("/my-courses")
    public String myCourses(@AuthenticationPrincipal Object principal, Model model) {
        String username = getUsername(principal);
        Student student = username != null ? studentService.findByUsername(username) : null;
        if (student == null) return "redirect:/login";
        model.addAttribute("enrollments", enrollmentService.findByStudent(student));
        return "my-courses";
    }
}
