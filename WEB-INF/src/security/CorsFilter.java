package security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

public class CorsFilter implements Filter {

    private static final Pattern ALLOWED_ORIGIN_PATTERN = Pattern.compile(
        "^https?://(([a-z0-9-]+\\.)*edihamiti\\.fr|localhost(:[0-9]+)?)$",
        Pattern.CASE_INSENSITIVE
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String origin = req.getHeader("Origin");
        if (origin != null && ALLOWED_ORIGIN_PATTERN.matcher(origin).matches()) {
            resp.setHeader("Access-Control-Allow-Origin", origin);
            resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
            resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            resp.setHeader("Access-Control-Allow-Credentials", "true");
            resp.setHeader("Access-Control-Max-Age", "3600");
        }

        // Handle preflight requests
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}
