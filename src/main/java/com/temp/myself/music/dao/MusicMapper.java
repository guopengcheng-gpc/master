package com.temp.myself.music.dao;

import com.temp.myself.music.entiy.MusicHistory;
import com.temp.myself.music.entiy.dto.MusicAuhtorNumDTO;
import com.temp.myself.until.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicMapper extends BaseDao<MusicHistory> {

    Integer getMusicNumMonth(@Param("yuan") String yuan,@Param("yue") String yue);//yue为1为当月，2为上一个月，3为上上个月

    List<MusicAuhtorNumDTO> getAuthorNum();//收听次数最多的五位歌手

    Integer getMusicCountNum();//总次数

    Integer getMusicCountMost();//最多次数

    Double getMusicArgNum();//平均次数

    Integer getMusicSouceNum(String yuan);//来源次数

    void updateMusicMonth();//更新每月次数
}
