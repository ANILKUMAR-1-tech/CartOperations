package com.eCommerce.CartOperations.security.user;

import com.eCommerce.CartOperations.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EcomUserDetails implements UserDetails {
    private Long id;
    private String email;
    private String password;

    private Collection<GrantedAuthority> authorities;


    public static EcomUserDetails build(User user){
        List<GrantedAuthority> authorities=user.getRoles()
                .stream()
                .map(roles -> new SimpleGrantedAuthority(roles.getName()))
                .collect(Collectors.toList());
        return new EcomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
