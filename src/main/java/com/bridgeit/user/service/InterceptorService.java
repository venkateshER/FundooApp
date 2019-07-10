//package com.bridgeit.user.service;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.bridgeit.utility.TokenUtil;
//
//@Component
//public class InterceptorService extends HandlerInterceptorAdapter {
//	
//	@Autowired
//	TokenUtil tokenUtil;
//	
//    @Override
//    public boolean preHandle(HttpServletRequest request, 
//            HttpServletResponse response, Object handler) throws Exception {
//    			
//    	String token=request.getHeader("token");
//    	long id=tokenUtil.verifyToken(token);
//    	request.setAttribute("userId",id);
//        return true;
//    }
//	
//}
