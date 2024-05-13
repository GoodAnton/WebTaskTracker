package leontev.webtasktracker.controllers;

import leontev.webtasktracker.dto.TaskDto;
import leontev.webtasktracker.services.Impl.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects/{project_id}/task-states/{task_state_id}/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    @GetMapping()
    public String getTasks (@PathVariable (name = "task_state_id") Long taskStateId,
                            Model model) {
        model.addAttribute("tasks", taskService.getTasks(taskStateId));
        return "tasks";
    }

    @PostMapping()
    public String createTask(@PathVariable (name = "task_state_id") Long taskStateId,
                              @ModelAttribute @Validated TaskDto taskDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("task", taskDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/projects/error";
        }
        taskService.createTask(taskStateId, taskDto);

        return "redirect:/projects/{project_id}";
    }

    @PostMapping("{task_id}/update")
    public String editTask( @PathVariable (name = "task_id") Long taskId,
                            @ModelAttribute @Validated TaskDto taskDto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("task", taskDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/projects/error";
        }

        taskService.editTask(taskId, taskDto);
        return "redirect:/projects/{project_id}";
    }

    @PostMapping("{task_id}/delete")
    public String deleteTask(@PathVariable (name = "task_id") Long taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/projects/{project_id}";
    }
}
