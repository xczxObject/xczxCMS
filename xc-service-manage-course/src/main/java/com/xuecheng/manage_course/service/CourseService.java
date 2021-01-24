package com.xuecheng.manage_course.service;

import com.alibaba.druid.util.StringUtils;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface CourseService {

    //查询课程的根节点，如果查询不到要自动添加根节点
    public String getTeachplanRoot(String courseId);

    //课程计划的查询
    public TeachplanNode findTeachplanList(String courseId);

    //添加课程计划
    public ResponseResult addTeachplan(Teachplan teachplan);

}
