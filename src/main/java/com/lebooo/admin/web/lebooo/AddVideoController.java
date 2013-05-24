package com.lebooo.admin.web.lebooo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author: Wei Liu
 * Date: 13-5-21
 * Time: PM2:41
 */
@Controller
@RequestMapping(value = "/lebooo/addvideo")
public class AddVideoController {
    @RequestMapping(method = RequestMethod.GET)
    public String addVideoForm(){
        return "lebooo/addVideo";
    }

    @RequestMapping(value = "add")
    public String addVideo(@RequestParam(value = "file", required = false) MultipartFile file,
                         HttpServletRequest request, ModelMap model) {

        System.out.println("开始");
        String path = request.getSession().getServletContext().getRealPath("upload");
        String fileName = file.getOriginalFilename();
        //        String fileName = new Date().getTime()+".jpg";
        System.out.println(path);
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        //保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileUrl", request.getContextPath() + "/upload/" + fileName);

        return "result";
    }
}
