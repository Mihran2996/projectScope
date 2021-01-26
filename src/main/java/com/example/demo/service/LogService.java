package com.example.demo.service;

import com.example.demo.model.Log;
import com.example.demo.model.Project;
import com.example.demo.repository.LogRepository;
import com.example.demo.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final ProjectRepository projectRepository;


    public void delete(List<Integer> ids) {
        List<Project> projects = new LinkedList<>();
        for (Integer id : ids) {
            Log log = logRepository.getOne(id);
            projects.add(projectRepository.getOne(log.getProject().getId()));
            logRepository.deleteById(id);
        }
        for (Project project : projects) {
            sumLog(project.getId());
        }
    }

    public void sumLog(int projectId) {
        Project project = projectRepository.getOne(projectId);
        List<Log> logs = logRepository.findAllLogByProjectId(projectId);
        if (logs.size() == 0) {
            project.setHours(0.0);
            projectRepository.save(project);
        } else {
            project.setHours(logRepository.getSum(projectId));
            projectRepository.save(project);
        }
    }
}
