package leontev.webtasktracker.services.helpers;

import leontev.webtasktracker.entities.Project;
import leontev.webtasktracker.exсeptions.BadRequestException;
import leontev.webtasktracker.exсeptions.NotFoundExeption;
import leontev.webtasktracker.repositories.ProjectRepository;
import leontev.webtasktracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional
public class ServiceHelper {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Project getProjectOrThrowException(Long projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundExeption(
                                String.format(
                                        "Project with - %s doesn`t exist.", projectId)));
    }

    public void ifUserPresentThrowException(String email) {
    userRepository.findByEmail(email)
                .ifPresent(user -> {
            throw new BadRequestException(String.format("User with - %s  already exists.", email));
        });

    }

}
