package com.example.trabalho3.Adapters;

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

import java.util.ArrayList;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder>{
    Context context;
    List<Categoria> categorias;

    public CategoriaAdapter(List<Categoria> categorias, Context context ){
        this.context = context;
        this.categorias = categorias;
    }

    @Override
    public CategoriaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = null;
        if (parent != null) {
            this.context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            cell = inflater.inflate(R.layout.item_categoria, parent, false);
        }
        return new ViewHolder(cell);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder != null){

            holder.textViewNomeCategoria.setText( categorias.get(holder.getAdapterPosition()).getNome());

            holder.textViewNomeCategoria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CategoriaActivity man = new  CategoriaActivity();

                    Intent it = new Intent(context.getApplicationContext(), CategoriaActivity.class);
                    String id = String.valueOf(categorias.get(holder.getAdapterPosition()).getId());
                    it.putExtra("IdCategoria",id);
                    context.startActivity(it);
                }
            });


        }
    }


    @Override
    public int getItemCount() {
        return categorias.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNomeCategoria ;

        public ViewHolder(View itemView) {
            super(itemView);

            if (itemView != null){
                this.textViewNomeCategoria = itemView.findViewById(R.id.textViewNomeCategoria);
            }

        }
    }
}
