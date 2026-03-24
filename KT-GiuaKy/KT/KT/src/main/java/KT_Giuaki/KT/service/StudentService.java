package KT_Giuaki.KT.service;

import KT_Giuaki.KT.entity.Role;
import KT_Giuaki.KT.entity.Student;

import KT_Giuaki.KT.repository.RoleRepository;
import KT_Giuaki.KT.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Student register(String username, String password, String email) {
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new IllegalStateException("Role STUDENT chưa tồn tại. Chạy ứng dụng lần đầu để DataLoader tạo roles."));
        Student student = new Student();
        student.setUsername(username.trim());
        student.setPassword(passwordEncoder.encode(password));
        student.setEmail(email.trim().toLowerCase());
        student.getRoles().add(studentRole);
        return studentRepository.saveAndFlush(student);
    }

    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return studentRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }
}
