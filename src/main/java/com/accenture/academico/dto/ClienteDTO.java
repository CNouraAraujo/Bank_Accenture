package com.accenture.academico.dto;

public record ClienteDTO(String nome, String cpf, String telefone, String senha, Integer idAgencia,
		Integer idEndereco) {
}
