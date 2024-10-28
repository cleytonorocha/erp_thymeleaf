package tech.leonam.erp.controller.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import tech.leonam.erp.model.entity.Users;
import tech.leonam.erp.service.security.UsersService;

@Log4j2
@Controller
@RequestMapping("/auth")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping
    public ResponseEntity<List<Users>> findAll() {
        List<Users> users = usersService.listAll();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterDTO data) {
        ResponseEntity<LoginResponseDTO> response = usersService.register(data);
        log.info("Usuário cadastrado!");
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AccountCredentialsVO accountCredentialsVO) {
        return usersService.login(accountCredentialsVO);
    }

}
