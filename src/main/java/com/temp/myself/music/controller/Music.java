package com.temp.myself.music.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.temp.myself.config.CodeCache;
import com.temp.myself.music.entiy.MusicHistory;
import com.temp.myself.music.service.MusicSrevice;
import com.temp.myself.until.IpUntil;
import com.temp.myself.until.MyX509TrustManager;
import com.temp.myself.until.UrlValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;


@RestController
public class Music {

    @Autowired
    private MusicSrevice musicSrevice;

    @RequestMapping("/music")
    public ModelAndView musicShow(HttpServletRequest httpServletRequest){
        String userAgent = httpServletRequest.getHeader("user-agent");
        String flag = httpServletRequest.getParameter("flag");
        ModelAndView model;
        List obj;
        List obj1;
        List obj2;
        List obj3;
        if (userAgent.indexOf("Android") != -1 || userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1) {//安卓和苹果
            model = new ModelAndView("musicApp.html");
            obj = getMusic("热门歌曲","20","1");
            obj1 = getQQMusic("热门歌曲","20","1");
            obj2 = getKuWoMusic("热门歌曲","20","1");
            obj3 = getWangYiMusic("热门歌曲","20","1");
        }else{//电脑
            model = new ModelAndView("music.html");
            obj = getMusic("热门歌曲","10","1");
            obj1 = getQQMusic("热门歌曲","10","1");
            obj2 = getKuWoMusic("热门歌曲","10","1");
            obj3 = getWangYiMusic("热门歌曲","10","1");
        }

        model.addObject("kuGou",obj);
        model.addObject("qqMusic",obj1);
        model.addObject("kuWo",obj2);
        model.addObject("wangYiYun",obj3);
        if ("1".equals(flag)){
            model.addObject("flag","1");
        }else{
            model.addObject("flag","0");
        }
        //获取ip地址
        String ipAddress = IpUntil.getIpAddr(httpServletRequest);

        System.out.println(ipAddress);
        Map map = IpUntil.getAddressByIp(ipAddress);
        System.out.println(map.toString());

        return model;
    }
    @RequestMapping("/musicShow")
    public ModelAndView musicShow1(){
        ModelAndView model = new ModelAndView("musicShow.html");
        return model;
    }

    @RequestMapping("/selectMusic")
    public Map selectMusic(@RequestParam(name="name",defaultValue = "")String name,@RequestParam(name = "pageSize",defaultValue = "10")String pageSize,@RequestParam(name = "page",defaultValue = "1")String page){
        Map map = new HashMap();
        List obj = null;
        List obj1 = null;
        List obj2 = null;
        List obj3 = null;
        try {
            if (name == null ||"".equals(name)){
                name = "热门歌曲";
            }
            obj = getMusic(name,pageSize,page);
            obj1 = getQQMusic(name,pageSize,page);
            if (obj1.size()>0){
                obj1 = obj1.subList(0,Integer.parseInt(pageSize));
            }
            if (name == null ||"".equals(name)){
                obj2 = getKuWoMusic("热门歌曲",pageSize,page);
            }else{
                obj2 = getKuWoMusic(name,pageSize,page);
            }
            obj3 = getWangYiMusic(name,pageSize,page);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查询失败");
        }
        map.put("kuGou",obj);
        map.put("qqMusic",obj1);
        map.put("kuWoMusic",obj2);
        map.put("wangYiMusic",obj3);
        return map;
    }

