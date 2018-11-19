package com.jianguo.course.dubbo.service.mapper;
import com.jianguo.course.dubbo.service.domain.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Select("select id,title ,description,user_id as userId from pe_course where id=#{id}")
    List<Course> listCourseById(@Param("id") int id);
}
