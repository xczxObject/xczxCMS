package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

public interface CourseControllerApi {
    QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);
}
