package com.accenture.academico.dto;

import java.util.List;

import com.accenture.academico.model.EnderecoCliente;
import com.accenture.academico.model.enums.TipoConta;

import lombok.Data;

@Data
public class ExtratoDTO {

    private ClienteEnderecoDTO cliente;
    private List<OperacoesClienteDTO> operacoes;

    @Data
    public static class ClienteEnderecoDTO {
        private String nome;
        private String cpf;
        private String telefone;
        private String senha;
        private EnderecoCliente endereco;
        private List<ContaDTO> contas;

        @Data
        public static class ContaDTO {
            private Double saldo;
            private TipoConta tipo;
        }
    }
}
