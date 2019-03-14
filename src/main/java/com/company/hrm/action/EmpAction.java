package com.company.hrm.action;

import com.company.hrm.common.ResResult;
import com.company.hrm.common.ServiceConst;
import com.company.hrm.common.SpringIoC;
import com.company.hrm.dao.pojo.Emp;
import com.company.hrm.service.iService.IEmpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EmpAction extends ActionSupport implements ModelDriven<Emp>, RequestAware, SessionAware {
    private Emp e = new Emp();
    private HttpServletRequest request = ServletActionContext.getRequest();
    private HttpServletResponse response = ServletActionContext.getResponse();

    @Override
    public Emp getModel() {
        return e;
    }

    @Override
    public void setRequest(Map<String, Object> map) {

    }

    @Override
    public void setSession(Map<String, Object> map) {

    }

    public String EmpDelete() throws Exception {
        int empno = Integer.parseInt(request.getParameter("empno"));
        Emp emp = new Emp();
        emp.setEmpno(empno);
        IEmpService empservice = (IEmpService) SpringIoC.getContext().getBean("empService");
        String msg = empservice.delete(emp);
        ResResult<Emp> res = null;
        if (msg.equals(ServiceConst.SUCCESS)) {
            res = ResResult.success("delete success!");
        } else {
            res = ResResult.error(500, "delete error");
        }
        String jsonRes = new ObjectMapper().writeValueAsString(res);
        PrintWriter out = response.getWriter();
        out.println(jsonRes);
        out.flush();
        out.close();
        return null;
    }

    public String EmpFindAll() throws Exception {
        //判断用户是否登录
        HttpSession session = request.getSession();
        ResResult<List<Emp>> result = null;
        if (session.getAttribute("username") != null) {
            //执行
            IEmpService empservice = (IEmpService) SpringIoC.getContext().getBean("empService");
            List<Emp> empList = new ArrayList<Emp>();
            empList = empservice.findAll();
            if (!empList.isEmpty() && empList.size() > 0) {
                result = ResResult.success("find all success", empList);
            } else {
                result = ResResult.error(404, "no data");
            }
        } else {
            result = ResResult.error(301, "have not login");
        }
        PrintWriter out = response.getWriter();
        String jsonResult = new ObjectMapper().writeValueAsString(result);
        out.println(jsonResult);
        out.flush();
        out.close();
        return null;
    }

    public String EmpFindById() throws Exception {
        int empno = Integer.parseInt(request.getParameter("empno"));
        IEmpService empservice = (IEmpService) SpringIoC.getContext().getBean("empService");
        Emp emp = null;
        ResResult<List<Emp>> res = null;
        List<Emp> empList = new ArrayList<Emp>();
        if (empservice.findById(empno) != null) {
            emp = empservice.findById(empno);
            empList.add(emp);
            res = ResResult.success("find success", empList);
        } else {
            res = ResResult.error(404, "no data");
        }
        PrintWriter out = response.getWriter();
        String jsonRes = new ObjectMapper().writeValueAsString(res);
        System.out.println(jsonRes);
        out.println(jsonRes);
        out.flush();
        out.close();
        return null;
    }

    public String EmpFindByName() throws Exception {
        String ename = request.getParameter("ename");
        IEmpService empservice = (IEmpService) SpringIoC.getContext().getBean("empService");
        List<Emp> empList = empservice.findByName(ename);
        ResResult<List<Emp>> res = null;

        if (!empList.isEmpty() && empList.size() > 0) {
            res = ResResult.success("success", empList);
            System.out.println(res);
        } else {
            res = ResResult.error(404, "no such data");
        }
        PrintWriter out = response.getWriter();
        String jsonRes = new ObjectMapper().writeValueAsString(res);
        System.out.println(jsonRes);
        out.println(jsonRes);
        out.flush();
        out.close();
        return null;
    }

    public String EmpSave() throws Exception {
        int empno = Integer.parseInt(request.getParameter("empno"));
        String ename = request.getParameter("ename");
        String job = request.getParameter("job");
        int mgr = Integer.parseInt(request.getParameter("mgr"));
        Date hiredate = null;

        try {
            hiredate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("hiredate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String sals = request.getParameter("sal");
        BigDecimal sal = null;
        if (!sals.isEmpty()) {
            sal = new BigDecimal(sals);
        }

        String comms = request.getParameter("comm");
        BigDecimal comm = null;
        if (!comms.isEmpty()) {
            comm = new BigDecimal(comms);
        }

        int deptno = Integer.parseInt(request.getParameter("deptno"));

        IEmpService empservice = (IEmpService) SpringIoC.getContext().getBean("empService");
        Emp emp = new Emp(empno, ename, job, mgr, hiredate, sal, comm, deptno);
        String msg = empservice.save(emp);
        System.out.println(msg);
        ResResult<Emp> res = null;
        if (msg.indexOf(ServiceConst.SUCCESS) != -1) {
            res = ResResult.success("success");
        } else {
            res = ResResult.error(500, "save error");
        }
        String jsonResult = new ObjectMapper().writeValueAsString(res);
        PrintWriter out = response.getWriter();
        out.println(jsonResult);
        out.flush();
        out.close();
        return null;
    }

    public String EmpUpdate() throws Exception {
        int empno = Integer.parseInt(request.getParameter("empno"));
        System.out.println(empno);
        String ename = request.getParameter("ename");
        String job = request.getParameter("job");
        int mgr = Integer.parseInt(request.getParameter("mgr"));
        Date hiredate = null;

        try {
            hiredate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("hiredate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String sals = request.getParameter("sal");
        BigDecimal sal = null;
        if (!sals.isEmpty()) {
            sal = new BigDecimal(sals);
        }

        String comms = request.getParameter("comm");
        BigDecimal comm = null;
        if (!comms.isEmpty()) {
            comm = new BigDecimal(comms);
        }

        int deptno = Integer.parseInt(request.getParameter("deptno"));

        IEmpService empservice = (IEmpService) SpringIoC.getContext().getBean("empService");
        Emp emp = new Emp(empno, ename, job, mgr, hiredate, sal, comm, deptno);
        String msg = empservice.update(emp);
        ResResult<Emp> res = null;
        if (msg.indexOf(ServiceConst.SUCCESS) != -1) {
            res = ResResult.success("update success");
        } else {
            res = ResResult.error(500, "update error");
        }
        String jsonRes = new ObjectMapper().writeValueAsString(res);
        PrintWriter out = response.getWriter();
        out.println(jsonRes);
        out.flush();
        out.close();
        return null;
    }
}
