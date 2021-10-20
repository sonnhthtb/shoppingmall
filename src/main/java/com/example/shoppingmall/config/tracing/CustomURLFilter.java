package com.example.shoppingmall.config.tracing;

import lombok.extern.slf4j.Slf4j;
import com.example.shoppingmall.constant.SystemConstant;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class CustomURLFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

//		ThreadContext.put(ServletUtils.DEMO_UID, ObjectUtils.defaultIfNull(ServletUtils.getCurrentUserId(), -1L).toString());
		servletRequest.setAttribute(SystemConstant.Tracing.TIME_REQUEST, System.currentTimeMillis());
		logRequest((HttpServletRequest) servletRequest);
		filterChain.doFilter(servletRequest, servletResponse);
	}

	private void logRequest(HttpServletRequest request) {
		if (request != null) {
			StringBuilder data = new StringBuilder();
			data.append("[").append(request.getMethod()).append(": ").append(request.getRequestURI()).append("]")
							.append("[").append(request.getQueryString()).append("]");
			log.info(data.toString());
		}
	}
}
