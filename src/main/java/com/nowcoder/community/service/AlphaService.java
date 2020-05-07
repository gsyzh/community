package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author gsyzh
 * @create 2020-05-03 20:10
 */
@Service
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("实例化");
    }

    public String find() {
        return alphaDao.select();
    }

    @PostConstruct
    public void init() {
        System.out.println("初始化");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("销毁");
    }
}
