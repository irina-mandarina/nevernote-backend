package com.example.demo.Services;

import com.example.demo.Entities.Log;
import com.example.demo.Entities.User;
import com.example.demo.models.GET.LogResponse;
import com.example.demo.types.Method;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LogService {
    ResponseEntity<String> getNoteLogs(Long noteId);
    ResponseEntity<String> getUserLogs(String username);
    List<Log> findAllByUserOrderByIdDesc(User user);
    List<Log> findAllBySubjectAndSubjectId(String subject, Long subjectId);
    List<LogResponse> logsToLogResponses(List<Log> logs);
    void log(ResponseEntity<String> response, String username, Method methodType, String path);
}
