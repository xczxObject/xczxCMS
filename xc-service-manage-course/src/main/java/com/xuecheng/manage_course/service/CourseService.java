package com.xuecheng.manage_course.service;

import com.alibaba.druid.util.StringUtils;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
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

    //课程计划的查询
    TeachplanNode findTeachplanList(String courseId);
    //添加课程计划
    @Transactional
    ResponseResult addTeachplan(Teachplan teachplan);

    //查询课程的根节点，如果查询不到要自动添加根节点
    String getTeachplanRoot(String courseId);

    //课程列表查询分页
    QueryResponseResult<CourseInfo> findCourseListPage(int page, int size, CourseListRequest courseListRequest);

    //添加课程提交
    AddCourseResult addCourseBase(CourseBase courseBase);

    //根据id获取课程详细信息
    CourseBase getCoursebaseById(String courseId);

    //更新课程信息
    ResponseResult updateCoursebase(String id, CourseBase courseBase);

    ResponseResult addCoursePic(String courseId, String pic);

    CoursePic findCoursepic(String courseId);

    ResponseResult deleteCoursePic(String courseId);

    CourseView getCoruseView(String id);

    CoursePublishResult prview(String id);
}
