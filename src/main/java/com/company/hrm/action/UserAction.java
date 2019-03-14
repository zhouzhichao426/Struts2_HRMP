package com.company.hrm.action;

import com.company.hrm.common.ResResult;
import com.company.hrm.common.ServiceConst;
import com.company.hrm.common.SpringIoC;
import com.company.hrm.dao.pojo.User;
import com.company.hrm.service.iService.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class UserAction extends ActionSupport implements ModelDriven<User>, RequestAware, SessionAware {

    private User u = new User();
    private HttpServletRequest request = ServletActionContext.getRequest();
    private HttpServletResponse response = ServletActionContext.getResponse();

    @Override
    public User getModel() {
        return u;
    }

    @Override
    public void setRequest(Map<String, Object> map) {

    }

    @Override
    public void setSession(Map<String, Object> map) {

    }

    public String UserLogin() throws Exception {
        IUserService userService = (IUserService) SpringIoC.getContext().getBean("userService");
        User user = userService.login(u.getUsername(), u.getUserpassword());
        ResResult<User> result = null;
        if(user != null){
            HttpSession session = request.getSession();
            session.setAttribute("username", user.getUsername());
            result = ResResult.success("login success",user);
        } else {
            result = ResResult.error(500, "password error");
        }
        PrintWriter out = response.getWriter();
        String jsonResult = new ObjectMapper().writeValueAsString(result);
        out.write(jsonResult);
        out.flush();
        out.close();
        return null;
    }
    public String UserRegist() throws Exception {
        String username = request.getParameter("username");
        String userpassword1 = request.getParameter("userpassword");
        IUserService userservice = (IUserService) SpringIoC.getContext().getBean("userService");
        User user = new User(username, userpassword1, 1);
        ResResult res = null;
        String a = userservice.regist(user);
        if(a.equals(ServiceConst.SUCCESS)){
            res = ResResult.success("regist success",user);
        } else {
            res = ResResult.error(500, "regist error");
        }
        PrintWriter out = response.getWriter();
        String jsonResult = new ObjectMapper().writeValueAsString(res);
        out.write(jsonResult);
        out.flush();
        out.close();
        return null;
    }
    public String UserExist() throws Exception {
        String username = request.getParameter("username");
        IUserService userService = (IUserService) SpringIoC.getContext().getBean("userService");
        boolean flag = userService.isExist(username);
        ResResult result = flag? ResResult.success() : ResResult.error(404, "no such user");
        String resultJson = new ObjectMapper().writeValueAsString(result);
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.println(resultJson);
        out.flush();
        out.close();
        return null;
    }
}
