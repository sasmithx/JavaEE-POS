package lk.ijse.gdse68.javaeepos.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "CORSFilter", urlPatterns = "/*")
public class CORSFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        String origin = req.getHeader("Origin");
        System.out.println(origin);
        if(origin == null){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "CORS Policy Violation");
            return;
        }

        res.setHeader("Access-Control-Allow-Origin", origin);
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        res.setHeader("Access-Control-Allow-Methods", "DELETE, PUT, GET, POST, OPTIONS, HEAD ");

        chain.doFilter(req, res);
    }
}
