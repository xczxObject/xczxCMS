package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDictionaryDao extends MongoRepository<SysDictionary,String> {
    //根据字典分类查询信息
    SysDictionary findBydType(String dType);
}
