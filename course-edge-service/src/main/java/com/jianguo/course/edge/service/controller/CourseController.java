package com.jianguo.course.edge.service.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import dto.CourseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CourseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Reference
    private CourseService courseService;

    @GetMapping("/get/{courseId}")
    public List<CourseDTO> getCourseList(HttpServletRequest request,
                                         @PathVariable("courseId") Integer courseId) {
        return courseService.listCourseByCourseId(courseId);
    }
}
