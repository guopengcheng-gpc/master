package com.temp.myself.until;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BaseDao<T> extends BaseMapper<T> {

    default LambdaQueryWrapper<T> getQueryWrapper(){
        LambdaQueryWrapper<T> lambdaQueryWrapper = new LambdaQueryWrapper();
        return lambdaQueryWrapper;
    }

}
