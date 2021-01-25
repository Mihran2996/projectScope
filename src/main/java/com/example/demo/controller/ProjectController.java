package com.example.demo.controller;

import com.example.demo.model.Project;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.LogService;
import com.example.demo.service.ProjectService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/project")
    public String projectPage(ModelMap modelMap, @AuthenticationPrincipal CurrentUser user,
                              @RequestParam(value = "msg", required = false) String msg,
                              @RequestParam(value = "msg2", required = false) String msg2) {
        modelMap.addAttribute("projects", projectService.getAllProjects());
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("msg2", msg2);
        modelMap.addAttribute("user", user.getUser());
        return "project";
    }

    @GetMapping("/create")
    public String createPage(ModelMap modelMap, @RequestParam(value = "msg", required = false) String msg) {
        modelMap.addAttribute("project", new Project());
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("users", userService.getAllUsers());
        return "create";
    }

    @PostMapping("/create/project")
    public String create(@Valid @ModelAttribute Project project, BindingResult bindingResult, ModelMap modelMap,
                         @RequestParam(value = "id", defaultValue = "") List<Integer> ids) {
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("users", userService.getAllUsers());
            return "create";
        } else if (ids.size() == 0) {
            return "redirect:/create?msg=User cannot be null!";
        }
        List<User> users = new ArrayList<>();
        for (Integer id : ids) {
            User userById = userService.getUserById(id);
            users.add(userById);
        }
        project.setUsers(users);
        projectService.createProject(project);
        return "redirect:/project?msg2=Project was created";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(value = "id", defaultValue = "") List<Integer> ids,
                         @AuthenticationPrincipal CurrentUser user) {
        if (user.getUser().getRole() == Role.TEAM_LEADER) {
            if (ids.size() == 0) {
                return "redirect:/project?msg=id was null";
            } else {
                projectService.delete(ids);
                return "redirect:/project?msg2=Project was deleted";
            }
        } else if (ids.size() == 0) {
            return "redirect:/log/page?msg=id was null";
        }
        logService.delete(ids);
        return "redirect:/log/page?msg2=Log was deleted";
    }

    @PostMapping("/search/name")
    public String search(@RequestParam(value = "keyword", defaultValue = "") String keyword, ModelMap modelMap,
                         @AuthenticationPrincipal CurrentUser user,
                         @RequestParam(value = "msg", required = false) String msg) {
        if (keyword.length() == 0 || keyword == null) {
            return "redirect:/project?msg=Name was null";
        }

        modelMap.addAttribute("projects", projectRepository.findByNameLike("%" + keyword + "%"));
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("user", user.getUser());
        return "search";
    }

    @PostMapping("/filter/project")
    public String filterProject(@RequestParam(value = "start", defaultValue = "") String start, @RequestParam(value = "end", defaultValue = "1900-01-01") Date end
            , ModelMap modelMap, @AuthenticationPrincipal CurrentUser user) {
        if (start.length() == 0 && end.toString().equals("1900-01-01")) {
            return "redirect:/project";
        } else if (start.length() == 0 && !end.toString().equals("1900-01-01")) {
            modelMap.addAttribute("projects", projectService.searchByEnd(end));
            modelMap.addAttribute("user", user.getUser());
            return "filter";
        } else if (start.length() != 0 && end.toString().equals("1900-01-01")) {
            modelMap.addAttribute("projects", projectService.searchByStart(start));
            modelMap.addAttribute("user", user.getUser());
            return "filter";
        }
        modelMap.addAttribute("projects", projectService.filter(start, end));
        modelMap.addAttribute("user", user.getUser());
        return "filter";

    }
}
