package com.example.trabalho3.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho3.Models.Tarefa;
import com.example.trabalho3.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TarefasAdapter extends RecyclerView.Adapter<TarefasAdapter.TarefaViewHolder> {

    private List<Tarefa> listaTarefas;
    private SimpleDateFormat dateFormat;
    Context context;

    public TarefasAdapter(List<Tarefa> listaTarefas, Context context) {
        this.listaTarefas = listaTarefas;
        this.context = context;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_tarefa, parent, false);
        return new TarefaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        Tarefa tarefa = listaTarefas.get(position);
        holder.bind(tarefa);
    }

    @Override
    public int getItemCount() {
        return listaTarefas.size();
    }

    public class TarefaViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDescricao;
        private TextView textViewDataInicial;
        private TextView textViewDataFinal;
        private TextView textViewSituacao;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
            textViewDataInicial = itemView.findViewById(R.id.textViewDataInicial);
            textViewDataFinal = itemView.findViewById(R.id.textViewDataFinal);
            textViewSituacao = itemView.findViewById(R.id.textViewSituacao);
        }

        public void bind(Tarefa tarefa) {
            textViewDescricao.setText(tarefa.getDescricao());
            textViewDataInicial.setText("Data Inicial: " + dateFormat.format(tarefa.getDataInicial()));
            textViewDataFinal.setText("Data Final: " + dateFormat.format(tarefa.getDataFinal()));
            textViewSituacao.setText("Situação: " + tarefa.getSituacao());
        }
    }
}
