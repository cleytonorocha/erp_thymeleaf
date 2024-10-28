package tech.leonam.erp.controller.security;

import lombok.Builder;

@Builder
public record LoginResponseDTO(String token) {}
