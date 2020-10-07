package com.h232ch.demospringsecurityh232ch.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoggingFilter extends GenericFilterBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override // 파트 41 커스텀 필터 (GenericFilterBean) : 로깅 필터를 만들어서 적용해보기 (서블릿 필터를 적용하는 것과 마찬가지다)
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start(((HttpServletRequest)servletRequest).getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse); // 다음 필터로 넘겨줘야 함
        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());

    }
}
