package com.example.demo.service;

import com.example.demo.model.Project;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getAllProjectsById(int id) {
        List<Project> projects = new ArrayList<>();
        List<Project> allProjects = projectRepository.findAll();
        for (int i = 0; i < allProjects.size(); i++) {
            for (User user : allProjects.get(i).getUsers()) {
                if (user.getId() == id) {
                    projects.add(allProjects.get(i));
                }
            }
        }
        return projects;
    }

    public void delete(List<Integer> ids) {
        for (Integer id : ids) {
            projectRepository.deleteById(id);
        }
    }

    public void createProject(Project project) {
        projectRepository.save(project);
    }

    public List<Project> filter(String start, Date end) {
        List<Project> allProject = projectRepository.findAll();
        List<Project> projects = new ArrayList<>();
        for (Project project : allProject) {
            if (start.equals(project.getDate()) && end.after(project.getDeadline())) {
                projects.add(project);
            }
        }
        return projects;
    }

    public List<Project> searchByEnd(Date end) {
        List<Project> allProject = projectRepository.findAll();
        List<Project> projects = new ArrayList<>();
        for (Project project : allProject) {
            if (end.after(project.getDeadline())) {
                projects.add(project);
            }
        }
        return projects;
    }

    public List<Project> searchByStart(String start) {
        List<Project> allProject = projectRepository.findAll();
        List<Project> projects = new ArrayList<>();
        for (Project project : allProject) {
            String date = String.valueOf(project.getDate()).substring(0, 10);
            if (start.equals(date)) {
                projects.add(project);
            }
        }
        return projects;
    }

    public Project getProjectById(int id) {

        return projectRepository.getOne(id);
    }
}
