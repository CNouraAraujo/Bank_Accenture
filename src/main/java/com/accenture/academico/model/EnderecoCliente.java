package com.accenture.academico.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_endereco")
public class EnderecoCliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 200, nullable = false)
	private String logradouro;

	@Column(nullable = false)
	private int cep;

	@Column(nullable = false)
	private int numero;

	@Column(length = 300)
	private String complemento;

	@Column(length = 50, nullable = false)
	private String estado;

	@Column(length = 150, nullable = false)
	private String cidade;

}
