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

            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));
            req.setAttribute("accessToken", accessToken);

            LOG.info(String.format("Get accessToken from current session for user ID=%s",
                    req.getSession().getAttribute("currentUserID")));
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }

}
