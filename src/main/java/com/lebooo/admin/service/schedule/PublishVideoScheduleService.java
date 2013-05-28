package com.lebooo.admin.service.schedule;

import com.lebooo.admin.entity.Task;
import com.lebooo.admin.service.lebooo.LeboooService;
import com.lebooo.admin.service.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springside.modules.mapper.JsonMapper;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-5-27
 * Time: PM2:49
 */
public class PublishVideoScheduleService extends JdkTimerJob{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TaskService taskService;

    @Autowired
    private LeboooService leboooService;

    @Value("${lebooo.upload.dir}")
    private String uploadDirectoryPath;

    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    @Override
    public void run() {
        List<Task> tasks = taskService.getDueTask(new Date(), Task.TYPE_VALUE_PUBLISH_VIDEO);
        for(Task task : tasks){
            PublishVideoInfo info = jsonMapper.fromJson(task.getDescription(), PublishVideoInfo.class);
            logger.info("开始 发布视频 {}", info.getVideo());
            publishVideo(info);
            logger.info("完成 发布视频 {}", info.getVideo());

            task.setStatus(Task.STATUS_VALUE_DONE);
            taskService.saveTask(task);
        }
    }

    private void publishVideo(PublishVideoInfo info){
        String auth = leboooService.login(info.getUsername());
        File uploadDirectory = new File(uploadDirectoryPath);
        File videoFile = new File(uploadDirectory, info.getVideo());
        File photoFile = new File(uploadDirectory, info.getPhoto());
        leboooService.publishVideo(auth, videoFile, photoFile, info.getDescription());

        videoFile.delete();
        photoFile.delete();
    }

    public String getUploadDirectoryPath() {
        return uploadDirectoryPath;
    }

    public static class PublishVideoInfo{
        private String username;
        private String video;
        private String photo;
        private String description;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
