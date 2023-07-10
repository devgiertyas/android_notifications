package com.example.trabalho3.Models;

import java.util.Date;
import java.util.List;

public class Tarefa {
    private int id;
    private String descricao;
    private String observacoes;
    private Date dataInicial;
    private Date dataFinal;
    private String situacao;
    private Categoria categoria;
    private List<Imagem> imagens;

    public Tarefa(int id, String descricao, String observacoes, Date dataInicial, Date dataFinal, String situacao, Categoria categoria, List<Imagem> imagens) {
        this.id = id;
        this.descricao = descricao;
        this.observacoes = observacoes;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.situacao = situacao;
        this.categoria = categoria;
        this.imagens = imagens;
    }

    public Tarefa(String descricao, String observacoes, Date dataInicial, Date dataFinal, String situacao, Categoria categoria, List<Imagem> imagens) {
        this.descricao = descricao;
        this.observacoes = observacoes;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.situacao = situacao;
        this.categoria = categoria;
        this.imagens = imagens;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public  Tarefa()
    {

    }


    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public String getSituacao() {
        return situacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public List<Imagem> getImagens() {
        return imagens;
    }

    public void setImagens(List<Imagem> imagens) {
        this.imagens = imagens;
    }
}

