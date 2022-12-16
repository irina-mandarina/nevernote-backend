package com.example.demo.Controllers;

import com.example.demo.Services.LogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping("/history")
    ResponseEntity<String> getUserLogs(@RequestAttribute String username) {
        return logService.getUserLogs(username);
    }

    @GetMapping("/{noteId}/history")
    ResponseEntity<String> getNoteLogs(@PathVariable Long noteId) {
        return logService.getNoteLogs(noteId);
    }
}
