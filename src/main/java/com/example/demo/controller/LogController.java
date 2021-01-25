package com.example.demo.controller;

import com.example.demo.model.Log;
import com.example.demo.repository.LogRepository;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.LogService;
import com.example.demo.service.ProjectService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@Controller
public class LogController {
    @Autowired
    LogRepository logRepository;
    @Autowired
    LogService logService;
    @Autowired
    UserService userService;
    @Autowired
    ProjectService projectService;

    @GetMapping("/log/page")
    public String logsPage(ModelMap modelMap, @AuthenticationPrincipal CurrentUser user,
                           @RequestParam(value = "msg", required = false) String msg,
                           @RequestParam(value = "msg2", required = false) String msg2) {
        modelMap.addAttribute("logs", logRepository.findAllByUserId(user.getUser().getId()));
        modelMap.addAttribute("user", user.getUser());
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("msg2", msg2);
        return "logs";
    }

    @GetMapping("/create/page")
    public String createPage(ModelMap modelMap, @RequestParam(value = "msg", required = false) String msg,
                             @AuthenticationPrincipal CurrentUser user) {
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("projects", projectService.getAllProjectsById(user.getUser().getId()));
        return "createLogs";
    }

    @PostMapping("/create/log")
    public String create(@RequestParam(value = "date", defaultValue = "1900-01-01") Date date,
                         @RequestParam(value = "hours", defaultValue = "0") double hours, @RequestParam(value = "user.id") int userId,
                         @RequestParam(value = "project.id", defaultValue = "0") int projectId) {

        if (date.toString().equals("1900-01-01") || hours == 0.0) {
            return "redirect:/log/page?msg=log was not created";
        } else if (projectId == 0) {
            return "redirect:/log/page?msg=log was not created";
        }
        Log log = Log.builder()
                .user(userService.getUserById(userId))
                .project(projectService.getProjectById(projectId))
                .date(date)
                .hours(hours)
                .build();
        logRepository.save(log);
        logService.sumLog(projectId);
        return "redirect:/log/page?msg2=Log was created";

    }


}