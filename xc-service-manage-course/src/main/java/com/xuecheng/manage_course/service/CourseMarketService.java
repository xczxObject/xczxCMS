package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.manage_course.dao.CourseMarketRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class CourseMarketService {
    @Resource
    private CourseMarketRepository courseMarketRepository;

    /**
     * 根据id查询courseMarket对象
     * @param courseMarketId
     * @return
     */
    public CourseMarket getCourseMarketById(String courseMarketId) {
        Optional<CourseMarket> optionalCourseMarket = courseMarketRepository.findById(courseMarketId);
        if (optionalCourseMarket.isPresent()) {
            return optionalCourseMarket.get();
        }
        return null;
    }

    @Transactional
    public CourseMarket updateCourseMarket(String id,CourseMarket courseMarket){
        CourseMarket one=this.getCourseMarketById(id);
        if(one!=null)
        {
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());//课程有效期，开始时间
            one.setEndTime(courseMarket.getEndTime());//课程有效期，结束时间
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
            courseMarketRepository.save(one);
        }else {
            //添加课程营销信息
            one=new CourseMarket();
            BeanUtils.copyProperties(courseMarket,one);
            //设置课程id
            one.setId(id);
            courseMarketRepository.save(one);
        }
        return one;
    }
}
