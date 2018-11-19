package com.jianguo.course.dubbo.service.dubbo.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jianguo.course.dubbo.service.domain.Course;
import com.jianguo.course.dubbo.service.mapper.CourseMapper;
import com.jianguo.course.dubbo.service.thirft.UserServiceProvider;
import com.jianguo.thrift.user.TeacherDTO;
import com.jianguo.thrift.user.UserService;
import dto.CourseDTO;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements service.CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private UserServiceProvider userServiceProvider;

    @Override
    public List<CourseDTO> listCourseByCourseId(int courseId) {
        List<CourseDTO> courseDTOList = new ArrayList<>();
        List<Course> courseList = courseMapper.listCourseById(courseId);

        UserService.Client client = userServiceProvider.userServiceClient();
        for (Course course : courseList) {
            TeacherDTO teacherDTO = null;
            try {
                teacherDTO = client.getTeacherById(course.getUserId());
            } catch (TException e) {
                e.printStackTrace();
            }
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(course, courseDTO);
            if (teacherDTO != null) {
                courseDTO.setTeacherDTO(teacherDTO);
            }
            courseDTOList.add(courseDTO);
        }
        return courseDTOList;
    }
}
