package io.jaziu.springauthorizationserver;


import io.jaziu.springauthorizationserver.model.Role;
import io.jaziu.springauthorizationserver.model.RoleName;
import io.jaziu.springauthorizationserver.model.User;
import io.jaziu.springauthorizationserver.repository.RoleRepository;
import io.jaziu.springauthorizationserver.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DbInit implements CommandLineRunner {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;

    public DbInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {



        roleRepository.save(new Role(RoleName.ROLE_USER));
        roleRepository.save(new Role(RoleName.ROLE_ADMIN));
        roleRepository.save(new Role(RoleName.ROLE_MODERATOR));

        User admin = new User("admin",encoder.encode("admin123"),"adminemail@gmail.com");
        User user = new User("user",encoder.encode("user123"),"useremail@gmail.com");
        User moderator = new User("moderator",encoder.encode("moderator123"),"moderator@gmail.com");
        userRepository.save(admin);
        userRepository.save(user);
        userRepository.save(moderator);



        Set<Role> adminRoles = new HashSet<>();
        Set<Role> userRoles = new HashSet<>();
        Set<Role> moderatorRoles = new HashSet<>();

        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Admin Role not find."));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));

        Role moderatorRole = roleRepository.findByName(RoleName.ROLE_MODERATOR)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: Moderator Role not find."));

        adminRoles.add(userRole);
        adminRoles.add(adminRole);
        userRoles.add(userRole);
        moderatorRoles.add(userRole);
        moderatorRoles.add(moderatorRole);

        admin.setEnabled(true);
        user.setEnabled(true);
        moderator.setEnabled(true);

        admin.setRoles(adminRoles);
        userRepository.save(admin);

        user.setRoles(userRoles);
        userRepository.save(user);

        moderator.setRoles(moderatorRoles);
        userRepository.save(moderator);

    }
}
