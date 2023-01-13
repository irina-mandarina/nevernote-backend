package com.example.demo.Repositories;

import com.example.demo.Entities.Log;
import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsRepository extends JpaRepository<Log, Long> {
    List<Log> findAllByUserOrSubjectAndSubjectIdIsInOrderByIdDesc(User user, String subject, List<Long> subjectIds);
    List<Log> findAllBySubjectAndSubjectId(String subject, Long subjectId);
}
