package com.temp.myself.temp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.temp.myself.temp.dao.PersonMapper;
import com.temp.myself.temp.entiy.Person;
import com.temp.myself.temp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    public PersonMapper personMapper;

    @Override
    public List<Person> getPersonList() {
        return personMapper.selectList(new LambdaQueryWrapper<>());
    }
}
