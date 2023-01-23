package com.example.demo.services;

import com.example.demo.Entities.Log;
import com.example.demo.Entities.User;
import com.example.demo.models.GET.LogResponse;
import com.example.demo.repositories.search_criteria.OrderCriteria;
import com.example.demo.repositories.search_criteria.SearchCriteria;
import com.example.demo.types.Method;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LogService {
    ResponseEntity<String> getNoteLogs(Long noteId);
    ResponseEntity<String> getUserLogs(String username);

    List<Log> findAllByUserOrSubjectAndSubjectIdIsInOrderByIdDesc(
            User user, String subject, List<Long> subjectIds);

    List<Log> findAllBySubjectAndSubjectId(String subject, Long subjectId);
    List<LogResponse> logsToLogResponses(List<Log> logs);
    void log(ResponseEntity<String> response, String username, Method methodType, String path);

    ResponseEntity<String> searchLogs(String username, final List<SearchCriteria> params, final List<OrderCriteria> orderParams);
}
