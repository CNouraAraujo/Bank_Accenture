package com.accenture.academico.model;

import java.time.LocalDateTime;

import com.accenture.academico.model.enums.TipoOperacao;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class OperacoesCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private TipoOperacao tipoOperacao;
    private Double valor;
    private LocalDateTime dataOperacao;

}
