package com.example.trabalho3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trabalho3.Database.CategoriaDAO;
import com.example.trabalho3.Models.Categoria;

public class CategoriaActivity extends AppCompatActivity {

    private  int idCategoria;
    private EditText editTextCategoria;
    private Button buttonSalvar;

    private CategoriaDAO categoriaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        editTextCategoria = findViewById(R.id.editTextCategoria);
        buttonSalvar = findViewById(R.id.buttonSalvar);

        categoriaDAO = new CategoriaDAO(this);

        idCategoria = Integer.parseInt(getIntent().getExtras().getString("IdCategoria").toString());

        if(idCategoria > -1)
        {
            RecuperarCategoria();
        }
    }

    private void RecuperarCategoria()
    {
        Categoria categoria = categoriaDAO.getCategoriaById(idCategoria);

        if(categoria != null)
        {
             editTextCategoria.setText(categoria.getNome());
        }
    }


    public void SalvarCategoria(View v)
    {
        String nomeCategoria = editTextCategoria.getText().toString().trim();

        if (!nomeCategoria.isEmpty()) {

            Categoria categoria = new Categoria(0, nomeCategoria);

            if(idCategoria > 0)
            {
                categoria.setId(idCategoria);
                categoriaDAO.updateCategoria(categoria);
            }
            else
            {
                categoriaDAO.insertCategoria(categoria);
            }

            Toast.makeText(this, "Categoria salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Por favor, insira o nome da categoria", Toast.LENGTH_SHORT).show();
        }

    }

}