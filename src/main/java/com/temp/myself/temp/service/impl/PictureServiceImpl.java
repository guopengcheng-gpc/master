package com.temp.myself.temp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.temp.myself.temp.dao.PictureMapper;
import com.temp.myself.temp.entiy.Picture;
import com.temp.myself.temp.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {
    @Autowired
    public PictureMapper pictureMapper;

    @Override
    public List<Picture> getPictureList(String name) {
        return pictureMapper.selectList(new LambdaQueryWrapper<Picture>().like(Picture::getName,name));
    }

    @Override
    public void savePicture(Picture picture) {
        pictureMapper.insert(picture);
    }

    @Override
    public void deletePicture(String name) {
        pictureMapper.delete(new LambdaQueryWrapper<Picture>().like(Picture::getName,name));
    }
}
