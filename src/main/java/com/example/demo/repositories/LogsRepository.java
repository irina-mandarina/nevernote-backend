package com.example.demo.repositories;

import com.example.demo.Entities.Log;
import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface LogsRepository extends JpaRepository<Log, Long>, JpaSpecificationExecutor<Log> {
    List<Log> findAllByUserOrSubjectAndSubjectIdIsInOrderByIdDesc(User user, String subject, List<Long> subjectIds);
    List<Log> findAllBySubjectAndSubjectId(String subject, Long subjectId);
}
