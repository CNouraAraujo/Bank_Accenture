package com.accenture.academico.dto;

import java.time.LocalDateTime;

import com.accenture.academico.model.enums.TipoOperacao;

import lombok.Data;

@Data
public class OperacoesClienteDTO {

    private Integer id;
    private TipoOperacao tipoOperacao;
    private Double valor;
    private LocalDateTime dataOperacao;
    private ClienteResumoDTO cliente;

    @Data
    public static class ClienteResumoDTO {
        private Integer id;
        private String nome;
        private String cpf;
    }
}
