package com.temp.myself.temp.service;

import com.temp.myself.temp.entiy.Picture;

import java.util.List;

public interface PictureService {
    List<Picture> getPictureList(String name);

    void savePicture(Picture picture);

    void deletePicture(String name);
}
