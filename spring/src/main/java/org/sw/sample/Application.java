package org.sw.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RestController
    public static class ApiController {
        private final String DATA_DIR = "./data/input";
        private final ObjectMapper objectMapper = new ObjectMapper();
        private final UserService userService;

        public ApiController(UserService userService){
            this.userService = userService;
        }

        @GetMapping("/api/gamerecord/users")
        // 여기에 코드를 작성하세요.
        public ResponseEntity<?> getUsers() {
            try {
                return ResponseEntity.ok(this.userService.users());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        @GetMapping("/api/gamerecord/winrate")
        // 여기에 코드를 작성하세요.
        public ResponseEntity<?> getWinrate() {
            try {
                List<Map<String, Object>> users = objectMapper.readValue(
                        Files.readAllBytes(Paths.get(DATA_DIR, "records.json")),
                        new TypeReference<>() {
                        });
                System.out.println(users);
                return ResponseEntity.ok("Hello, world!");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }
}
