package com.temp.myself.music.service.impl;


import com.temp.myself.music.dao.MusicMapper;
import com.temp.myself.music.entiy.MusicHistory;
import com.temp.myself.music.entiy.dto.MusicAuhtorNumDTO;
import com.temp.myself.music.service.MusicSrevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MusicServiceImpl implements MusicSrevice {

    @Autowired
    public MusicMapper musicMapper;

    @Override
    public void insertMusic(MusicHistory musicHistory) throws Exception{
        //查询有没有播放历史
        MusicHistory musicHistory1 = musicMapper.selectOne(musicMapper.getQueryWrapper().eq(MusicHistory::getMusicId, musicHistory.getMusicId()));
        System.out.println("测试");
        if (musicHistory1 == null){
            if (musicHistory.getNote() == null || "".equals(musicHistory.getNote())){
                musicHistory.setNum(1);
                musicHistory.setBeginTime(LocalDateTime.now());
                musicHistory.setUpdateTime(LocalDateTime.now());
                musicHistory.setMonthOne(1);
                musicMapper.insert(musicHistory);
            }else{
                throw new Exception("收藏失败");
            }
        }else{
            if (musicHistory.getNote() == null || "".equals(musicHistory.getNote())){
                musicHistory1.setNum(musicHistory1.getNum() + 1);
            }
            musicHistory1.setUpdateTime(LocalDateTime.now());
            musicHistory1.setNote(musicHistory.getNote());
            musicHistory1.setMonthOne(musicHistory1.getMonthOne() +1);
            musicMapper.updateById(musicHistory1);
        }

    }

    @Override
    public Map getMusicNumMonth(String yuan) {
        Map map = new HashMap();
        Calendar cal = Calendar.getInstance();
        Integer month = cal.get(Calendar.MONTH) + 1;
        Integer month1 = 0;
        Integer month2 = 0;
        if (month == 2){
            month1 = 1;
            month2 = 12;
        }else if(month == 3){
            month1 = 12;
            month2 = 11;
        }else{
            month1 = month -1;
            month2 = month -2;
        }
        map.put(month,musicMapper.getMusicNumMonth(yuan,"1"));
        map.put(month1,musicMapper.getMusicNumMonth(yuan,"2"));
        map.put(month2,musicMapper.getMusicNumMonth(yuan,"3"));
        return map;
    }

    @Override
    public List<MusicHistory> getNewMusic() {
        return musicMapper.selectList(musicMapper.getQueryWrapper().orderByDesc(MusicHistory::getUpdateTime).last("limit 6"));
    }

    @Override
    public List<MusicAuhtorNumDTO> getMusicAuthorNum() {
        return musicMapper.getAuthorNum();
    }

    @Override
    public List getMusicNum() {
        List list = new ArrayList<>();
        Integer num = musicMapper.getMusicCountNum();
        Integer count = musicMapper.selectCount(musicMapper.getQueryWrapper().orderByDesc(MusicHistory::getNum));
        Integer count1 = musicMapper.getMusicCountMost();
        Double avg = musicMapper.getMusicArgNum();
        list.add(num);
        list.add(count);
        list.add(count1);
        list.add(avg);
        list.add(musicMapper.getMusicSouceNum("酷狗"));
        list.add(musicMapper.getMusicSouceNum("酷我音乐"));
        list.add(musicMapper.getMusicSouceNum("网易云音乐"));
        return list;
    }


}
