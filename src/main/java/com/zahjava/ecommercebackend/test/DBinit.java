package com.zahjava.ecommercebackend.test;

import com.zahjava.ecommercebackend.model.AuthModel.Role;
import com.zahjava.ecommercebackend.model.AuthModel.User;
import com.zahjava.ecommercebackend.repository.RoleRepository;
import com.zahjava.ecommercebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class DBinit {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${login.username}")
    private String username;
    @Value("${login.password}")
    private String password;

    public DBinit(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void runtimeCreateUser() {
        String roleName = "ROLE_ADMIN";
        int roleExistCount = roleRepository.countByRoleNameAndIsActiveTrue(roleName);
        Role role = null;
        if (roleExistCount > 0) {
            role = roleRepository.findByRoleNameAndIsActiveTrue(roleName);
        } else {
            role = new Role();
            role.setRoleName(roleName);
            // role = roleRepository.save(role);
        }
        User user = userRepository.findByUsernameAndIsActiveTrue(username);
        if (user == null) {
            user = new User();
            user.setFirstName("Golam");
            user.setLastname("Kibria");
            user.setEmail("golamkibria.java@gmail.com");
            user.setUsername(username);
            user.setMobileNo("01531921892");
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setRoles(Arrays.asList(role));
        user = userRepository.save(user);
    }
}
