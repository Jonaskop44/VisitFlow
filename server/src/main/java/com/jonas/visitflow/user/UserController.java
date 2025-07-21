package com.jonas.visitflow.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @GetMapping("/api/userinfo")
    public String userInfo(Principal principal) {
        return "User information accessed successfully! " + principal.getName();
    }
}
