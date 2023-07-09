package com.example.trabalho3;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho3.Adapters.CategoriaSpinnerAdapter;
import com.example.trabalho3.Adapters.ImagemAdapter;
import com.example.trabalho3.Database.CategoriaDAO;
import com.example.trabalho3.Models.Categoria;
import com.example.trabalho3.Models.Imagem;
import com.example.trabalho3.Models.Tarefa;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TarefaActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 3;

    private EditText editTextDescricao;
    private Spinner spinnerCategoria;
    private EditText editTextDataInicial;
    private EditText editTextDataFinal;
    private EditText editTextObservacoes;
    private RecyclerView recyclerViewImagens;
    private Button buttonSalvar;
    private Button buttonAdicionarImagem;

    private ImagemAdapter imagemAdapter;
    private List<Imagem> listaImagens;

    private List<Categoria> listaCategorias;
    private CategoriaDAO categoriaDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        editTextDescricao = findViewById(R.id.editTextDescricao);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        editTextObservacoes = findViewById(R.id.editTextObservacoes);
        recyclerViewImagens = findViewById(R.id.recyclerViewImagens);
        buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonAdicionarImagem = findViewById(R.id.buttonAdicionarImagem);

        listaImagens = new ArrayList<>();
        imagemAdapter = new ImagemAdapter(listaImagens);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewImagens.setLayoutManager(layoutManager);
        recyclerViewImagens.setAdapter(imagemAdapter);

        imagemAdapter.setOnDeleteClickListener(new ImagemAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removerImagemDoRecyclerView(position);
            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTarefa();
            }
        });

        buttonAdicionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSelecionarImagemDialog();
            }
        });

        EditText editTextDataInicial = findViewById(R.id.editTextDataInicial);
        editTextDataInicial.setInputType(InputType.TYPE_NULL);
        editTextDataInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(editTextDataInicial);
            }
        });

        EditText editTextDataFinal = findViewById(R.id.editTextDataFinal);
        editTextDataFinal.setInputType(InputType.TYPE_NULL);
        editTextDataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(editTextDataFinal);
            }
        });

        CarregarCategorias();

    }

    private void CarregarCategorias()
    {
        categoriaDAO = new CategoriaDAO(this);
        listaCategorias = categoriaDAO.getAllCategorias();

        CategoriaSpinnerAdapter categoriaAdapter = new CategoriaSpinnerAdapter(this, listaCategorias);
        spinnerCategoria.setAdapter(categoriaAdapter);

    }


    private void abrirSelecionarImagemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecionar Imagem");
        String[] options = {"Câmera", "Galeria"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    abrirCamera();
                } else if (which == 1) {
                    abrirGaleria();
                }
            }
        });
        builder.show();
    }

    private void abrirCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        }
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                adicionarImagemAoRecyclerView(imageBitmap);
            } else if (requestCode == REQUEST_GALLERY) {
                Uri imageUri = data.getData();
                Bitmap imageBitmap = null;
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                adicionarImagemAoRecyclerView(imageBitmap);
            }
        }
    }

    private void adicionarImagemAoRecyclerView(Bitmap imageBitmap) {
        Imagem imagem = new Imagem(imageBitmap);
        listaImagens.add(imagem);
        imagemAdapter.notifyDataSetChanged();
    }

    private void removerImagemDoRecyclerView(int position) {
        listaImagens.remove(position);
        imagemAdapter.notifyDataSetChanged();
    }

    private void salvarTarefa() {
        String descricao = editTextDescricao.getText().toString().trim();

        if(descricao == null || descricao.trim().isEmpty())
        {
            Toast.makeText(this,"Informe a descrição!", Toast.LENGTH_SHORT).show();
            return;
        }

        Categoria categoriaSelecionada = (Categoria) spinnerCategoria.getSelectedItem();

        if(categoriaSelecionada == null)
        {
            Toast.makeText(this,"Selecione uam categoria!", Toast.LENGTH_SHORT).show();
            return;
        }

        String observacoes = editTextObservacoes.getText().toString().trim();

        String dataInicial = editTextDataInicial.getText().toString().trim();
        String dataFinal = editTextDataFinal.getText().toString().trim();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date dataInicialParsed = null;
        Date dataFinalParsed = null;

        try {
            dataInicialParsed = dateFormat.parse(dataInicial);
            dataFinalParsed = dateFormat.parse(dataFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(dataInicialParsed == null || dataFinalParsed == null)
        {
            Toast.makeText(this,"Informe a data inicial e final!", Toast.LENGTH_SHORT).show();
            return;
        }

        Tarefa tarefa = new Tarefa(
                descricao, observacoes,
                dataInicialParsed,
                dataFinalParsed,
                "teste", categoriaSelecionada );

        /* Adicione a lista de imagens da tarefa (caminhos dos arquivos)
        List<String> imagens = imagemAdapter.getImagens();
        tarefa.setImagens(imagens);

        // Crie uma instância do TarefaDAO
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        // Salve a tarefa no banco de dados
        long idTarefa = tarefaDAO.salvarTarefa(tarefa);


        if (idTarefa != -1) {
            Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar tarefa!", Toast.LENGTH_SHORT).show();
        }*/

        Toast.makeText(this,"Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamera();
            } else {
                Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showDateTimePicker(final EditText editText) {
        Calendar calendar = Calendar.getInstance();

        // Obtenha a data atual
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crie o DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Aqui você pode formatar a data selecionada conforme necessário
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);

                // Obtenha a hora atual
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Crie o TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(TarefaActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Aqui você pode formatar a hora selecionada conforme necessário
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);

                        // Combinar data e hora selecionadas
                        String selectedDateTime = selectedDate + " " + selectedTime;

                        editText.setText(selectedDateTime);
                    }
                }, hourOfDay, minute, true);

                timePickerDialog.show();
            }
        }, year, month, day);

        datePickerDialog.show();
    }



}
