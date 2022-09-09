package com.temp.myself.music.entiy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName(value = "MUSIC")
public class MusicHistory implements Serializable {

    @TableId(value = "ID",type = IdType.AUTO)
    private Integer id;
    /**
     * 歌曲id
     */
    private String musicId;
    /**
     * 文件id
     */
    private String fileId;
    /**
     * 名字
     */
    private String name;
    /**
     * 作者
     */
    private String author;
    /**
     * 专辑id
     */
    private String album;
    /**
     * 来源
     */
    private String source;
    /**
     * 次数
     */
    private Integer num;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updateTime;
    /**
     * 备注
     */
    private String note;
    /**
     * 三个月收听次数
     */
    private Integer monthOne;
    private Integer monthTwo;
    private Integer monthThree;

    public MusicHistory() {
    }

    public MusicHistory(String musicId, String fileId, String name, String author, String album, String source, Integer num, LocalDateTime beginTime, LocalDateTime updateTime, String note) {
        this.musicId = musicId;
        this.fileId = fileId;
        this.name = name;
        this.author = author;
        this.album = album;
        this.source = source;
        this.num = num;
        this.beginTime = beginTime;
        this.updateTime = updateTime;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getMonthOne() {
        return monthOne;
    }

    public void setMonthOne(Integer monthOne) {
        this.monthOne = monthOne;
    }

    public Integer getMonthTwo() {
        return monthTwo;
    }

    public void setMonthTwo(Integer monthTwo) {
        this.monthTwo = monthTwo;
    }

    public Integer getMonthThree() {
        return monthThree;
    }

    public void setMonthThree(Integer monthThree) {
        this.monthThree = monthThree;
    }
}
