package com.example.trabalho3.Models;

public class Tarefa_Imagem {
    private int id;
    private int tarefaId;
    private String imagemPath;

    public Tarefa_Imagem(int id, int tarefaId, String imagemPath) {
        this.id = id;
        this.tarefaId = tarefaId;
        this.imagemPath = imagemPath;
    }

    public int getId() {
        return id;
    }

    public int getTarefaId() {
        return tarefaId;
    }

    public String getImagemPath() {
        return imagemPath;
    }
}

