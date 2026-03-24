package KT_Giuaki.KT.security;

import KT_Giuaki.KT.entity.Student;
import KT_Giuaki.KT.repository.RoleRepository;
import KT_Giuaki.KT.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String sub = oauth2User.getAttribute("sub");

        String username = (email != null && !email.isBlank()) ? email : "oauth_" + (sub != null ? sub : "user");

        Optional<Student> existing = studentRepository.findByEmail(email != null && !email.isBlank() ? email : "");
        if (existing.isEmpty() && email != null && !email.isBlank()) {
            existing = studentRepository.findByUsername(username);
        }
        Student student;
        if (existing.isPresent()) {
            student = existing.get();
        } else {
            student = new Student();
            student.setUsername(username);
            student.setEmail(email != null && !email.isBlank() ? email : username + "@oauth.local");
            student.setPassword("{noop}oauth2");
            roleRepository.findByName("STUDENT").ifPresent(r -> student.getRoles().add(r));
            studentRepository.save(student);
        }

        return new CustomOAuth2User(oauth2User, student.getUsername(), student.getRoles());
    }
}
