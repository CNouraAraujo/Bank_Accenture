package com.accenture.academico.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false)
    private String nome;

    @Column(name = "cpf", length = 20, nullable = false)
    private String cpf;

    @Column(length = 20, nullable = false)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "id_agencia", nullable = false)
    @JsonBackReference
    private Agencia agencia;

    @OneToOne
    @JoinColumn(name = "id_endereco", nullable = false)
    private EnderecoCliente enderecoCliente;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ContaBancaria> contaBancarias = new ArrayList<>();

}
