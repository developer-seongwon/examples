package org.sw.sample.record.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sw.sample.record.user.api.record.AllUserResponseRecord;
import org.sw.sample.record.user.service.UserService;
import org.sw.sample.record.user.service.dto.AllUserRequest;
import org.sw.sample.record.user.service.dto.AllUserResponse;

/**
 * 사용자 API 컨트롤러
 */
@RestController
@RequestMapping("/api/gamerecord")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 모든 사용자 조회
     * GET /api/gamerecord/users
     */
    @GetMapping("/users")
    public ResponseEntity<AllUserResponseRecord> getUsers() {
        AllUserRequest request = new AllUserRequest();
        AllUserResponse response = this.userService.getUsers(request);
        return ResponseEntity.status(200)
                .header("Content-Type", "application/json")
                .header("User-Length", String.valueOf(response.getSize()))
                .body(new AllUserResponseRecord(response.getUsers()));
    }
}
