package com.mall.common.util;

import com.google.gson.Gson;
import com.mall.common.jedis.JedisClient;
import com.mall.common.model.Note;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class NoteUtil {
    private static final String ACCOUNTSID = "";
    private static final String AUTHTOKEN = "";
    private static final String RESTURL = "https://api.ucpaas.com";
    private static final String APPID = "";
    private static final String TEMPLETEID = "249500";
    private static final String VERSION = "2014-06-30";
    private static final String TIP = "TIP";
    @Autowired
    private JedisClient jedisClient;
    @Value("PARAM")
    private String PARAM;

    public HttpResponse post(CloseableHttpClient client, String uri, String body)
            throws Exception {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setHeader("Authorization", getAuthorization());
        if (body != null && body.length() > 0) {
            BasicHttpEntity entity = new BasicHttpEntity();
            entity.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
            entity.setContentLength(body.getBytes().length);
            httpPost.setEntity(entity);
        }
        HttpResponse response = client.execute(httpPost);
        return response;
    }

    public String checkNum(String to,String param) {
        String resp = "";
//        String param = getParam();
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String uri = getSmsRestUri();
        Note bean = new Note();
        bean.setAppId(APPID);
        bean.setParam(param);
        bean.setTemplateId(TEMPLETEID);
        bean.setTo(to);
        String body = new Gson().toJson(bean);
        body = "{\"templateSMS\":" + body + "}";
        try {
            HttpResponse response = post(client, uri, body);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                resp = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resp;
    }

    /**
     * 验证码
     * @return
     */
    public String getParam(){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        String param = sb.toString();
        //缓存验证码
        String paramId = UUID.randomUUID().toString().replace("-", "");
        jedisClient.set(PARAM + ":" + paramId , param);
        return param;
    }

    private String getSmsRestUri() {
        StringBuffer sb = new StringBuffer();
        String url = sb.append(RESTURL).append("/").append(VERSION).append("/")
                .append("Accounts").append("/").append(ACCOUNTSID)
                .append("/Messages/templateSMS").append("?sig=")
                .append(getSigParameter()).toString();
        return url;
    }

    private String getAuthorization() throws Exception {
        String src = ACCOUNTSID + ":" + getTimeStamp();
        String authorization = ToolUtil.base64Encoder(src);

        return authorization;
    }

    private String getSigParameter() {
        String timeStamp = getTimeStamp();
        String sig = "";
        try {
            sig = ToolUtil.md5Digest(ACCOUNTSID + AUTHTOKEN + timeStamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sig.toUpperCase();// 转大写
    }

    /**
     * 时间戳是当前系统时间（24小时制），格式“yyyyMMddHHmmss”，需与SigParameter中时间戳相同。
     *
     * @return
     */
    private String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        return date;
    }
}
