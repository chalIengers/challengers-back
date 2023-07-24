package org.knulikelion.challengers_backend.controller;

import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.UserRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.UserUpdateRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/get")
    public Object getUserById(Long id){
        Object result = userService.getUserById(id);
        return result;
    }
    @PostMapping("/create")
    public ResultResponseDto createProject(@RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return userService.createUser(userUpdateRequestDto);
    }
    @DeleteMapping("/remove")
    public ResultResponseDto removeUserById(Long id){
        return userService.removeUser(id);
    }
    @PutMapping("/update")
    public ResultResponseDto updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto, Long userId) throws Exception {
        return userService.updateUser(userId,userUpdateRequestDto);
    }
}
