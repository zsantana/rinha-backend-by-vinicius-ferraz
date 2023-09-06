package org.acme;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import org.hibernate.annotations.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Cacheable
@RegisterForReflection
public class Pessoa extends PanacheEntityBase {

    public Pessoa() {}

    public static Pessoa of(PessoaDTO pessoaDTO) {
        Pessoa p = new Pessoa();
        p.id = pessoaDTO.id;
        p.apelido = pessoaDTO.apelido;
        p.nome = pessoaDTO.nome;
        p.nascimento = pessoaDTO.nascimento;
        p.stack = pessoaDTO.stack == null ? null : Arrays.toString(pessoaDTO.stack);
        return p;
    }

    @Id
    @Column(name = "publicID")
    public UUID id;

    public String apelido;

    public String nome;

    public LocalDate nascimento;

    public String stack;

    @Column(name = "BUSCA_TRGM")
    @Generated
    @JsonIgnore
    public String busca;
    
}
