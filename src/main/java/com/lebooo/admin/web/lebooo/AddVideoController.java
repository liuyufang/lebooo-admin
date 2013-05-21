package com.lebooo.admin.web.lebooo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: Wei Liu
 * Date: 13-5-21
 * Time: PM2:41
 */
@Controller
@RequestMapping(value = "/lebooo/addvideo")
public class AddVideoController {
    @RequestMapping(method = RequestMethod.GET)
    public String addVideo(){
        return "lebooo/addVideo";
    }
}
