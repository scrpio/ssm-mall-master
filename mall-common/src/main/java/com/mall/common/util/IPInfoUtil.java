package com.mall.common.util;

import com.google.gson.Gson;
import com.mall.common.model.IpWeatherResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPInfoUtil {
    private static final Logger log = LoggerFactory.getLogger(IPInfoUtil.class);
    /**
     * Mob全国天气预报接口
     */
    private final static String GET_WEATHER = "http://apicloud.mob.com/v1/weather/ip?key=275e1ff67aa34&ip=";

    /**
     * 获取客户端IP地址
     *
     * @param request 请求
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");//X-Forwarded-For：Squid 服务代理
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");//Proxy-Client-IP：apache 服务代理
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");//WL-Proxy-Client-IP：weblogic 服务代理
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");//HTTP_CLIENT_IP：有些代理服务器
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");//X-Real-IP：nginx服务代理
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * 获取IP返回地理天气信息
     *
     * @param ip ip地址
     * @return
     */
    public static String getIpInfo(String ip) {
        if (null != ip) {
            String url = GET_WEATHER + "59.42.27.119";
            String result = HttpUtil.sendGet(url);
            return result;
        }
        return null;
    }

    /**
     * 获取IP返回地理信息
     *
     * @param ip ip地址
     * @return
     */
    public static String getIpCity(String ip) {
        if (null != ip) {
            String url = GET_WEATHER + "59.42.27.119";
            String json = HttpUtil.sendGet(url);
            String result = "未知";
            try {
                IpWeatherResult weather = new Gson().fromJson(json, IpWeatherResult.class);
                if (weather.getResult() != null) {
                    result = weather.getResult().get(0).getCity() + " " + weather.getResult().get(0).getDistrct();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    public static void main(String[] args) {
        log.info(getIpInfo("59.42.27.119"));
    }
}
