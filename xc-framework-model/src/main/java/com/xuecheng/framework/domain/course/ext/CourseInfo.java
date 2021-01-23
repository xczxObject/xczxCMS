package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 2018/2/10.
 */
@Data
@ToString
public class CourseInfo extends CourseBase {

    //课程图片
    private String pic;
    //课程名字
    private String name;
    //课程id
    private String  id;

}
