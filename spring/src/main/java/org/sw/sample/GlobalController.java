package org.sw.sample;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sw.sample.exception.NotFoundException;

@RestController
public class GlobalController {

    /**
     * 모든 HTTP 요청을 수집하는 메소드
     */
    /**
     * 지원하지 않는 경로에 대한 처리
     */
    @RequestMapping("/**")
    public void unsupported(HttpServletRequest request) {
        // check black-list
        System.out.println("Remote IP: " + request.getRemoteAddr());
        throw NotFoundException.ofPath(String.format(
                "Unsupported Path - %s %s",
                request.getMethod(), request.getRequestURI()
        ));
    }
}
