package com.example.backend;

import java.time.LocalTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public ResponseEntity<?> test() {
        System.out.println(LocalTime.now());
        return ResponseEntity.ok("Hello World");
    }
}
