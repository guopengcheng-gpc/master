package com.temp.myself.until;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 获取外部url请求的值
 */
public class UrlValue {
    /**
     * 获取外部https的url的json返回值
     * @param path
     * @return
     */
    public static String getHttpsValue(String path){
        BufferedReader in;

        try {
            //创建SSLContext
            SSLContext sslContext= SSLContext.getInstance("SSL");
            TrustManager[] tm={new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            //URL url = new URL("https://complexsearch.kugou.com/v2/search/song?callback=callback123&keyword="+ name+"&page=1&pagesize=30&bitrate=0&isfuzzy=0&tag=em&inputtype=0&platform=WebFilter&userid=0&clientver=2000&iscorrection=1&privilege_filter=0&srcappid=2919&clienttime=1626761585839&mid=1626761585839&uuid=1626761585839&dfid=-&signature=D1FEE04154C779937F150645E9C197E1");
            URL url = new URL(path);

            //打开和url之间的连接
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            //请求方式
            //conn.setRequestMethod("POST");
//           //设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "utf-8");
            //设置当前实例使用的SSLSoctetFactory
            conn.setSSLSocketFactory(ssf);
            conn.connect();
            StringBuffer result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            String result2 =  result.toString();

            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            in.close();
            return result2;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取数据异常");
            return "";
        }
    }

    /**
     * 获取外部http的json返回值
     * @param path
     * @return
     */
    public static String getHttpValue(String path){
        BufferedReader in;
        try {
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //请求方式
            //conn.setRequestMethod("POST");
//           //设置通用的请求属性
            conn.setRequestProperty("Access-Control-Allow-Origin", "*");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "utf-8");

            conn.connect();
            StringBuffer result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取数据异常");
            return null;
        }

    }
}
