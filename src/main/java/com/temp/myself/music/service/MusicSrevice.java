package com.temp.myself.music.service;

import com.temp.myself.music.entiy.MusicHistory;
import com.temp.myself.music.entiy.dto.MusicAuhtorNumDTO;

import java.util.List;
import java.util.Map;

public interface MusicSrevice {

    void insertMusic(MusicHistory musicHistory) throws Exception;

    /**
     * 根据来源，月份查听歌条数，  yue为1为当月，2为上一个月，3为上上个月
     * @param yuan
     * @return
     */
    Map getMusicNumMonth(String yuan);

    /**
     * 获取最新六条数据
     * @return
     */
    List<MusicHistory> getNewMusic();

    /**
     * 获取收听次数最多的五位歌手
     * @return
     */
    List<MusicAuhtorNumDTO> getMusicAuthorNum();

    /**
     * 获取统计次数
     * @return
     */
    List getMusicNum();
}
