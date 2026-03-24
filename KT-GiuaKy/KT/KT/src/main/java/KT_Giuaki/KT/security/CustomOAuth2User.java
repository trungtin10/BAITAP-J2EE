package KT_Giuaki.KT.security;

import KT_Giuaki.KT.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oauth2User;
    private final String username;
    private final Set<Role> roles;

    public CustomOAuth2User(OAuth2User oauth2User, String username, Set<Role> roles) {
        this.oauth2User = oauth2User;
        this.username = username;
        this.roles = roles != null ? roles : Collections.emptySet();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getName() {
        return username;
    }
}
