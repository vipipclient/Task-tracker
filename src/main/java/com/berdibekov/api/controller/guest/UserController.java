package com.berdibekov.api.controller.guest;


import com.berdibekov.domain.User;
import com.berdibekov.dto.error.ErrorDetail;
import com.berdibekov.exception.ResourceNotFoundException;
import com.berdibekov.exception.ValidationException;

import com.berdibekov.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping({"/api/"})
@RestController("UserController")
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/active.polls", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all active polls", response = User.class)
    public ResponseEntity<?> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
