package tech.leonam.erp.service.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import tech.leonam.erp.controller.security.AccountCredentialsVO;
import tech.leonam.erp.controller.security.LoginResponseDTO;
import tech.leonam.erp.controller.security.RegisterDTO;
import tech.leonam.erp.exceptions.UserPresentException;
import tech.leonam.erp.model.entity.Users;
import tech.leonam.erp.repository.UsersRepository;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<LoginResponseDTO> login(AccountCredentialsVO accountCredentialsVO) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                accountCredentialsVO.getLogin(),
                accountCredentialsVO.getPassword());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = jwtService.generateToken((Users) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));

    }

    public ResponseEntity<LoginResponseDTO> register(RegisterDTO registerDTO) {
        if (usersRepository.existsByLogin(registerDTO.login())) {
            throw new UserPresentException();
        }
        String password = new BCryptPasswordEncoder().encode(registerDTO.password());
        Users user = Users.builder()
                .login(registerDTO.login())
                .password(password)
                .role(registerDTO.role())
                .build();
        usersRepository.save(user);
        return ResponseEntity.ok().body(new LoginResponseDTO(user.getPassword()));
    }

    public List<Users> listAll() {
        return usersRepository.findAll();
    }
}