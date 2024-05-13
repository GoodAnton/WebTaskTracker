package leontev.webtasktracker.controllers;

import leontev.webtasktracker.dto.UserDto;
import leontev.webtasktracker.dto.UserFilterDto;
import leontev.webtasktracker.enums.Role;
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
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping()
    public String getAllUsers(Model model, UserFilterDto userFilterDto) {
        model.addAttribute("users", userService.getAllUsers(userFilterDto));
        return "adminPanel";
    }

    @GetMapping("/{user_id}")
    public String getUserById(@PathVariable (name = "user_id") Long userId, Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("user", userService.findUserById(userId));
        model.addAttribute("roles", Role.values());
        model.addAttribute("authRole", userDetails.getAuthorities()
                .stream().filter(role -> role==Role.ADMIN).findAny().orElse(null));
        model.addAttribute("authEmail", userDetails.getUsername());
        return "user";
    }

    @PostMapping()
    public String createUser(@ModelAttribute @Validated UserDto userDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {   //Хранит ошибки валидации
        redirectAttributes.addFlashAttribute("user", userDto);
        redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/registration";
        }
        userService.createUser(userDto);
        return "redirect:/login";
    }

    @PostMapping("{user_id}/updateByUser")
    public String editUserByUser(@PathVariable (name = "user_id") Long userId,
                            @ModelAttribute  (name = "user") @Validated  UserDto userDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", userDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/{user_id}";
        }

        userService.editUserByUser(userId, userDto);
        return "redirect:/users/{user_id}";
    }

    @PostMapping("{user_id}/updateByAdmin")
    public String editUserByAdmin(@PathVariable (name = "user_id") Long userId,
                           @ModelAttribute  (name = "user")   UserDto userDto) {

        userService.editUserByAdmin(userId, userDto);
        return "redirect:/users/{user_id}";
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{user_id}/delete")
    public String deleteUser(@PathVariable (name = "user_id") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/users";
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute UserDto userDto) {
        model.addAttribute("user", userDto);
        model.addAttribute("roles", Role.values());
       return "registrationPage";
    }
}




