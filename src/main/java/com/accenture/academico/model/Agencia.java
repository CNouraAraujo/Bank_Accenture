package com.accenture.academico.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_agencia")
public class Agencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idAgencia;

	@Column(length = 45, nullable = false)
	private String nome;

	@Column(length = 15, nullable = false)
	private String telefone;

	@OneToMany(mappedBy = "agencia")
	@JsonManagedReference
	private List<Cliente> clientes;

	@Column(length = 100, nullable = false) // Novo campo endereço
	private String endereco;

	@Column(length = 10, nullable = false, unique = true) // Novo campo numeroAgencia
	private Integer numeroAgencia;

	public Agencia() {
	}

	public Agencia(Integer codigoBanco) {
		this.idAgencia = codigoBanco;
	}

	@PrePersist
	private void prePersist() {
		if (numeroAgencia == null) {
			numeroAgencia = generateNumeroAgencia();
		}
	}

	private Integer generateNumeroAgencia() {
		return (int) (Math.random() * 10000); // Ajuste conforme necessário
	}

}
