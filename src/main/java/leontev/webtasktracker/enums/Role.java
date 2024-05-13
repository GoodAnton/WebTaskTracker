package leontev.webtasktracker.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority { //Даем понять SpringSecurity что является ролями
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
