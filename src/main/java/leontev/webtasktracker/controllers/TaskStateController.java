package leontev.webtasktracker.controllers;

import leontev.webtasktracker.dto.TaskStateDto;
import leontev.webtasktracker.services.Impl.TaskStateServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project_id}/task-states")
public class TaskStateController {

    private final TaskStateServiceImpl taskStateService;

    @GetMapping()
    public String getTaskStates(@PathVariable (name = "project_id") Long projectId,
                                Model model) {
        model.addAttribute("states", taskStateService.getTaskStates(projectId));
        return  "states"; //todo нужен ли?
    }

    @PostMapping()
    public String createTaskState(@PathVariable (name = "project_id") Long projectId,
                                  @ModelAttribute @Validated TaskStateDto taskStateDto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("state", taskStateDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/projects/error";
        }

        taskStateService.createTaskState(projectId, taskStateDto);

        return "redirect:/projects/{project_id}";
    }

    @PostMapping("/{task_state_id}/update")
    public String editTaskState(@PathVariable (name = "task_state_id") Long taskStateId,
                                  @ModelAttribute @Validated TaskStateDto taskStateDto,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("state", taskStateDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/projects/error";
        }

        taskStateService.editTaskState(taskStateId, taskStateDto);
        return "redirect:/projects/{project_id}";

    }


    @PostMapping("/{task_state_id}/delete")
    public String deleteTaskState(@PathVariable (name = "task_state_id") Long taskStateId) {
        taskStateService.deleteTaskState(taskStateId);
        return "redirect:/projects/{project_id}";

    }

}
