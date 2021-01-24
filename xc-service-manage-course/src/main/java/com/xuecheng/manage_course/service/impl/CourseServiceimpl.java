package com.xuecheng.manage_course.service.impl;

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
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceimpl implements CourseService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    //课程计划的查询
    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    }

    //添加课程计划
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if(teachplan==null|| StringUtils.isEmpty(teachplan.getCourseid())||StringUtils.isEmpty(teachplan.getPname()))
        {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程处理
        String courseId=teachplan.getCourseid();
        //页面传入的parentId
        String parentId=teachplan.getParentid();
        if(StringUtils.isEmpty(parentId)){
            //取得该课程的跟节点
            parentId=this.getTeachplanRoot(courseId);
        }
        Optional<Teachplan> optional = teachplanRepository.findById(parentId);
        Teachplan parentNode=optional.get();
        //父节点的级别
        String grade=parentNode.getGrade();
        //新节点
        Teachplan teachplan1=new Teachplan();
        //将页面提交的teachplan信息拷贝到teachplannew对象中
        BeanUtils.copyProperties(teachplan,teachplan1);
        teachplan1.setParentid(parentId);
        teachplan1.setCourseid(courseId);
        if(grade.equals("1"))
        {
            teachplan1.setGrade("2");//级别,根据父节点的级别来设置
        }else{
            teachplan1.setGrade("3");
        }
        teachplanRepository.save(teachplan1);
        //要处理parentId
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询课程的根节点，如果查询不到要自动添加根节点
    public String getTeachplanRoot(String courseId){
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        //调用dao查询teachplan表得到该课程的根结点（一级结点）
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if(teachplanList == null || teachplanList.size()<=0){
            //新添加一个课程的根结点
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseId);
            teachplan.setParentid("0");
            teachplan.setGrade("1");//一级结点
            teachplan.setStatus("0");
            teachplan.setPname(courseBase.getName());
            teachplanRepository.save(teachplan);
            return teachplan.getId();

        }
        //返回根结点的id
        return teachplanList.get(0).getId();

    }
}
