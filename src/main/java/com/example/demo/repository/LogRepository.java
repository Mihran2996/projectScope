package com.example.demo.repository;


import com.example.demo.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findAllByUserId(int id);

    List<Log> findAllLogByProjectId(int id);

    @Query(value = "select sum(hours) as projectscope from Log where project_id=:id", nativeQuery = true)
    Double getSum(@Param("id") int id);

}
