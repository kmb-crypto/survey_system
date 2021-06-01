package main.service;

import main.api.request.LoginRequest;
import main.api.response.LoginResponse;
import main.dto.LoginResponseUserDto;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginContext;
import java.util.Optional;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(final PasswordEncoder passwordEncoder,
                       final UserRepository userRepository,
                       final AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse getLoginResponse(final LoginRequest loginRequest) {
        String name = loginRequest.getName();
        String password = loginRequest.getPassword();

        Optional<User> optionalUser = userRepository.findByName(name);
        if (optionalUser.isEmpty()
                || !passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            return new LoginResponse();
        } else {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(name, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new LoginResponse(true, user2LoginResponseUserDto(optionalUser.get()));
        }

    }

    private LoginResponseUserDto user2LoginResponseUserDto(User currentUser) {
        LoginResponseUserDto loginResponseUserDto = new LoginResponseUserDto();
        loginResponseUserDto.setId(currentUser.getId());
        loginResponseUserDto.setName(currentUser.getName());
        loginResponseUserDto.setModeration(currentUser.isModerator());
        return loginResponseUserDto;
    }
}

