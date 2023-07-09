package com.example.trabalho3.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trabalho3.Models.Categoria;
import com.example.trabalho3.R;

import java.util.List;

public class CategoriaSpinnerAdapter  extends ArrayAdapter<Categoria> {

    public CategoriaSpinnerAdapter(Context context, List<Categoria> categorias) {
        super(context, 0, categorias);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Categoria categoria = getItem(position);
        if (categoria != null) {
            textView.setText(categoria.getNome());
        }

        return convertView;
    }
}
