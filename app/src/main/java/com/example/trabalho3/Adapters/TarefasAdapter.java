package com.example.trabalho3.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho3.CategoriaActivity;
import com.example.trabalho3.Models.Categoria;
import com.example.trabalho3.Models.Tarefa;
import com.example.trabalho3.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho3.CategoriaActivity;
import com.example.trabalho3.MainActivity;
import com.example.trabalho3.Models.Categoria;
import com.example.trabalho3.R;
import com.example.trabalho3.TarefaActivity;

import java.util.ArrayList;
import java.util.List;

public class TarefasAdapter extends RecyclerView.Adapter<TarefasAdapter.ViewHolder>{
    private List<Tarefa> listaTarefas;
    private SimpleDateFormat dateFormat;
    Context context;

    public TarefasAdapter(List<Tarefa> listaTarefas, Context context ){
        this.listaTarefas = listaTarefas;
        this.context = context;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    @Override
    public TarefasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = null;
        if (parent != null) {
            this.context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            cell = inflater.inflate(R.layout.item_tarefa, parent, false);
        }
        return new ViewHolder(cell);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder != null){


            holder.textViewDescricao.setText(listaTarefas.get(holder.getAdapterPosition()).getDescricao());
            holder.textViewSituacao.setText("Situação: " + listaTarefas.get(holder.getAdapterPosition()).getSituacao());

            if (listaTarefas.get(holder.getAdapterPosition()).getDataInicial() != null) {
                holder.textViewDataInicial.setText("Data Inicial: " + dateFormat.format(listaTarefas.get(holder.getAdapterPosition()).getDataInicial()));
            } else {
                holder.textViewDataInicial.setText("Data Inicial: N/A");
            }

            if (listaTarefas.get(holder.getAdapterPosition()).getDataFinal() != null) {
                holder.textViewDataFinal.setText("Data Final: " + dateFormat.format(listaTarefas.get(holder.getAdapterPosition()).getDataFinal()));
            } else {
                holder.textViewDataFinal.setText("Data Final: N/A");
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TarefaActivity man = new  TarefaActivity();

                    Intent it = new Intent(context.getApplicationContext(), TarefaActivity.class);
                    String id = String.valueOf(listaTarefas.get(holder.getAdapterPosition()).getId());
                    it.putExtra("idTarefa",id);
                    context.startActivity(it);
                }
            });


        }
    }


    @Override
    public int getItemCount() {
        return listaTarefas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewDescricao;
        private TextView textViewDataInicial;
        private TextView textViewDataFinal;
        private TextView textViewSituacao;

        public ViewHolder(View itemView) {
            super(itemView);

            if (itemView != null){
                textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
                textViewDataInicial = itemView.findViewById(R.id.textViewDataInicial);
                textViewDataFinal = itemView.findViewById(R.id.textViewDataFinal);
                textViewSituacao = itemView.findViewById(R.id.textViewSituacao);
            }

        }
    }
}




