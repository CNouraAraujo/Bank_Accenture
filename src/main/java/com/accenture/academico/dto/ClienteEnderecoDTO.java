package com.accenture.academico.dto;

import java.util.ArrayList;
import java.util.List;

import com.accenture.academico.model.EnderecoCliente;

import lombok.Data;

@Data
public class ClienteEnderecoDTO {

    private String nome;
    private String cpf;
    private String telefone;
    private String senha;
    private EnderecoCliente endereco;
    private List<ContaDTO> contas = new ArrayList<>();

    @Data
    public static class ContaDTO {
        private Double saldo;
        private String tipo;
    }
}



//package com.accenture.academico.dto;
//
//import java.util.List;
//
//import com.accenture.academico.model.Conta;
//import com.accenture.academico.model.EnderecoCliente;
//
//import lombok.Data;
//
//@Data
//public class ClienteEnderecoDTO {
//
//	private String nome;
//	private String cpf;
//	private String telefone;
//	private EnderecoCliente endereco;
//	private Conta conta;
//
//	@Data
//	public static class ContaDTO {
//		private Double saldo;
//		private String tipo;
//	}
//}
