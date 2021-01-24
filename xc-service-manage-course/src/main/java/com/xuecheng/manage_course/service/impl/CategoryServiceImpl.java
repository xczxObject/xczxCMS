package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    CategoryMapper categoryMapper;
    @Override
    public CategoryNode findList() {
        return categoryMapper.selectList();
    }
}
