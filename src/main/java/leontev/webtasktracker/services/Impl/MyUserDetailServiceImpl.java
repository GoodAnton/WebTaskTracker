package leontev.webtasktracker.services.Impl;

import leontev.webtasktracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyUserDetailServiceImpl implements UserDetailsService { //SpringSecurity - в class UserServiceImpl добавить не могу - будет зацикливание бинов. с SecurityConfiguration

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User( //сэтим поля в userdetails
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singleton(user.getRole()) // Коллекция, т.к. мб неск ролей
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user" + email));
    }

    public Long getIdByEmail(String email) {
        return userRepository.findByEmail(email).get().getUserId();
    }
}
