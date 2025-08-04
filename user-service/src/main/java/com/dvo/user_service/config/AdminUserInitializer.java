package com.dvo.user_service.config;

import com.dvo.user_service.entity.RoleType;
import com.dvo.user_service.entity.User;
import com.dvo.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class AdminUserInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()){
            User admin = User.builder()
                    .username("admin")
                    .email("admin@mail.ru")
                    .password(passwordEncoder.encode("12345"))
                    .roleType(RoleType.ADMIN)
                    .build();

            userRepository.save(admin);
        }
    }
}
