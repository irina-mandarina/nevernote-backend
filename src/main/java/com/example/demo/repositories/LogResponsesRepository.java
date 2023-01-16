package com.example.demo.repositories;

import com.example.demo.Entities.Log;
import com.example.demo.Entities.User;
import com.example.demo.models.GET.LogResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogResponsesRepository extends JpaSpecificationExecutor<LogResponse> {
}
