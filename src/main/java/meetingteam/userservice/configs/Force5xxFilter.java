package meetingteam.userservice.configs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import meetingteam.commonlibrary.exceptions.InternalServerException;

@Component
public class Force5xxFilter extends OncePerRequestFilter{
    @Value("${simulation.anomalies.enable-5xx-error:false}")
    private boolean enable5xxError;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(enable5xxError){
            throw new InternalServerException("Simulate 5xx error");
        }
        else {
            filterChain.doFilter(request, response);
        }
    }
    
}
