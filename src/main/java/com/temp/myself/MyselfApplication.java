package com.temp.myself;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan(value = "com.temp.myself.*.dao")
@ServletComponentScan
public class MyselfApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyselfApplication.class, args);
    }
}
