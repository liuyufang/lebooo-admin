package com.lebooo.admin.web.lebooo;

import com.lebooo.admin.entity.Task;
import com.lebooo.admin.entity.User;
import com.lebooo.admin.service.account.AccountService;
import com.lebooo.admin.service.account.ShiroDbRealm;
import com.lebooo.admin.service.lebooo.LeboResponseData;
import com.lebooo.admin.service.lebooo.LeboooService;
import com.lebooo.admin.service.schedule.PublishVideoScheduleService;
import com.lebooo.admin.service.task.TaskService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springside.modules.mapper.JsonMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Autowired
    private PublishVideoScheduleService publishVideoScheduleService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AccountService accountService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    private SimpleDateFormat sdfForVideoName = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    @RequestMapping(method = RequestMethod.GET)
    public String publishVideoForm() {
        return "lebooo/publishVideo";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String publishVideo(@RequestParam(value = "username") String username,
                               @RequestParam(value = "video") MultipartFile video,
                               @RequestParam(value = "photo") MultipartFile photo,
                               @RequestParam(value = "description") String description,
                               @RequestParam(value = "publishTimeOption") String publishTimeOption,
                               HttpServletRequest request, ModelMap model) {
        try {
            File tempVideoFile = File.createTempFile(Task.TYPE_VALUE_PUBLISH_VIDEO, ".video");
            video.transferTo(tempVideoFile);

            File tempPhotoFile =  File.createTempFile(Task.TYPE_VALUE_PUBLISH_VIDEO, ".photo");
            photo.transferTo(tempPhotoFile);

            String authorization = leboooService.login(username);
            LeboResponseData leboResponseData = leboooService.publishVideo(authorization, tempVideoFile, tempPhotoFile, description);

            tempVideoFile.delete();
            tempPhotoFile.delete();

            if(leboResponseData != null ){
                model.put("message", "发布视频成功");
            }else{
                model.put("error", "发布视频失败，请重试");
            }
        } catch (Exception e) {
            model.put("error", "发布视频失败，请重试");
        }
        return "lebooo/publishVideo";
    }

    @RequestMapping(value = "schedule", method = RequestMethod.POST)
    public String publishVideoSchedule(@RequestParam(value = "username") String username,
                               @RequestParam(value = "video") MultipartFile video,
                               @RequestParam(value = "photo") MultipartFile photo,
                               @RequestParam(value = "description") String description,
                               @RequestParam(value = "publishDate") String publishDate,
                               @RequestParam(value = "publishTime") String publishTime,
                               HttpServletRequest request, ModelMap model) {
        try {
            File uploadDirectory = new File(publishVideoScheduleService.getUploadDirectoryPath());
            if(!uploadDirectory.exists()){
                uploadDirectory.mkdirs();
            }
            String fileNamePrefix = sdfForVideoName.format(new Date());
            File videoFile = new File(uploadDirectory, fileNamePrefix + ".video");
            File photoFile = new File(uploadDirectory, fileNamePrefix + ".photo");

            video.transferTo(videoFile);
            photo.transferTo(photoFile);

            PublishVideoScheduleService.PublishVideoInfo info = new PublishVideoScheduleService.PublishVideoInfo();
            info.setDescription(description);
            info.setUsername(username);
            info.setPhoto(photoFile.getName());
            info.setVideo(videoFile.getName());

            Task task = new Task();
            ShiroDbRealm.ShiroUser shiroUser = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
            User user = accountService.getUser(shiroUser.id);
            task.setUser(user);
            task.setTitle("发布视频 " + videoFile.getName());
            task.setDescription(jsonMapper.toJson(info));
            task.setScheduleTime(sdf.parse(publishDate + " " + publishTime));
            task.setType(Task.TYPE_VALUE_PUBLISH_VIDEO);
            task.setStatus(Task.STATUS_VALUE_TODO);
            taskService.saveTask(task);

            model.put("message", "发布视频成功(定时发布)");
        } catch (Exception e) {
            model.put("error", "发布视频失败，请重试");
        }
        return "lebooo/publishVideo";
    }
}
