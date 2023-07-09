package com.example.trabalho3.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho3.Models.Imagem;
import com.example.trabalho3.R;

import java.util.List;

public class ImagemAdapter extends RecyclerView.Adapter<ImagemAdapter.ImagemViewHolder> {

    private List<Imagem> listaImagens;
    private OnDeleteClickListener onDeleteClickListener;

    public ImagemAdapter(List<Imagem> listaImagens) {
        this.listaImagens = listaImagens;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ImagemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagem, parent, false);
        return new ImagemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagemViewHolder holder, int position) {
        Imagem imagem = listaImagens.get(position);
        holder.bind(imagem);
    }

    @Override
    public int getItemCount() {
        return listaImagens.size();
    }

    public class ImagemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton buttonDelete;

        public ImagemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewImagem);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteImagem);
        }

        public void bind(Imagem imagem) {
            imageView.setImageBitmap(imagem.getBitmap());

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(position);
                    }
                }
            });
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}
