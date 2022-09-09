package com.temp.myself.temp.controller;

import com.temp.myself.temp.entiy.Person;
import com.temp.myself.temp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class Test {

    @Autowired
    public PersonService personService;

    @RequestMapping("/test")
    public ModelAndView test(){
        ModelAndView modelAndView = new ModelAndView("ceshi.html");
        List<Person> list = personService.getPersonList();
        modelAndView.addObject("list",list);
        return modelAndView;
    }
}
