package com.eCommerce.CartOperations.service.user;

import com.eCommerce.CartOperations.dto.UserDto;
import com.eCommerce.CartOperations.exceptions.ResourceNotFoundException;
import com.eCommerce.CartOperations.model.User;
import com.eCommerce.CartOperations.repository.userRepository;
import com.eCommerce.CartOperations.request.UserCreateRequest;
import com.eCommerce.CartOperations.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final userRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("user not found"));
    }

    @Override
    public User createUser(UserCreateRequest request) {
        return Optional.of(request).filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(request1-> {
                    User user=new User();
                    user.setFirstName(request1.getFirstName());
                    user.setLastName(request1.getLastName());
                    user.setEmail(request1.getEmail());
                    user.setPassword(passwordEncoder.encode(request1.getPassword()));
                    return userRepository.save(user);
                }).orElseThrow(()-> new ResourceNotFoundException("Already user Exist with this"+request.getEmail()));
    }

    @Override
    public User updateUser(UserUpdateRequest request,Long userId) {
        return userRepository.findById(userId)
                .map(existUser->{
                    existUser.setFirstName(request.getFirstName());
                    existUser.setLastName(request.getLastName());
                    return  userRepository.save(existUser);
                }).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));

    }

    @Override
    public void deleteUserById(Long userId) {
         userRepository.findById(userId).ifPresentOrElse(userRepository::delete,()->{
             throw new ResourceNotFoundException("user not found");
         });
    }

    @Override
    public UserDto convertedToDto(User user){
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public User getAuthencatedUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        return userRepository.findByEmail(email);
    }

}
