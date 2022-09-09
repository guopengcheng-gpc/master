package com.temp.myself.until;

import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PictureSplit {

    @Value("${web.uploadPath}")
    private String uploadPath;

    public String getUploadPath() {
        return uploadPath;
    }

    /**
     * 有图片放置为位置，位置修改后需要修改
     * @param path
     * @param name
     */
    public static void splitPicture(String path, String name){
        String picture = path;
        File file = new File(picture);
        PictureSplit pictureSplit = new PictureSplit();
        String pathUrl = pictureSplit.getUploadPath();
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            int rows = 4;
            int cols = 4;
            int chunks = 16;

            int chunkWidth = bufferedImage.getWidth()/cols;
            int chunkHeight = bufferedImage.getHeight()/rows;

            int count = 0;

            BufferedImage[] images = new BufferedImage[chunks];
            for (int i=0;i<rows;i++){
                for (int j=0;j<cols;j++){
                    images[count] = new BufferedImage(chunkWidth,chunkHeight,bufferedImage.getType());
                    Graphics2D gr = images[count++].createGraphics();
                    gr.drawImage(bufferedImage,0,0,chunkWidth,chunkHeight,chunkWidth*j,chunkHeight*i,chunkWidth*j+chunkWidth,chunkHeight*i+chunkHeight,null);
                    gr.dispose();
                }
            }

            for (int i=0;i<images.length;i++){
                ImageIO.write(images[i],"jpg",new File("/root/static/pic/"+name+i+".jpg"));//linux为/root/static/pic/，windos为D://pic/
            }
            System.out.println("分割完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
