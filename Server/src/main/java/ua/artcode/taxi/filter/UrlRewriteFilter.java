package ua.artcode.taxi.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.hibernate.jpa.internal.EntityManagerImpl.LOG;

public class UrlRewriteFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        LOG.info("created UrlRewriteFilter");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                                                        throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) req;
        String requestURI = request.getRequestURI();

        if (requestURI.contains("/order/user")) {
            String toReplace = requestURI.substring(requestURI.indexOf("/app/order"), requestURI.lastIndexOf("/"));
            String newURI = requestURI.replace(toReplace, "");
            req.getRequestDispatcher(newURI).forward(req, res);

            LOG.info(String.format("URI %s change to URI %s", requestURI, newURI));

        } else if (requestURI.contains("/order/order")) {
            String[] orders = requestURI.split("/");
            int count = 0;
            for (String order : orders) {
                if (order.equals("order")) {
                    count++;
                }
            }
            if (count > 1) {
                String toReplace = requestURI.substring(requestURI.indexOf("/app/order"), requestURI.lastIndexOf("/"));
                String newURI = requestURI.replace(toReplace, "/order");
                req.getRequestDispatcher(newURI).forward(req, res);

                LOG.info(String.format("URI %s change to URI %s", requestURI, newURI));
            }

        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        //
    }
}