    /**
     * 获取酷我音乐播放的地址
     * @param id
     * @return
     */
    @RequestMapping("/getMusicXi")
    public Map getMusicXi(@RequestParam("id") String id,@RequestParam("fid") String fid,@RequestParam("name") String name){
        BufferedReader in;
        Map map = new HashMap();
        try {
            //String path = "http://antiserver.kuwo.cn/anti.s?type=convert_url&rid="+id+"&format=aac|mp3&response=url";
            String path = "";
            if("酷我音乐".equals(name)){
                path = "https://www.kuwo.cn/api/v1/www/music/playUrl?mid="+id.split("_")[1]+"&type=music&httpsStatus=1&reqId=7ac54a91-b0d7-11ec-8bea-1fc0d586b156";
            }else if ("酷狗".equals(name)){
                path = "http://wwwapi.kugou.com/yy/index.php?r=play/getdata&hash="+id+"&album_id="+fid+"&_=1497972864535";
            }else if ("qq音乐".equals(name)){

            }
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //请求方式
            //conn.setRequestMethod("POST");
//           //设置通用的请求属性
            conn.setRequestProperty("Access-Control-Allow-Origin", "*");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "utf-8");
            if ("酷狗".equals(name)){

                String val = "";
                Random random = new Random();
                //参数length，表示生成几位随机数
                for(int i = 0; i < 4; i++) {
                    String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
                    //输出字母还是数字
                    if ("char".equalsIgnoreCase(charOrNum)) {
                        //输出是大写字母还是小写字母
                        int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                        val += (char) (random.nextInt(26) + temp);
                    } else if ("num".equalsIgnoreCase(charOrNum)) {
                        val += String.valueOf(random.nextInt(10));
                    }
                }

                conn.setRequestProperty("Cookie", "kg_mid="+val);
            }
            conn.connect();
            StringBuffer result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result);
            if ("酷狗".equals(name)){
                JSONObject json = JSONArray.parseObject(result.toString());
                Object data = json.get("data");
                JSONObject json1 = JSONArray.parseObject(data.toString());
                String result1 = (String)json1.get("play_url");
                String result2 = (String)json1.get("lyrics");
                String[] list = result2.split("[\n]");
                map.put("url",result1);
                map.put("lyc",list);
                return map;
            }
            if ("酷我音乐".equals(name)){
                JSONObject json = JSONArray.parseObject(result.toString());
                Object data = json.get("data");
                if (data == null || "".equals(data)){
                    map.put("msg","歌曲为会员歌曲");
                    map.put("url","");
                }else{
                    JSONObject json1 = JSONArray.parseObject(data.toString());
                    String result1 = (String)json1.get("url");
                    map.put("url",result1);
                }
            }
            String[] list = getMusicLyc(id);
            map.put("lyc",list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 酷狗
     * @param name
     * @return
     */
    private List getMusic(String name,String pageSize,String page){
        BufferedReader in;//https://www.kugou.com/song/#hash=${ll.FileHash}&album_id=${ll.AlbumID}酷狗播放页面

        try {
            //创建SSLContext
            SSLContext sslContext= SSLContext.getInstance("SSL");
            TrustManager[] tm={new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            //URL url = new URL("https://complexsearch.kugou.com/v2/search/song?callback=callback123&keyword="+ name+"&page=1&pagesize=30&bitrate=0&isfuzzy=0&tag=em&inputtype=0&platform=WebFilter&userid=0&clientver=2000&iscorrection=1&privilege_filter=0&srcappid=2919&clienttime=1626761585839&mid=1626761585839&uuid=1626761585839&dfid=-&signature=D1FEE04154C779937F150645E9C197E1");
            URL url = new URL("https://songsearch.kugou.com/song_search_v2?keyword="+name+"&page="+page+"&pagesize="+pageSize+"&userid=-1&clientver=&platform=WebFilter&tag=em&filter=2&iscorrection=1&privilege_filter=0");

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
            String result2 =  result.toString(); //返回json字符串
            //获取数据
            JSONObject json = JSONArray.parseObject(result2);
            //JSONObject jsonObject = JSON.parseObject(result2);
            //System.out.println(result2.substring(12,result2.length()-1));
            Object map = json.get("data");
            JSONObject json1 = JSONArray.parseObject(map.toString());
            List list = (List)json1.get("lists");
            System.out.println(list);

            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            in.close();
            list.forEach(item ->{
                ((JSONObject)item).put("SingerName",((String)((JSONObject)item).get("SingerName")).replace("<em>","").replace("</em>",""));
                ((JSONObject)item).put("SongName",((String)((JSONObject)item).get("SongName")).replace("<em>","").replace("</em>",""));
            });
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取数据异常");
            return null;
        }

    }

    /**
     * qq音乐
     * @param name
     * @return
     */
    public List getQQMusic(String name,String pageSize,String page){
        BufferedReader in;

        try {
            //创建SSLContext
            SSLContext sslContext= SSLContext.getInstance("SSL");
            TrustManager[] tm={new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            URL url = new URL("https://c.y.qq.com/soso/fcgi-bin/client_search_cp?aggr=1&cr=1&flag_qc=0&p="+page+"&n="+pageSize+"&w=" + name);
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
            String result2 =  result.toString(); //返回json字符串
            //获取数据
            JSONObject json = JSONArray.parseObject(result2.substring(9,result2.length()-1));
            //JSONObject jsonObject = JSON.parseObject(result2);
            System.out.println(result2.substring(12,result2.length()-1));
            Object map = json.get("data");
            JSONObject json1 = JSONArray.parseObject(map.toString());
            Object map1 = json1.get("song");
            JSONObject json2 = JSONArray.parseObject(map1.toString());
            List list = (List)json2.get("list");
            System.out.println(list);

            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            in.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取数据异常");
            return new ArrayList();
        }

    }

    /**
     * 酷我
     * @param name
     * @return
     */
    public List getKuWoMusic(String name,String pageSize,String page){
        BufferedReader in;

        try {
            String path = "https://kuwo.cn/";
            String reponse = UrlValue.getHttpsValue(path);
            //reponse = reponse.split("[+]")[0].split(";")[0].split("=")[1];
            //URL url = new URL("http://search.kuwo.cn/r.s?all="+name+"&ft=music&%20itemset=web_2013&client=kt&pn="+page+"&rn="+pageSize+"&rformat=json&encoding=utf8");
            URL url = new URL("http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key="+ URLEncoder.encode(name)+"&pn="+page+"&rn="+pageSize+"&httpsStatus=1&reqId=9ca04130-b0da-11ec-8ef6-f53497f5204b");
            //URL url = new URL("http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key="+name+"&pn=1&rn="+pageSize);

            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //请求方式
            conn.setRequestMethod("GET");
//           //设置通用的请求属性
            conn.setRequestProperty("Content-Type", "text/xml");
            conn.setRequestProperty("Charset", "utf-8");

            conn.setRequestProperty("Referer", "http://www.kuwo.cn/singer_detail/47");
            conn.setRequestProperty("csrf", "D8U8SQB54L5");
            conn.setRequestProperty("Cookie", "_ga=GA1.2.1053133548.1642407480; Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1648542654; _gid=GA1.2.702494058.1648866045; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1648866324; kw_token=D8U8SQB54L5");
            conn.connect();
            StringBuffer result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            String result2 =  result.toString(); //返回json字符串
            //获取数据
            System.out.println(result2);
            JSONObject json = JSONArray.parseObject(result2);
            //JSONObject jsonObject = JSON.parseObject(result2);
            //System.out.println(result2.substring(12,result2.length()-1));
            JSONObject json1 = (JSONObject)json.get("data");
            List list = (List)json1.get("list");
            System.out.println(list);

            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            in.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取数据异常");
            return null;
        }

    }

    /**
     * 网易云
     * @param name
     * @return
     */
    public List getWangYiMusic(String name,String pageSize,String page){
        BufferedReader in;

        try {
            //java.net.URLEncoder.encode(name,"utf-8")将name转为URLEncode
            page = (Integer.parseInt(pageSize) * (Integer.parseInt(page) - 1) + 1) + "";
            URL url = new URL("http://music.163.com/api/search/get/web?csrf_token=hlpretag=&hlposttag=&s="+java.net.URLEncoder.encode(name,"utf-8")+"&type=1&offset="+java.net.URLEncoder.encode(page,"utf-8")+"&total=true&limit="+java.net.URLEncoder.encode(pageSize,"utf-8")+"");
            //URL url = new URL("https://api.imjad.cn/cloudmusic/?type=search&search_type=1&s=" + name);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //请求方式
            //conn.setRequestMethod("GET");
//           //设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.connect();
            StringBuffer result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

            String result2 =  result.toString(); //返回json字符串
            //获取数据
            System.out.println(result2);
            JSONObject json = JSONArray.parseObject(result2);
            //JSONObject jsonObject = JSON.parseObject(result2);
            //System.out.println(result2.substring(12,result2.length()-1));
            Object obj = json.get("result");
            JSONObject json1 = JSONArray.parseObject(obj.toString());
            List list = (List)json1.get("songs");
            System.out.println(list);

            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            in.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取数据异常");
            return null;
        }

    }

    private String[] getMusicLyc(String id){
        BufferedReader in;
        id = id.split("_")[1];
        try {
            //java.net.URLEncoder.encode(name,"utf-8")将name转为URLEncode
            URL url = new URL("http://m.kuwo.cn/newh5/singles/songinfoandlrc?musicId=" + id);
            //URL url = new URL("https://api.imjad.cn/cloudmusic/?type=search&search_type=1&s=" + name);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //请求方式
            //conn.setRequestMethod("GET");
//           //设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.connect();
            StringBuffer result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

            String result2 =  result.toString(); //返回json字符串
            //获取数据
            System.out.println(result2);
            JSONObject json = JSONArray.parseObject(result2);
            Object obj = null;
            try {
                obj = json.get("data");
            } catch (Exception e) {
                System.out.println(json.toString());
            }
            JSONObject json1 = JSONArray.parseObject(obj.toString());
            List list = (List)json1.get("lrclist");
            List<String> list1 = new ArrayList();
            for (int i=0;i<list.size();i++){
                JSONObject json2 = JSONArray.parseObject(list.get(i).toString());
                String lyc = "[00:" +json2.get("time")+ "]" +json2.get("lineLyric");
                list1.add(lyc);
            }
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            in.close();
            String[] lists = new String[list1.size()];
            list1.toArray(lists);
            return lists;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取数据异常");
            return null;
        }
    }

    /**
     * 获取网易云音乐歌词
     * @param id
     * @return
     */
    @RequestMapping("/getWangYiMusicLyc")
    public Map getWangYiMusicLyc(String id){
        Map map = new HashMap();
        BufferedReader in;
        try {
            //创建SSLContext
            SSLContext sslContext= SSLContext.getInstance("SSL");
            TrustManager[] tm={new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            URL url = new URL("https://api.imjad.cn/cloudmusic/?type=lyric&id=" + id);
            //打开和url之间的连接
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            //请求方式
            //conn.setRequestMethod("GET");
//           //设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
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

            String result2 =  result.toString(); //返回json字符串
            //获取数据

            JSONObject json = JSONArray.parseObject(result2);
            Object obj = json.get("lrc");
            JSONObject json1 = JSONArray.parseObject(obj.toString());
            Object obj1 = json1.get("lyric");
            String[] list = obj1.toString().split("[\n]");
            map.put("lyc",list);
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取数据异常");
            map.put("lyc",null);
        }
        return map;
    }

    /**
     * 保存播放历史
     * @param musicHistory
     * @return
     */
    @RequestMapping(value = "/saveMusic")
    public Map saveMusicHistory(@RequestBody MusicHistory musicHistory){
        Map map = new HashMap();
        try {
            musicSrevice.insertMusic(musicHistory);
            if (musicHistory.getNote() == null || "".equals(musicHistory.getNote())){
                map.put("msg","保存成功");
            }else{
                map.put("msg","收藏成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (musicHistory.getNote() == null || "".equals(musicHistory.getNote())){
                map.put("msg","保存失败");
            }else{
                map.put("msg","收藏失败");
            }
        }
        return map;
    }

    public String musicTui(){
        BufferedReader in;
        try {
            //创建SSLContext
            SSLContext sslContext= SSLContext.getInstance("SSL");
            TrustManager[] tm={new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            URL url = new URL("http://mobilecdnbj.kugou.com/api/v5/special/recommend?recommend_expire=0&sign=52186982747e1404d426fa3f2a1e8ee4&plat=0&uid=0&version=9108&page=1&area_code=1&appid=1005&mid=286974383886022203545511837994020015101&_t=1545746286");
            //打开和url之间的连接
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //请求方式
            conn.setRequestMethod("POST");
//           //设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            //设置当前实例使用的SSLSoctetFactory
            conn.setSSLSocketFactory(ssf);
            conn.connect();
            PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
            printWriter.print("ip="+ "");
            StringBuffer result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            String result2 =  result.toString(); //返回json字符串
            //获取数据

            JSONObject json = JSONArray.parseObject(result2);
            Object obj = json.get("lrc");
            JSONObject json1 = JSONArray.parseObject(obj.toString());
            Object obj1 = json1.get("lyric");
            String[] list = obj1.toString().split("[\n]");

            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取数据异常");
        }
          return "";
    }

    /**
     * 将时间戳转为时间格式
     * @param even
     * @param ms
     * @return
     */
    public static String s(String even,int ms){
        if (even == null || "".equals(even)){
            even = "0";
        }
        int fen = 0;
        int miao = 0;
        if (ms == 1){
            even = Integer.parseInt(even)/1000 + "";
        }
        if (Integer.parseInt(even)>60){
            fen = Integer.parseInt(even)/60;
            miao = Integer.parseInt(even)%60;
        }else{
            miao = Integer.parseInt(even);
        }
        return fen+":"+miao;
    }

    @RequestMapping("/musicNotes")
    public ModelAndView musicNotes(){
        ModelAndView model = new ModelAndView("musicNotes.html");
        //前三个月的个来源的次数
        /*Map map = new HashMap();
        map.put("酷狗",musicSrevice.getMusicNumMonth("酷狗"));
        map.put("网易云音乐",musicSrevice.getMusicNumMonth("网易云音乐"));
        map.put("酷我音乐",musicSrevice.getMusicNumMonth("酷我音乐"));*/
        model.addObject("threeMusicNum",JSONArray.toJSON(CodeCache.musicMap.get("threeMusicNum")));
        model.addObject("monthMusic",JSONArray.toJSON(CodeCache.musicMap.get("monthMusic")));
        model.addObject("musicNew",CodeCache.musicMap.get("musicNew"));
        Object authorNum = JSONArray.toJSON(CodeCache.musicMap.get("musicAuthorNum"));
        model.addObject("musicAuthorNum",authorNum);
        model.addObject("musicCount",CodeCache.musicMap.get("musicCount"));
        return model;
    }

    /**
     * 音乐统计
     * @return
     */
    @RequestMapping("/musicNotesAll")
    public Map musicNotesAll(){
        Map map = new HashMap();
        map.put("threeMusicNum",CodeCache.musicMap.get("threeMusicNum"));
        map.put("monthMusic",JSONArray.toJSON(CodeCache.musicMap.get("monthMusic")));
        map.put("musicNew",CodeCache.musicMap.get("musicNew"));
        map.put("musicAuthorNum",CodeCache.musicMap.get("musicAuthorNum"));
        map.put("musicCount",CodeCache.musicMap.get("musicCount"));
        return map;
    }

    /**
     * 将http转为音频流播放   修改中不可用
     * @param reponse
     * @param id
     * @return
     */
    @RequestMapping("/kuwoMusic/{id}")
    public String musicKuWozhuan(HttpServletResponse reponse,HttpServletRequest request,@PathVariable String id){

        try {
            URL url = new URL(UrlValue.getHttpValue("http://antiserver.kuwo.cn/anti.s?type=convert_url&rid="+id+"&format=aac|mp3&response=url"));
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //请求方式
            //conn.setRequestMethod("POST");
//           //设置通用的请求属性
            conn.setRequestProperty("Access-Control-Allow-Origin", "*");
            conn.setRequestProperty("Content-Type", "application/octet-stream;charset=UTF-8");//audio/mpeg
            conn.setRequestProperty("Charset", "utf-8");

            conn.connect();
            //读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(reponse.getOutputStream()));
            String range = request.getHeader("Range");
            reponse.setContentType("application/octet-stream");
            reponse.setContentLength(url.getFile().length());
            String line;
            while ((line = in.readLine()) != null) {
                bufferedWriter.write(line);
            }

            in.close();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
