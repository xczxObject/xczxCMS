package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.dao.SysDictionaryDao;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysdictionaryService {

    @Autowired
    SysDictionaryDao sysDictionaryDao;

    //根据字典分类type查询字典信息
    public SysDictionary finDictionaryByType(String type){
        return sysDictionaryDao.findBydType(type);
    }
}
