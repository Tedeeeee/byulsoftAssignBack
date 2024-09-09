package project.assign.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName() == null) {
            throw new RuntimeException("다시 로그인을 진행해주세요");
        }
        return authentication.getName();
    }
}
