package filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jianguo.thrift.user.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 登录filter
 */
public abstract class LoginFilter implements Filter {
    private final String TOKEN = "token";
    private static Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000)
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .build();

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1、获取token
        String token;
        token = request.getParameter(TOKEN);
        if (StringUtils.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (Objects.equals(TOKEN, cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

        }

        // 2、根据token获取UserDTO
        UserDTO userDTO = null;
        if (StringUtils.isNotEmpty(token)) {
            String userStr = cache.getIfPresent(token);
            if (StringUtils.isNotEmpty(userStr)) {
                userDTO = JSONObject.parseObject(userStr, UserDTO.class);
            } else {
                userDTO = getUserDTOByToken(token);
                if (userDTO != null) {
                    cache.put(token, JSON.toJSONString(userDTO));
                }
            }
        }

        // 3、没有登录，去登录
        if (userDTO == null) {
            response.sendRedirect("http://localhost/user/login");
            return;
        }

        login(request, response, userDTO);

        filterChain.doFilter(request, response);


    }

    /**
     * 其他应用集成时可以自定义登录成功后的动作
     *
     * @param request
     * @param response
     * @param userDTO
     */
    protected abstract void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO);

    private UserDTO getUserDTOByToken(String token) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost/user/fetchUserByToken");
        httpPost.addHeader(TOKEN, token);
        CloseableHttpResponse execute = null;
        try {
            execute = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int statusCode = execute.getStatusLine().getStatusCode();
        if (!Objects.equals(statusCode, HttpStatus.SC_OK)) {
            throw new RuntimeException("post请求失败");
        }
        HttpEntity entity = execute.getEntity();
        try {
            return JSONObject.parseObject(EntityUtils.toString(entity), UserDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void destroy() {

    }
}