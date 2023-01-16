package com.example.demo.Controllers;

import com.example.demo.Entities.Log;
import com.example.demo.Services.LogService;
import com.example.demo.repositories.specification.specification_builders.LogSpecificationsBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @GetMapping("/logs/search")
    ResponseEntity<String> searchLogs(@RequestParam(value = "search") String search) {
        LogSpecificationsBuilder builder = new LogSpecificationsBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Log> spec = builder.build();
        return logService.searchLogs(spec);
    }
}
