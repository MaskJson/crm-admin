package com.moving.admin.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class OriginFilter
 */
public class OriginFilter implements Filter {

    /**
     * Default constructor.
     */
    public OriginFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;
        String url = req.getRemoteHost();
        url = url != null ? url : "";
//		if(url.indexOf("111.230.200.16")>-1) {
//			chain.doFilter(req, rep);
//		}
//		else {
//			response.setCharacterEncoding("UTF-8");
//			response.setContentType("application/json; charset=utf-8");
//			PrintWriter printWriter = response.getWriter();
//			Map<String, Object> map = new HashMap<>();
//			map.put("code", 400);
//			map.put("message", "没有访问权限，请联系管理员");
//			map.put("data", null);
//			printWriter.write(map.toString());
//			printWriter.close();
//		}
        chain.doFilter(req, rep);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}
