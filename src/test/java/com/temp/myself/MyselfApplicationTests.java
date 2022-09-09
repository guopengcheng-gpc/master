package com.temp.myself;


import com.temp.myself.music.service.MusicSrevice;
import com.temp.myself.temp.entiy.Picture;
import com.temp.myself.until.JSONUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyselfApplicationTests {

    @Autowired
    private MusicSrevice musicSrevice;

    @Test
    void contextLoads() {
        /*Music music = new Music();*/
        /*music.getWangYiMusicLyc("111");*/
        /*Integer ci = musicSrevice.getMusicNumMonth("酷狗","2");
        System.out.println(ci);*/
    }

}
