package service;

import dto.CourseDTO;
import java.util.List;

public interface CourseService {
    List<CourseDTO> listCourseByCourseId(int courseId);
}
