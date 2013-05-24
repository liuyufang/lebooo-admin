package com.lebooo.admin.web.lebooo;

import com.lebooo.admin.service.lebooo.LeboResponseData;
import com.lebooo.admin.service.lebooo.LeboooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * @author: Wei Liu
 * Date: 13-5-21
 * Time: PM2:41
 */
@Controller
@RequestMapping(value = "/lebooo/publishvideo")
public class PublishVideoController {
    @Autowired
    private LeboooService leboooService;

    @RequestMapping(method = RequestMethod.GET)
    public String publishVideoForm() {
        return "lebooo/publishVideo";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String publishVideo(@RequestParam(value = "username") String username,
                               @RequestParam(value = "video") MultipartFile video,
                               @RequestParam(value = "photo") MultipartFile photo,
                               @RequestParam(value = "description") String description,
                               HttpServletRequest request, ModelMap model) {
        try {
            File tempVideoFile = File.createTempFile("publish-video", ".lebooo-admin");
            video.transferTo(tempVideoFile);

            File tempPhotoFile =  File.createTempFile("publish-video", ".lebooo-admin");
            photo.transferTo(tempPhotoFile);

            String authorization = leboooService.login(username);
            LeboResponseData leboResponseData = leboooService.publishVideo(authorization, tempVideoFile, tempPhotoFile, description);

            tempVideoFile.delete();
            tempPhotoFile.delete();

            model.put("message", "发布视频成功");
        } catch (Exception e) {
            model.put("error", "发布视频失败，请重试");
        }
        return "lebooo/publishVideo";
    }
}
