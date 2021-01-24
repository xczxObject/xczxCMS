package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.manage_course.dao.CourseMarketRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;


public interface CourseMarketService {

    //根据id查询courseMarket对象
    public CourseMarket getCourseMarketById(String courseMarketId);

    public CourseMarket updateCourseMarket(String id,CourseMarket courseMarket);
}
