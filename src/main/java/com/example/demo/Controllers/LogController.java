package com.example.demo.Controllers;

import com.example.demo.Entities.Log;
import com.example.demo.Services.LogService;
import com.example.demo.repositories.search_criteria.SearchCriteria;
import com.example.demo.repositories.specification.specification_builders.LogSpecificationsBuilder;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class LogController {
    private final LogService logService;
//
//    @GetMapping("/history")
//    ResponseEntity<String> getUserLogs(@RequestAttribute String username) {
//        return logService.getUserLogs(username);
//    }

    @GetMapping("/{noteId}/history")
    ResponseEntity<String> getNoteLogs(@PathVariable Long noteId) {
        return logService.getNoteLogs(noteId);
    }

    @GetMapping("/history")
    ResponseEntity<String> searchLogs(@RequestParam(value = "search") String search) {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }
        return logService.searchLogs(params);
    }
}
