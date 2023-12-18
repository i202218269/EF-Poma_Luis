package com.company.springframework.model;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "tbl_empleados")
@Entity
@Getter
@Setter

public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column(precision = 10, scale = 2)
    private BigDecimal salario;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha_ingreso;

    @ManyToOne(fetch = FetchType.EAGER)
    private Departamento departamento;


}
