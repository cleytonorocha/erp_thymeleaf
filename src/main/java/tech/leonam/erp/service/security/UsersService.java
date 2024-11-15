package tech.leonam.erp.service.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import tech.leonam.erp.controller.security.AccountCredentialsVO;
import tech.leonam.erp.controller.security.LoginResponseDTO;
import tech.leonam.erp.controller.security.RegisterDTO;
import tech.leonam.erp.exceptions.UserPresentException;
import tech.leonam.erp.model.entity.Users;
import tech.leonam.erp.repository.UsersRepository;

@Log4j2
@Service
@RequiredArgsConstructor
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<LoginResponseDTO> login(AccountCredentialsVO accountCredentialsVO) {
        log.info("Iniciando login para o usuário: {}", accountCredentialsVO.getLogin());  // Log de entrada
        
        try {
            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                    accountCredentialsVO.getLogin(),
                    accountCredentialsVO.getPassword());
            Authentication auth = this.authenticationManager.authenticate(usernamePassword);
            String token = jwtService.generateToken((Users) auth.getPrincipal());

            log.info("Login bem-sucedido para o usuário: {}", accountCredentialsVO.getLogin()); // Log de sucesso
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            log.error("Erro no login para o usuário: {}", accountCredentialsVO.getLogin(), e); // Log de erro
            throw new UsernameNotFoundException("User or password not found");
        }
    }

    public ResponseEntity<LoginResponseDTO> register(RegisterDTO registerDTO) {
        log.info("Registrando novo usuário com o login: {}", registerDTO.login());

        if (usersRepository.existsByLogin(registerDTO.login())) {
            log.warn("Tentativa de registro de usuário com login já existente: {}", registerDTO.login());  // Log de tentativa inválida
            throw new UserPresentException();
        }

        String password = new BCryptPasswordEncoder().encode(registerDTO.password());
        Users user = Users.builder()
                .login(registerDTO.login())
                .password(password)
                .role(registerDTO.role())
                .build();

        usersRepository.save(user);

        log.info("Usuário registrado com sucesso: {}", registerDTO.login());  // Log de sucesso
        String token = jwtService.generateToken(user);
        
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    public List<Users> listAll() {
        log.info("Listando todos os usuários.");
        List<Users> usersList = usersRepository.findAll();
        log.info("Total de usuários encontrados: {}", usersList.size());
        return usersList;
    }
}
