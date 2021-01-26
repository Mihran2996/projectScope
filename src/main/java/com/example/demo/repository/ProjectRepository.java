package com.example.demo.repository;


import com.example.demo.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByNameLike(String keyword);
    @Query(value = "select * from project join user_project where user_id=:id", nativeQuery = true)
    List<Project> getAllProjectByUserId(@Param("id") int id);

}
