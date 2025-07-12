package com.eCommerce.CartOperations.data;

import ch.qos.logback.core.CoreConstants;
import com.eCommerce.CartOperations.model.Roles;
import com.eCommerce.CartOperations.model.User;
import com.eCommerce.CartOperations.repository.RoleRepository;
import com.eCommerce.CartOperations.repository.userRepository;
import com.eCommerce.CartOperations.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitilizer implements ApplicationListener<ApplicationReadyEvent> {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultroles = Set.of("ROLE_ADMIN", "ROLE_USER");
//        createDefaultUserIfNotExist();
        createDefaultRolesIfNotExist(defaultroles);
//        createDefaulAdminIfNotExist();
    }

    private void createDefaultRolesIfNotExist(Set<String> roles) {

        roles.stream()
                .filter(roleName -> roleRepository.findByName(roleName).isEmpty())
                .map(Roles::new)
                .forEach(roleRepository::save);
    }


//    private void createDefaultUserIfNotExist() {
//       Roles userRoles = roleRepository.findByName("ROLE_USER").get();
//        for (int i=1;i<=5;i++){
//            String email="default"+i+"@gamil.com";
//            if (userRepository.existsByEmail(email))
//            {
//                continue;
//            }
//            User user=new User();
//            user.setPassword(passwordEncoder.encode("123"));
//            user.setEmail("default"+i);
//            user.setFirstName("user");
//            user.setLastName("user"+i);
//            user.setRoles(Set.of(userRoles));
//            userRepository.save(user);
//        }
//}


//    private void createDefaulAdminIfNotExist() {
//        Roles adminrRoles = roleRepository.findByName("ROLE_ADMIN").get();
//        for (int i=1;i<=2;i++){
//            String email="default"+i+"@gamil.com";
//            if (userRepository.existsByEmail(email))
//            {
//                continue;
//            }
//            User user=new User();
//            user.setPassword(passwordEncoder.encode("123"));
//            user.setEmail("Admin"+i);
//            user.setFirstName("admin");
//            user.setLastName("admin"+i);
//            user.setRoles(Set.of(adminrRoles));
//            userRepository.save(user);
//        }
//
//    }

}
