package leontev.webtasktracker.repositories;

import leontev.webtasktracker.dto.UserFilterDto;
import leontev.webtasktracker.entities.User;
import java.util.List;


public interface FilterRepository {

    List<User> findAllUsersByFilter(UserFilterDto userFilterDto);
}
