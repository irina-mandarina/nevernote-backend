package com.example.demo.Controllers;

import com.example.demo.repositories.search_criteria.OrderCriteria;
import com.example.demo.services.LogService;
import com.example.demo.repositories.search_criteria.SearchCriteria;
import lombok.AllArgsConstructor;
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

    @GetMapping("/history")
    ResponseEntity<String> getUserLogs(@RequestAttribute String username) {
        return logService.getUserLogs(username);
    }

    @GetMapping("/{noteId}/history")
    ResponseEntity<String> getNoteLogs(@PathVariable Long noteId) {
        return logService.getNoteLogs(noteId);
    }

    // pagination
    // filter for user
    @GetMapping("/history/search")
    ResponseEntity<String> searchLogs(@RequestAttribute String username, @RequestParam(value = "search") String search,
                                      @RequestParam(value = "order") String order) {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        List<OrderCriteria> orderParams = new ArrayList();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }
        if (order != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc),");
            Matcher matcher = pattern.matcher(order + ",");
            while (matcher.find()) {
                orderParams.add(new OrderCriteria(matcher.group(1), matcher.group(3)));
            }
        }
        return logService.searchLogs(username, params, orderParams);
    }
}
