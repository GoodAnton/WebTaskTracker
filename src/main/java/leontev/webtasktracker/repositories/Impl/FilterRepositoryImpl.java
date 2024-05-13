package leontev.webtasktracker.repositories.Impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import leontev.webtasktracker.dto.UserFilterDto;
import leontev.webtasktracker.entities.User;
import leontev.webtasktracker.repositories.FilterRepository;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor

public class FilterRepositoryImpl implements FilterRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllUsersByFilter(UserFilterDto userFilterDto) {
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);

        var user =criteria.from(User.class);
        criteria.select(user);

        List<Predicate> predicates = new ArrayList<>();  // из библиотеки jakarta persistence
        if (userFilterDto.getNickName() != null && !userFilterDto.getNickName().isBlank()) {
            predicates.add(cb.like(user.get("nickName"), userFilterDto.getNickName()));
        }
        if (userFilterDto.getEmail() != null && !userFilterDto.getEmail().isBlank()) {
            predicates.add(cb.like(user.get("email"), userFilterDto.getEmail()));
        }

        criteria.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(criteria).getResultList();
    }

    }
