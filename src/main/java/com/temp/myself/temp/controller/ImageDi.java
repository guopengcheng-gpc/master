package com.temp.myself.temp.controller;

import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ImageDi {

    @RequestMapping("/imageDi")
    public ModelAndView imageDiShow(){
        ModelAndView modelAndView = new ModelAndView("imageDi.html");
        return modelAndView;
    }

    /**
     * 预览转换后图片
     * @param file
     */
    @RequestMapping("/lookImage")
    public Map lookImage(@RequestParam("file") MultipartFile file, @RequestParam("oldColor")String oldColor, @RequestParam(value = "newColor")String newColor){

        File fileNew = changeImage(file,oldColor,newColor);
        Map map = new HashMap();
        try {
            FileInputStream fileInputStream = new FileInputStream(fileNew);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            map.put("file", Base64Utils.encodeToString(buffer));
            map.put("filename",file.getOriginalFilename());
            fileNew.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 图片转换颜色
     * @param oldFile
     * @param oldColor
     * @param newColor
     * @return
     */
    public File changeImage(MultipartFile oldFile,String oldColor,String newColor){
        if ("red".equals(newColor)){
            newColor = "255,0,0";
        }else if ("blue".equals(newColor)){
            newColor = "67,142,219";
        }else if ("white".equals(newColor)){
            newColor = "255,255,255";
        }

        //待处理的图片
        File file = new File(oldFile.getOriginalFilename());
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            byte[] zj = oldFile.getBytes();
            for(int i=0;i<zj.length;i++){
                outputStream.write(zj[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        int[] rgb = new int[3];
        String[] oldColorRgb = oldColor.split(",");

        BufferedImage bufferedImage = null;
        File newFile = null;

        try {
            bufferedImage = ImageIO.read(file);

            int width = bufferedImage.getWidth();
            int hight = bufferedImage.getHeight();
            int minx = bufferedImage.getMinTileX();
            int miny = bufferedImage.getMinTileY();
            System.out.println("正在处理--"+file.getName());
            for(int i = minx;i<width ;i++){
                for(int j = miny;j<hight;j++){
                    //换色
                    int pixel = bufferedImage.getRGB(i,j);
                    rgb[0] = (pixel & 0xff0000) >> 16;
                    rgb[1] = (pixel & 0xff00) >> 8;
                    rgb[2] = (pixel & 0xff);
                    //红换蓝   判断是否为需要替换的rgb值
                    /*if(rgb[0]<150 && rgb[0]>100 && rgb[1]<50 && rgb[2]<50){
                        bufferedImage.setRGB(i,j,0xFFB6C1);
                    }*/
                    Color color = new Color(Integer.parseInt(newColor.split(",")[0]),Integer.parseInt(newColor.split(",")[1]),Integer.parseInt(newColor.split(",")[2]));
                    if ("red".equals(oldColor)){
                        if(rgb[0]>170  && rgb[1]<75 && rgb[2]<75){
                            bufferedImage.setRGB(i,j,color.getRGB());
                        }
                    }else if("blue".equals(oldColor)){
                        if(rgb[0]<80 && rgb[0]>30 && rgb[1]>105 && rgb[1]<150 && rgb[2]>160 && rgb[2]<225){
                            bufferedImage.setRGB(i,j,color.getRGB());
                        }
                    }else if ("white".equals(oldColor)){
                        if(rgb[0]>175  && rgb[1]>185 && rgb[2]>210){
                            bufferedImage.setRGB(i,j,color.getRGB());
                        }
                    }else{
                        if(rgb[0] == Integer.parseInt(oldColorRgb[0]) && rgb[1] == Integer.parseInt(oldColorRgb[1]) && rgb[2] == Integer.parseInt(oldColorRgb[2])){
                            bufferedImage.setRGB(i,j,color.getRGB());
                        }
                    }
                }
            }
            System.out.println("处理结束--"+file.getName());
            file.delete();
            FileOutputStream fileOutputStream;

            try {
                newFile = File.createTempFile("转换后",file.getName().split("\\.")[1]);
                fileOutputStream = new FileOutputStream(newFile);
                ImageIO.write(bufferedImage,file.getName().split("\\.")[1],fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }
}
