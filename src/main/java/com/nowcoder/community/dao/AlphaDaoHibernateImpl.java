package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author gsyzh
 * @create 2020-05-03 18:40
 */
@Repository("alphaDaoHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
