package com.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/toPage")
public class LoginPageController {

    @RequestMapping("/{page}")
    public String  requestPartHtmlLogin(@PathVariable String page){
        return page;
    }


    @RequestMapping(value="/test/test01/{name}" , method = RequestMethod.GET)
    public String test0(@PathVariable String name) {
        return "redirect:/backend/hellow.html";
    }

    @RequestMapping(value="/test/test02" , method = RequestMethod.GET)
    public String test1() {
        return "redirect:/backend/login.html";
    }

}
