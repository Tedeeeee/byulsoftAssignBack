package project.assign.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import project.assign.commonApi.ErrorResponse;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.info("Access denied", accessDeniedException);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN, 403, "권한의 오류가 있습니다");
        // 응답 헤더 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JSON 형태로 응답 작성
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        // 응답에 JSON 데이터 쓰기
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
        //response.sendError(HttpServletResponse.SC_FORBIDDEN, "이번엔 권한에러다");
    }
}
