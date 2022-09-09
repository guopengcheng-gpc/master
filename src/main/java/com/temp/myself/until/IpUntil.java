package com.temp.myself.until;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取ip地址工具类
 */
public class IpUntil {

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        // ipAddress = this.getRequest().getRemoteAddr();

        return ipAddress;
    }

    /**
     * 通过ip获取地址
     * @param ip
     * @return
     */
    public static Map getAddressByIp(String ip){
        Map map = new HashMap();
        String path = "https://ip.taobao.com/outGetIpInfo?ip="+ip+"&accessKey=alibaba-inc";
        String jsonString = UrlValue.getHttpsValue(path);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String msg = (String)jsonObject.get("msg");
        if ("query success".equals(msg)){
            JSONObject object = (JSONObject)jsonObject.get("data");
            map.put("country",object.get("country"));//国家
            map.put("city",object.get("city"));//城市
            map.put("isp",object.get("isp"));//运营商
            map.put("region",object.get("region"));//省份
        }else{
            System.out.println(jsonString);
        }
        return map;
    }
}
