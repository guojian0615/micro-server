package com.jianguo.useredgeservice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jianguo.thrift.user.UserDTO;
import com.jianguo.useredgeservice.eum.ReturnCodeEum;
import com.jianguo.useredgeservice.response.LoginResponse;
import com.jianguo.useredgeservice.response.ResponseBase;
import com.jianguo.useredgeservice.thrift.UserServiceProvider;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceProvider userServiceProvider;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/login")
    public String login() {
        System.out.println("hello world");
        return "login";
    }

    /**
     * 登录成功，返回token
     *
     * @param username
     * @param pwd
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseBase login(@RequestParam("username") String username,
                              @RequestParam("pwd") String pwd) {
        // 1、验证用户信息
        UserDTO userDTO;
        try {
            userDTO = userServiceProvider.userServiceClient().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return new ResponseBase(ReturnCodeEum.USERNAME_OR_PWD_ISINVALID);
        }
        if (userDTO == null) {
            return new ResponseBase(ReturnCodeEum.USERNAME_OR_PWD_ISINVALID);
        }
        if (!Objects.equals(userDTO.getPassword(), md5(pwd))) {
            return new ResponseBase(ReturnCodeEum.USERNAME_OR_PWD_ISINVALID);
        }
        // 2、生成token
        String token = generateToken();

        // 3、将token和用户信息存入缓存redis
        stringRedisTemplate.opsForValue().set(token, JSON.toJSONString(userDTO), 3600, TimeUnit.SECONDS);
        return new LoginResponse(token, ReturnCodeEum.SUCCESS);
    }

    /**
     * 根据token从redis中获取用户信息
     *
     * @param token
     * @return
     */
    @PostMapping("/fetchUserByToken")
    @ResponseBody
    public UserDTO getUserDTOByToken(@RequestHeader("token") String token) {
        String jsonStr = stringRedisTemplate.opsForValue().get(token);
        return JSONObject.parseObject(jsonStr, UserDTO.class);
    }

    /**
     * 注册用户信息
     *
     * @param userInfo
     */
    @PostMapping("/register")
    @ResponseBody
    public ResponseBase registerUser(UserDTO userInfo) {
        try {
            userInfo.setPassword(md5(userInfo.getPassword()));
            userServiceProvider.userServiceClient().registerUser(userInfo);
            return new ResponseBase(ReturnCodeEum.SUCCESS);
        } catch (TException e) {
            e.printStackTrace();
            return new ResponseBase(ReturnCodeEum.ERROR);
        }
    }


    private String generateToken() {
        String charSet = "0123456789abcdefghijklmnoptrstuvwxyz";
        return buildRandom(charSet, 32);
    }

    private String buildRandom(String charSet, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(length);
            stringBuilder.append(charSet.charAt(index));
        }
        return stringBuilder.toString();

    }

    private String md5(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(password.getBytes("utf-8"));
            return HexUtils.toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }

}
