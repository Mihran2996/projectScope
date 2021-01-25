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
            logRepository.deleteById(id);
            projects.add(projectRepository.getOne(log.getProject().getId()));
        }
        for (Project project : projects) {
            int id = project.getId();
            sumLog(id);
        }
    }

    public void sumLog(int projectId) {
        Project project = projectRepository.getOne(projectId);
        List<Log> allByProjectId = logRepository.findAllByProjectId(projectId);
        double sum = 0.0;
        int j = 0;
        for (int i = 0; i < allByProjectId.size(); i += 2) {
            j = i + 1;
            if (i == allByProjectId.size()) {
                break;
            } else if (allByProjectId.size() == 1) {
                Log byProjectId = logRepository.findByProjectId(projectId);
                sum = byProjectId.getHours();
                break;
            } else {
                sum += Double.sum(allByProjectId.get(i).getHours(), allByProjectId.get(j).getHours());
            }
        }
        project.setHours(sum);
        projectRepository.save(project);
    }
}
