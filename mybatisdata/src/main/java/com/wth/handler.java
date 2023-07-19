package com.wth;

import com.wth.entity.Message;
import com.wth.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class handler {
    @Autowired
    private MessageService service;

    @RequestMapping("/h")
        public String h(){
        Message select = service.select();
        return "redirect:hell";
    }
    @RequestMapping("/hell")
    public String hello(){
        return "h";
    }
}
