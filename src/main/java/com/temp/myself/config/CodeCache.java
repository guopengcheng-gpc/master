package com.temp.myself.config;

import com.temp.myself.music.dao.MusicMapper;
import com.temp.myself.music.service.MusicSrevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class CodeCache {
    public static Map musicMap = new HashMap();

    @Autowired
    private MusicSrevice musicSrevice;

    @Autowired
    public MusicMapper musicMapper;



    @PostConstruct
    public void init() {
        //系统启动中。。。加载codeMap
        //音乐统计加入缓存
        Map map = new HashMap();
        map.put("酷狗",musicSrevice.getMusicNumMonth("酷狗"));
        map.put("网易云音乐",musicSrevice.getMusicNumMonth("网易云音乐"));
        map.put("酷我音乐",musicSrevice.getMusicNumMonth("酷我音乐"));
        musicMap.put("threeMusicNum",map);
        musicMap.put("monthMusic",musicSrevice.getMusicNumMonth(""));
        musicMap.put("musicNew",musicSrevice.getNewMusic());
        musicMap.put("musicAuthorNum",musicSrevice.getMusicAuthorNum());
        musicMap.put("musicCount",musicSrevice.getMusicNum());

    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
    }

    //@Scheduled(cron = "0 0/2 * * * ?")
    @Async //这里的@Async注解很关键
    @Scheduled(fixedDelay = 120000)  //间隔2分钟
    public void testOne() {
        //每2分钟执行一次缓存
        init();
    }

    @Async //这里的@Async注解很关键
    @Scheduled(cron = "0 0 1 1 * ?")  //每月1日1点执行更新每月数据
    public void updateMusicMonth() {
        musicMapper.updateMusicMonth();
        System.out.println("每月次数更新成功");
    }

}
