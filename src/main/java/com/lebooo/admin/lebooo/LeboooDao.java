package com.lebooo.admin.lebooo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-5-21
 * Time: AM10:26
 */
public class LeboooDao extends SimpleHttpClient{
    private static String leboLoginSalt = "ch1putaoButuputaopi";

    private static String leboServerIp = "219.142.106.138";
    private static String leboApi = "http://"+ leboServerIp +":8080/cmd=GEOWEIBOD.handle_lebo_service2/";
    private static String leboUploadApi = "http://"+ leboServerIp +":8080/cmd=GEOWEIBOD.handle_upload_service2";

    public String login(String accountId) {
        LeboPostData data = new LeboPostData("loginMultiSession");
        Map params = Maps.newHashMap();
        params.put("pass", getPass(accountId));
        params.put("username", accountId);
        data.setParams(params);

        String responseText = postPlainText(leboApi, jsonMapper.toJson(data));
        LeboResponseData responseData = jsonMapper.fromJson(responseText, LeboResponseData.class);
        return authorization(responseData);
    }


//    public void createRobot() {
//        Map map = Maps.newHashMap();
//        map.put("a", "aa");
//        String url = "http://219.142.106.139:8080/cmd=GEOWEIBOD.handle_lebo_service2/?name=robot1&gender=m&sign=mysign";
//        String reponseText = postPlainText(leboApi, jsonMapper.toJson(map));
//        System.out.println(reponseText);
//
//    }

//    public void searchUser() {
//        Map map = Maps.newHashMap();
//        map.put("a", "aa");
//        String url = "http://219.142.106.139:8080/cmd=GEOWEIBOD.handle_lebo_service2/?name=robot1&gender=m&sign=mysign";
//        String reponseText = postPlainText(leboApi, jsonMapper.toJson(map));
//        System.out.println(reponseText);
//    }

    /**
     *
     * @param authorization
     * @param video
     * @param photo
     * @param fileName
     * @param fileDescription
     *
     *
     * 成功上传服务器响应:
     * {
    "error": "OK",
    "result": {
    "authorID": "storage_1804289383_113",
    "authorKey": "storage_1804289383_113",
    "authorPhotoID": "photo-mlacas-.jpeg",
    "authorPhotoUrl": "/?cmd=GEOWEIBOD.mime3+photo-mlacas-.jpeg",
    "content": "test upload video",
    "isAutoPost": 0,
    "leboID": "storage_1804289383_2321",
    "movieID": "photo-mnapcz-o.mp4",
    "movieUrl": "/?cmd=GEOWEIBOD.mime3+photo-mnapcz-o.mp4",
    "photoID": "mime-mnapcz-q",
    "photoUrl": "/?cmd=GEOWEIBOD.topic_mime3+storage_1804289383_2321+photo-mnapcz-p.png",
    "submitTime": 1369386467
    },
    "version": "1.0"
    }
     */
    public LeboResponseData publishVideo(String authorization, File video, File photo, String fileName, String fileDescription){
        String url = leboUploadApi + "%20publishLebo/?" + authorization;

        HttpPost httpPost = new HttpPost (url);
        try{
            MultipartEntity multiPartEntity = new MultipartEntity () ;
            multiPartEntity.addPart("content", new StringBody(fileDescription != null ? fileDescription : "")) ;
            multiPartEntity.addPart("movie", new FileBody(video, "application/octect-stream"));
            multiPartEntity.addPart("photo", new FileBody(photo, "application/octect-stream"));
            httpPost.setEntity(multiPartEntity);

            HttpResponse response = httpClient.execute(httpPost);
            checkStatus(response);
            HttpEntity responseHttpEntiry = response.getEntity();
            return jsonMapper.fromJson(getResponseText(responseHttpEntiry), LeboResponseData.class);
        } catch (IOException e) {
            httpPost.abort();
            throw new RuntimeException("上传视频出错", e);
        }

    }

    public LeboResponseData getAccountInfo(String auth, String accountId){
        LeboPostData postData = new LeboPostData("getAccountInfo");
        Map<String, Object> map = Maps.newHashMap();
        map.put("accountID", accountId);
        postData.setParams(map);

        String responseText = postPlainText(leboApi, jsonMapper.toJson(postData));
        LeboResponseData responseData = jsonMapper.fromJson(responseText, LeboResponseData.class);
        return responseData;
    }

    // ---- Hepler ---
    private static String getPass(String accountId) {
        String str = accountId + leboLoginSalt;
        try {
            return Encodes.encodeHex(Digests.md5(new ByteArrayInputStream(str.getBytes())));
        } catch (IOException e) {
            throw new RuntimeException("计算md5出错", e);
        }
    }

    private static String authorization(LeboResponseData loginResponseData) {
        List<String> list = Lists.newArrayList();
        list.add("Global_MultiSession=1");
        list.add("Global_UserName=" + loginResponseData.getResult().get("accountID"));
        list.add("Global_AccessToken=" + loginResponseData.getResult().get("token"));
        list.add("Global_SessionID=" + loginResponseData.getResult().get("sessionID"));

        return StringUtils.join(list,"&");
    }


    // --- Run ---
    public static void main(String[] args) {
        LeboooDao leboooDao = new LeboooDao();
        String accountId = "storage_1804289383_113";
        String auth = leboooDao.login(accountId);
        leboooDao.publishVideo(auth, new File("/Users/liuwei/lebooo/small-video.mp4"), new File("/Users/liuwei/lebooo/small-photo.png"), "test-file-name", "test upload video 2");
        LeboResponseData data = leboooDao.getAccountInfo(auth, accountId);
        System.out.println(data.getResult().get("topicCount"));
    }
}




