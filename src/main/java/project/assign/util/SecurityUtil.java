package project.assign.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import project.assign.exception.BusinessExceptionHandler;

@RequiredArgsConstructor
public class SecurityUtil {


    public static String getCurrentMemberEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName() == null) {
            throw new BusinessExceptionHandler(HttpStatus.NOT_FOUND, 404, "사용자를 확인할 수 없습니다");
        }
        return authentication.getName();
    }
}
