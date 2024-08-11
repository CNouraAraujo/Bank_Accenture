package com.accenture.academico.model;

import jakarta.persistence.*;

import com.accenture.academico.model.enums.TipoOperacao;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_operacao")
public class Operacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_hora_movimento", nullable = false)
    private LocalDateTime dataHoraMovimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_operacao", length = 15, nullable = false)
    private TipoOperacao tipoOperacao;

    @ManyToOne
    @JoinColumn(name = "id_conta_origem", nullable = false)
    private ContaBancaria contaOrigem;

}
