package com.example.trabalho3.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho3.Adapters.CategoriaAdapter;
import com.example.trabalho3.Database.CategoriaDAO;
import com.example.trabalho3.Models.Categoria;
import com.example.trabalho3.R;
import com.example.trabalho3.databinding.FragmentCategoriasBinding;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private RecyclerView recyclerViewCategorias;
    private CategoriaAdapter categoriaAdapter;
    private List<Categoria> categorias;
    private CategoriaDAO categoriaDAO;

    private FragmentCategoriasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCategoriasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerViewCategorias = root.findViewById(R.id.recyclerViewCategorias);
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(getActivity()));

        CarregarCategorias();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        CarregarCategorias();
    }

    private  void CarregarCategorias()
    {
        categoriaDAO = new CategoriaDAO(getActivity());
        categorias = categoriaDAO.getAllCategorias();

        categoriaAdapter = new CategoriaAdapter(categorias, getActivity());
        recyclerViewCategorias.setAdapter(categoriaAdapter);
    }


}