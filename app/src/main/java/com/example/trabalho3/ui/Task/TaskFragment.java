package com.example.trabalho3.ui.Task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho3.Adapters.CategoriaAdapter;
import com.example.trabalho3.Adapters.TarefasAdapter;
import com.example.trabalho3.Database.CategoriaDAO;
import com.example.trabalho3.Database.TarefaDAO;
import com.example.trabalho3.Models.Tarefa;
import com.example.trabalho3.R;
import com.example.trabalho3.databinding.FragmentGalleryBinding;

import java.util.List;

public class TaskFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private List<Tarefa> listaTarefas;
    private TarefaDAO tarefaDAO;

    private TarefasAdapter tarefasAdapter;

    private RecyclerView recyclerViewTarefas;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerViewTarefas = root.findViewById(R.id.recyclerViewTarefas);
        recyclerViewTarefas.setLayoutManager(new LinearLayoutManager(getActivity()));

        CarregarTarefas();

        return root;
    }

    private  void CarregarTarefas()
    {

        tarefaDAO = new TarefaDAO(getContext());
        listaTarefas = tarefaDAO.getTarefas();

        tarefasAdapter = new TarefasAdapter(listaTarefas, getActivity());
        recyclerViewTarefas.setAdapter(tarefasAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        CarregarTarefas();
    }

}