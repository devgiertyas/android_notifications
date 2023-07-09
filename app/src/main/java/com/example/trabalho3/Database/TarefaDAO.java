package com.example.trabalho3.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.trabalho3.Models.Categoria;
import com.example.trabalho3.Models.Imagem;
import com.example.trabalho3.Models.Tarefa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TarefaDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasktres.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CATEGORIA = "categorias";
    private static final String TABLE_TAREFA = "tarefa";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRICAO = "descricao";
    private static final String COLUMN_OBSERVACOES = "observacoes";
    private static final String COLUMN_DATA_INICIAL = "data_inicial";
    private static final String COLUMN_DATA_FINAL = "data_final";
    private static final String COLUMN_SITUACAO = "situacao";
    private static final String COLUMN_CATEGORIA_ID = "categoria_id";

    private static final String TABLE_TAREFA_IMAGEM = "tarefa_imagem";
    private static final String COLUMN_TAREFA_ID = "tarefa_id";
    private static final String COLUMN_IMAGEM_PATH = "imagem_path";
    private Context context;

    public TarefaDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableTarefa = "CREATE TABLE " + TABLE_TAREFA + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DESCRICAO + " TEXT, " +
                COLUMN_OBSERVACOES + " TEXT, " +
                COLUMN_DATA_INICIAL + " TEXT, " +
                COLUMN_DATA_FINAL + " TEXT, " +
                COLUMN_SITUACAO + " TEXT, " +
                COLUMN_CATEGORIA_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CATEGORIA_ID + ") REFERENCES " + TABLE_CATEGORIA + "(" + COLUMN_ID + ")" +
                ")";

        String createTableTarefaImagem = "CREATE TABLE " + TABLE_TAREFA_IMAGEM + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TAREFA_ID + " INTEGER, " +
                COLUMN_IMAGEM_PATH + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_TAREFA_ID + ") REFERENCES " + TABLE_TAREFA + "(" + COLUMN_ID + ")" +
                ")";

        db.execSQL(createTableTarefa);
        db.execSQL(createTableTarefaImagem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAREFA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAREFA_IMAGEM);
        onCreate(db);
    }

    public void salvarTarefa(Tarefa tarefa) {
        SQLiteDatabase db = getWritableDatabase();

        // Salvar os dados da tarefa na tabela "tarefa"
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRICAO, tarefa.getDescricao());
        values.put(COLUMN_OBSERVACOES, tarefa.getObservacoes());
        values.put(COLUMN_DATA_INICIAL, formatarData(tarefa.getDataInicial()));
        values.put(COLUMN_DATA_FINAL, formatarData(tarefa.getDataFinal()));
        values.put(COLUMN_SITUACAO, tarefa.getSituacao());
        values.put(COLUMN_CATEGORIA_ID, tarefa.getCategoria().getId());

        long idTarefa = db.insert(TABLE_TAREFA, null, values);

        // Salvar as imagens da tarefa na tabela "tarefa_imagem"
        for (Imagem imagem : tarefa.getImagens()) {
            ContentValues imagemValues = new ContentValues();
            imagemValues.put(COLUMN_TAREFA_ID, idTarefa);
            //imagemValues.put(COLUMN_IMAGEM_PATH, imagem.getBitmap());
            db.insert(TABLE_TAREFA_IMAGEM, null, imagemValues);
        }

        db.close();
    }

    public List<Tarefa> getTarefas() {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TAREFA;
        Cursor cursor = db.rawQuery(query, null);

        List<Tarefa> tarefas = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String descricao = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRICAO));
            String observacoes = cursor.getString(cursor.getColumnIndex(COLUMN_OBSERVACOES));
            Date dataInicial = parseData(cursor.getString(cursor.getColumnIndex(COLUMN_DATA_INICIAL)));
            Date dataFinal = parseData(cursor.getString(cursor.getColumnIndex(COLUMN_DATA_FINAL)));
            String situacao = cursor.getString(cursor.getColumnIndex(COLUMN_SITUACAO));
            int categoriaId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORIA_ID));

            // Obter a categoria da tarefa
            CategoriaDAO categoriaDAO = new CategoriaDAO(context);
            Categoria categoria = categoriaDAO.getCategoriaById(categoriaId);

            // Obter as imagens da tarefa
            List<String> imagens = getImagensByTarefaId(id);

            // Criar o objeto Tarefa com os dados do cursor
            Tarefa tarefa = new Tarefa(id, descricao, observacoes, dataInicial, dataFinal, situacao, categoria, imagens);

            tarefas.add(tarefa);
        }

        cursor.close();
        db.close();

        return tarefas;
    }

    private Date parseData(String dataString) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return format.parse(dataString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String formatarData(Date data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(data);
    }

    private List<String> getImagensByTarefaId(int tarefaId) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TAREFA_IMAGEM + " WHERE " + COLUMN_TAREFA_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(tarefaId)});

        List<String> imagens = new ArrayList<>();

        while (cursor.moveToNext()) {
            String imagemPath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGEM_PATH));
            imagens.add(imagemPath);
        }

        cursor.close();
        db.close();

        return imagens;
    }
}
