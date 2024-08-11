package com.accenture.academico.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_agencia")
public class Agencia {

    @Id
    private Integer idAgencia;

    @Column(length = 45, nullable = false)
    private String nome;

    @Column(length = 15, nullable = false)
    private String telefone;

    @OneToMany(mappedBy = "agencia")
    @JsonManagedReference
    private List<Cliente> clientes;

    public Agencia() {}

    public Agencia(Integer codigoBanco) {
        this.idAgencia = codigoBanco;
    }
    
}
