package com.AgroLink.User.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 🔧 AppMaintenanceFilter
 *
 * Blocks requests based on:
 *  - Full application maintenance mode
 *  - Specific restricted paths
 *  - Specific restricted origins
 */


//sourav:- killswitch for maintenance mode
@Component
public class AppMaintenanceFilter extends OncePerRequestFilter {

    @Value("${app.maintenanceMode:false}")
    private boolean maintenanceMode;

    @Value("#{'${app.maintenancePaths:/api/orders,/api/admin}'.split(',')}")
    private List<String> maintenancePaths;

    @Value("${app.originRestrictionEnabled:false}")
    private boolean originRestrictionEnabled;

    @Value("${app.restrictedOrigin:http://localhost:5173}")
    private String restrictedOrigin;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String origin = request.getHeader("Origin");

        boolean blockAll = maintenanceMode;
        boolean blockPath = maintenancePaths.stream().anyMatch(uri::startsWith);
        boolean blockOrigin = originRestrictionEnabled && origin != null && origin.equals(restrictedOrigin);

        if (blockAll || blockPath || blockOrigin) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Service temporarily unavailable due to maintenance [Enabled].\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
