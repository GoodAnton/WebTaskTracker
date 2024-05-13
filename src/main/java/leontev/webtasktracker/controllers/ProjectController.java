package leontev.webtasktracker.controllers;

import leontev.webtasktracker.dto.ProjectDto;
import leontev.webtasktracker.enums.Phase;
import leontev.webtasktracker.services.Impl.ProjectServiceImpl;
import leontev.webtasktracker.services.Impl.TaskStateServiceImpl;
import leontev.webtasktracker.services.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectServiceImpl projectService;
    private final UserServiceImpl userService;
    private final TaskStateServiceImpl taskStateService;

    @GetMapping()
    public String getAllProjects(@AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {

        model.addAttribute("projects", projectService.getAllProjects(
                userService.findUserByEmail(userDetails.getUsername()).getUserId()
        ));

        return "projects";
    }

    @GetMapping("/{project_id}")
    public String getProjectById(@PathVariable(name = "project_id") Long projectId,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {

        model.addAttribute("project", projectService.getProjectById(projectId,
                userService.findUserByEmail(userDetails.getUsername()).getUserId()));
        model.addAttribute("states", taskStateService.getTaskStates(projectId));
        model.addAttribute("phases", Phase.values());
        return "project";
    }

    @PostMapping()
    public String createProject(@ModelAttribute @Validated ProjectDto projectDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("project", projectDto);
            redirectAttributes.addFlashAttribute("createProjectError", bindingResult.getAllErrors());
                return "redirect:/projects/error";
        }

        ProjectDto dto = projectService.createProject(projectDto.getName(), userDetails.getUsername());
        return "redirect:/projects/" + dto.getId();
    }

    @PostMapping("{project_id}/update")
    public String editProject(@PathVariable(name = "project_id") Long projectId,
                              @ModelAttribute @Validated ProjectDto projectDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("project", projectDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/projects/error";
        }

        ProjectDto dto = projectService.editProject(projectId,projectDto.getName());
        return "redirect:/projects/" + dto.getId();
    }

    @PostMapping("/{project_id}/delete")
    public String deleteProjectByName(@PathVariable(name = "project_id") Long projectId) {
        projectService.deleteProject(projectId);
        return "redirect:/projects";
    }

    @GetMapping("/error")
    public String getErrorPage(Model model, @ModelAttribute ProjectDto projectDto) {
        model.addAttribute("project", projectDto);
        return "errors";
    }

}

