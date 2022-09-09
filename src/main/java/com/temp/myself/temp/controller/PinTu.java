package com.temp.myself.temp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.temp.myself.temp.entiy.Picture;
import com.temp.myself.temp.service.PictureService;
import com.temp.myself.until.JSONUtils;
import com.temp.myself.until.PictureSplit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PinTu {

    @Autowired
    private PictureService pictureService;
    @Value("${picturePath}")
    private String urlPath;

    @Value("${web.uploadPath}")
    private String uploadPath;

    @RequestMapping("/pintu/{name}")
    public ModelAndView imageDiShow(@PathVariable String name, HttpServletRequest httpServletRequest){
        String userAgent = httpServletRequest.getHeader("user-agent");
        ModelAndView modelAndView = new ModelAndView("pintu/pintu.html");
        modelAndView.addObject("name",name);
        modelAndView.addObject("urlPath",urlPath);
        List<Picture> list = pictureService.getPictureList(name);
        List bi = new ArrayList();
        for (Picture picture :list){
            bi.add(Float.parseFloat(picture.getScale()));

        }
        modelAndView.addObject("biLi", JSON.toJSONString(bi));
        int sum = list.size();
        modelAndView.addObject("sum",sum);
        if (userAgent.indexOf("Android") != -1 || userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1) {//安卓和苹果
            modelAndView.addObject("device","phone");
        }else{
            modelAndView.addObject("device","computer");
        }
        return modelAndView;
    }

    /**
     * 保存图片
     * @param pic
     * @param name
     * @return
     */
    @RequestMapping("/savePic")
    public Map savePicture(@RequestParam(name = "pic",defaultValue = "")String pic,@RequestParam(name = "name")String name){
        Map map = new HashMap();
        String[] pics = new String[0];
        if (!"".equals(pic)){
            pic = pic.substring(0,pic.length()-1);
            pics = pic.split(",");
        }
        List bi = new ArrayList();
        pictureService.deletePicture(name);
        for (int i=0;i<pics.length;i++){
            try {
                String path = uploadPath+"pic/"+i+name+".jpg";//+"pic/"
                File file1 = new File(path);
                if (!file1.exists()) {
                    file1.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file1);
                if (pics[i].contains(urlPath)){
                    URL url = new URL(pics[i]);
                    HttpURLConnection coon = (HttpURLConnection)url.openConnection();
                    coon.setConnectTimeout(5000);
                    coon.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    InputStream inputStream = coon.getInputStream();

                    byte[] b = new byte[1024];
                    int len = 0;
                    while ((len =inputStream.read(b)) != -1){
                        fileOutputStream.write(b,0,len);
                    }
                    inputStream.close();

                    fileOutputStream.flush();
                    fileOutputStream.close();
                }else{
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] bytes = decoder.decodeBuffer(pics[i]);
                    for (int j=0;j<bytes.length;j++){
                        if (bytes[j] < 0){//调整异常数据
                            bytes[j] += 256;
                        }
                    }
                    fileOutputStream.write(bytes);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                PictureSplit.splitPicture(path,i+name);
                BufferedImage image = ImageIO.read(file1);
                String scale = new BigDecimal(image.getHeight()).divide(new BigDecimal(image.getWidth()),2,BigDecimal.ROUND_UP).toString();
                Picture picture = new Picture();
                picture.setName(i+name);
                picture.setPath(path);
                picture.setScale(scale);
                bi.add(Float.parseFloat(scale));
                pictureService.savePicture(picture);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("msg","保存失败");
            }
        }
        map.put("biLi", JSON.toJSONString(bi));
        map.put("sum", pics.length);
        map.put("msg","保存成功");
        return map;
    }
}
