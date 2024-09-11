package tech.leonam.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;
import tech.leonam.erp.model.enums.UF;
import tech.leonam.erp.service.ClienteService;

@Controller
@AllArgsConstructor
public class ControleView {

    private final ClienteService clienteService;

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/")
    public String index(Model model) {
        return "home";
    }

    @GetMapping("/vendas")
    public String vendas(Model model) {
        return "vendas";
    }

    @GetMapping("/cadastro_clientes")
    public String cadastro_clientes(Model model) {
        model.addAttribute("estados", UF.values());
        return "cadastro_clientes";
    }

    @GetMapping("/atualizar_clientes")
    public String atualizar_clientes(Model model) {
        return "atualizar_clientes";
    }

    @GetMapping("/listar_clientes")
    public String listar_clientes(Model model, @PathVariable @RequestParam(defaultValue = "0") Integer pagina) {
        var consulta = clienteService.buscarTodosOsCLientes(pagina, 20, "id", "ASC");
        model.addAttribute("clientes", consulta.getContent());
        model.addAttribute("paginaCorrente", consulta.getNumber());
        model.addAttribute("totalPages", consulta.getTotalPages());
        return "listar_clientes";
    }

    @GetMapping("/deletar_clientes")
    public String deletar_clientes(Model model) {
        return "deletar_clientes";
    }

    @GetMapping("/sidebar")
    public String sidebar(Model model) {
        return "sidebar";
    }

}
