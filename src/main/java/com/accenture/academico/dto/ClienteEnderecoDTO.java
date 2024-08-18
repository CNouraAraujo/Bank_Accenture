package com.accenture.academico.dto;

import java.util.ArrayList;
import java.util.List;

import com.accenture.academico.model.EnderecoCliente;
import com.accenture.academico.model.enums.TipoConta;

import lombok.Data;

@Data
public class ClienteEnderecoDTO {

	private String nome;
	private String cpf;
	private String telefone;
	private String senha;
	private EnderecoCliente endereco;
	private List<ContaDTO> contas = new ArrayList<>();

	public ClienteEnderecoDTO(String nome, String cpf, String telefone, String senha, EnderecoCliente endereco,
			List<ContaDTO> contas) {
		this.nome = nome;
		this.cpf = cpf;
		this.telefone = telefone;
		this.senha = senha;
		this.endereco = endereco;
		this.contas = contas;
	}

	public ClienteEnderecoDTO() {
	}

	@Data
	public static class ContaDTO {
		private Double saldo;
		private TipoConta tipo;
	}
}