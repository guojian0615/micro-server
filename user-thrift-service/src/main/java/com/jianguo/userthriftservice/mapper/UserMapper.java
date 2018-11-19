package com.jianguo.userthriftservice.mapper;

import com.jianguo.userthriftservice.domain.Teacher;
import com.jianguo.userthriftservice.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    @Select("select id,username,password,real_name as realName,mobile,email"
            + " from pe_user where id=#{id}")
    User getUserById(@Param("id") Integer id);

    /**
     * 根据用户名查询用户信息
     *
     * @param userName
     * @return
     */
    @Select("select id,username,password,real_name as realName,mobile,email"
            + " from pe_user where username=#{name}")
    User getUserByName(@Param("name") String userName);

    /**
     * 注册用户信息
     *
     * @param user
     */
    @Insert("insert pe_user(username,password,real_name ,mobile,email)"
            + " value(#{u.username},#{u.password},#{u.realName},#{u.mobile},#{u.email})")
    void registerUser(@Param("u") User user);

    /**
     * 根据用户id获取教师信息
     *
     * @param id
     * @return
     */
    @Select("select id,username,password,real_name as realName,mobile,email,intro,stars"
            + " from pe_user u inner join pe_teacher t on u.id = t.user_id where u.id=#{id}")
    Teacher getTeacherById(@Param("id") int id);
}
