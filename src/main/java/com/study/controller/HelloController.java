package com.study.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/world")
    public String world() {
        return "hello world";
    }

    @RequestMapping("/preAuthorizeTest")
    @PreAuthorize("hasAnyRole('ADMIN', 'DICT_ALL','DICT_EDIT')")
    public String preAuthorizeTest() {
        return "preAuthorizeTest";
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String hasRoleTest() {
        return "hasRole";
    }

}
