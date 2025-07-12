package com.eCommerce.CartOperations.controller;

import com.eCommerce.CartOperations.dto.UserDto;
import com.eCommerce.CartOperations.exceptions.AlreadyExistsException;
import com.eCommerce.CartOperations.exceptions.ResourceNotFoundException;
import com.eCommerce.CartOperations.model.User;
import com.eCommerce.CartOperations.request.UserCreateRequest;
import com.eCommerce.CartOperations.request.UserUpdateRequest;
import com.eCommerce.CartOperations.response.ApiResponse;
import com.eCommerce.CartOperations.service.user.IUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            User user=userService.getUserById(userId);
            UserDto userDto=userService.convertedToDto(user);
            return ResponseEntity.ok(new ApiResponse("success",userDto));
        } catch (ResourceNotFoundException e) {
           return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }
    @PostMapping("/user/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateRequest request){
        try {
            User user=  userService.createUser(request);
            UserDto userDto=userService.convertedToDto(user);
            return ResponseEntity.ok(new ApiResponse("success",userDto));
        } catch (AlreadyExistsException e) {
           return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PutMapping("/user/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request,@PathVariable Long userId){
        try {
            User user=userService.updateUser(request,userId);
            UserDto userDto=userService.convertedToDto(user);
            return ResponseEntity.ok(new ApiResponse("update success",userDto));
        } catch (ResourceNotFoundException e) {
             return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @DeleteMapping("/user/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
           userService.deleteUserById(userId);
            return ResponseEntity.ok(new ApiResponse("delete success",userId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }


}
