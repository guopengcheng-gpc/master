package com.temp.myself.temp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PhoneVideo {

    @RequestMapping("/phone")
    public ModelAndView imageDiShow(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("phone/phoneVideo.html");
        StringBuffer url = request.getRequestURL();
        String path = url.toString().replaceAll("//","@");
        System.out.println(path.split("/")[0].replaceAll("@","//"));
        modelAndView.addObject("path",path.split("/")[0].replaceAll("@","//"));
        String sessionId = request.getSession().getId();
        modelAndView.addObject("sessionId",sessionId);
        return modelAndView;
    }
}
