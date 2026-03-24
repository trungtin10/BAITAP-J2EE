package KT_Giuaki.KT.controller;

import KT_Giuaki.KT.dto.RegisterDto;
import KT_Giuaki.KT.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final StudentService studentService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("student", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("student") RegisterDto dto, BindingResult result,
                           Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("student", dto);
            return "register";
        }
        String username = dto.getUsername() != null ? dto.getUsername().trim() : "";
        String email = dto.getEmail() != null ? dto.getEmail().trim().toLowerCase() : "";
        if (studentService.existsByUsername(username)) {
            result.rejectValue("username", "error.username", "Tên đăng nhập đã tồn tại");
            model.addAttribute("student", dto);
            return "register";
        }
        if (studentService.existsByEmail(email)) {
            result.rejectValue("email", "error.email", "Email đã tồn tại");
            model.addAttribute("student", dto);
            return "register";
        }
        studentService.register(username, dto.getPassword(), email);
        redirect.addFlashAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }
}
