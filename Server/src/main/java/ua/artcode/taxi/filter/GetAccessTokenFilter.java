package ua.artcode.taxi.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetAccessTokenFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(GetAccessTokenFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("created GetAccessTokenFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                                                            throws IOException, ServletException {
        if(!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)){
            // nope
        } else {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;

            Object accessTokenObj = req.getSession().getAttribute("accessToken");
            req.setAttribute("accessToken", accessTokenObj);

            LOG.info(String.format("User ID=%s get accessToken: %s",
                    req.getSession().getAttribute("currentUserID"),
                    String.valueOf(accessTokenObj)));
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }

}
