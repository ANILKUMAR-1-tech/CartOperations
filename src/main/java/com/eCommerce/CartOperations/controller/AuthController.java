package com.eCommerce.CartOperations.controller;

import com.eCommerce.CartOperations.model.Roles;
import com.eCommerce.CartOperations.model.User;
import com.eCommerce.CartOperations.repository.RoleRepository;
import com.eCommerce.CartOperations.repository.userRepository;
import com.eCommerce.CartOperations.request.LoginRequest;
import com.eCommerce.CartOperations.request.RegisterRequest;
import com.eCommerce.CartOperations.response.ApiResponse;
import com.eCommerce.CartOperations.response.JwtResponse;
import com.eCommerce.CartOperations.security.jwt.JwtUtils;
import com.eCommerce.CartOperations.security.user.EcomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager manager;
    private final userRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request){
        try {
            Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt= jwtUtils.generateTokenForuser(authentication);
            EcomUserDetails userDetails= (EcomUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse=new JwtResponse(userDetails.getId(),jwt);
            return ResponseEntity.ok(new ApiResponse("Login success",jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request){
      if(userRepository.existsByEmail(request.getEmail())){
          return ResponseEntity.badRequest().body(new ApiResponse("already registered register with differennt email",null));
      }

        Set<Roles> assignedRoles;


        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            assignedRoles = request.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
        } else {
            Roles defaultUserRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found."));
            assignedRoles = Set.of(defaultUserRole);
        }

        User user =new User();
      user.setEmail(request.getEmail());
      user.setPassword(passwordEncoder.encode(request.getPassword()));
      user.setLastName(request.getLastName());
      user.setFirstName(request.getFirstName());
      user.setRoles(assignedRoles);
      userRepository.save(user);
      return ResponseEntity.ok(new ApiResponse("registration success",null));

    }
}
