package com.example.demo.repository;


import com.example.demo.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findAllByUserId(int id);

    List<Log> findAllByProjectId(int id);

    Log findByProjectId(int id);

}
