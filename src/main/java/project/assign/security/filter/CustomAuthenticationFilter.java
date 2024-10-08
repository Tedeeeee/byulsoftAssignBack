package project.assign.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.assign.entity.Member;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest = null;
        try {
            authRequest = getAuthRequest(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws IOException {
        Member member = objectMapper.readValue(request.getInputStream(), Member.class);

        return new UsernamePasswordAuthenticationToken(member.getMemberEmail(), member.getMemberPassword());


    }
}
