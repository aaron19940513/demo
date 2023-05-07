package com.demo.service;

import com.demo.DBEnum;
import com.demo.mapper.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoService {


    @Autowired
    private DemoMapper demoMapper;

    public void test() {
        demoMapper.selectAllCoffee(DBEnum.DB1, "a", "b");
    }

}
