package com.jianguo.course.edge.service.filter;

import com.jianguo.thrift.user.UserDTO;
import filter.LoginFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CourseLoginFilter extends LoginFilter {
    @Override
    protected void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO) {
        request.setAttribute("userInfo", userDTO);
    }
}
