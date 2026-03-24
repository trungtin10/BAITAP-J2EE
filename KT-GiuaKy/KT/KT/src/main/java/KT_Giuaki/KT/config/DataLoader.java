package KT_Giuaki.KT.config;

import KT_Giuaki.KT.entity.Category;
import KT_Giuaki.KT.entity.Course;
import KT_Giuaki.KT.entity.Role;
import KT_Giuaki.KT.entity.Student;
import KT_Giuaki.KT.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleRepository.count() > 0) return;

        Role adminRole = roleRepository.save(new Role(null, "ADMIN", new HashSet<>()));
        Role studentRole = roleRepository.save(new Role(null, "STUDENT", new HashSet<>()));

        Student admin = new Student();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@course.edu");
        admin.getRoles().add(adminRole);
        studentRepository.save(admin);

        Student student = new Student();
        student.setUsername("student");
        student.setPassword(passwordEncoder.encode("student123"));
        student.setEmail("student@course.edu");
        student.getRoles().add(studentRole);
        studentRepository.save(student);

        Category cat1 = categoryRepository.save(new Category(null, "Công nghệ thông tin", new HashSet<>()));
        Category cat2 = categoryRepository.save(new Category(null, "Kinh tế", new HashSet<>()));
        Category cat3 = categoryRepository.save(new Category(null, "Ngoại ngữ", new HashSet<>()));

        List.of(
            new Course(null, "Lập trình Java", "https://picsum.photos/300/200?random=1", 4, "Nguyễn Văn A", cat1, new HashSet<>()),
            new Course(null, "Cơ sở dữ liệu", "https://picsum.photos/300/200?random=2", 3, "Trần Thị B", cat1, new HashSet<>()),
            new Course(null, "Mạng máy tính", "https://picsum.photos/300/200?random=3", 3, "Lê Văn C", cat1, new HashSet<>()),
            new Course(null, "Kinh tế vi mô", "https://picsum.photos/300/200?random=4", 2, "Phạm Thị D", cat2, new HashSet<>()),
            new Course(null, "Tiếng Anh 1", "https://picsum.photos/300/200?random=5", 2, "Hoàng Văn E", cat3, new HashSet<>()),
            new Course(null, "Web Development", "https://picsum.photos/300/200?random=6", 4, "Nguyễn Thị F", cat1, new HashSet<>())
        ).forEach(courseRepository::save);
    }
}
