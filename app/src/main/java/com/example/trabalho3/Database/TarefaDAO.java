package com.example.trabalho3.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.trabalho3.Models.Categoria;
import com.example.trabalho3.Models.Imagem;
import com.example.trabalho3.Models.Tarefa;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TarefaDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasktres.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_CATEGORIA = "categorias";
    private static final String TABLE_TAREFA = "tarefa";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRICAO = "descricao";
    private static final String COLUMN_OBSERVACOES = "observacoes";
    private static final String COLUMN_DATA_INICIAL = "data_inicial";
    private static final String COLUMN_DATA_FINAL = "data_final";
    private static final String COLUMN_SITUACAO = "situacao";
    private static final String COLUMN_CATEGORIA_ID = "categoria_id";

    private static final String COLUMN_NOTIFICACAO = "is_notificado";
    private static final String TABLE_TAREFA_IMAGEM = "tarefa_imagem";
    private static final String COLUMN_TAREFA_ID = "tarefa_id";
    private static final String COLUMN_IMAGEM_DATA = "imagem_data";
    private static final String COLUMN_NOME = "nome";

    private Context context;

    public TarefaDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableCategoria = "CREATE TABLE " + TABLE_CATEGORIA + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME + " TEXT" +
                ")";

        String createTableTarefa = "CREATE TABLE " + TABLE_TAREFA + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DESCRICAO + " TEXT, " +
                COLUMN_OBSERVACOES + " TEXT, " +
                COLUMN_DATA_INICIAL + " TEXT, " +
                COLUMN_DATA_FINAL + " TEXT, " +
                COLUMN_SITUACAO + " TEXT, " +
                COLUMN_NOTIFICACAO + " INTEGER DEFAULT 0, " +
                COLUMN_CATEGORIA_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CATEGORIA_ID + ") REFERENCES " + TABLE_CATEGORIA + "(" + COLUMN_ID + ")" +
                ")";

        String createTableTarefaImagem = "CREATE TABLE " + TABLE_TAREFA_IMAGEM + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TAREFA_ID + " INTEGER, " +
                COLUMN_IMAGEM_DATA + " BLOB, " +
                "FOREIGN KEY(" + COLUMN_TAREFA_ID + ") REFERENCES " + TABLE_TAREFA + "(" + COLUMN_ID + ")" +
                ")";

        db.execSQL(createTableCategoria);
        db.execSQL(createTableTarefa);
        db.execSQL(createTableTarefaImagem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAREFA_IMAGEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAREFA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIA);
        onCreate(db);
    }

    public int salvarTarefa(Tarefa tarefa) {
        SQLiteDatabase db = getWritableDatabase();

        // Salvar os dados da tarefa na tabela "tarefa"
        ContentValues tarefaValues = new ContentValues();
        tarefaValues.put(COLUMN_DESCRICAO, tarefa.getDescricao());
        tarefaValues.put(COLUMN_OBSERVACOES, tarefa.getObservacoes());
        tarefaValues.put(COLUMN_DATA_INICIAL, formatarData(tarefa.getDataInicial()));
        tarefaValues.put(COLUMN_DATA_FINAL, formatarData(tarefa.getDataFinal()));
        tarefaValues.put(COLUMN_SITUACAO, tarefa.getSituacao());
        tarefaValues.put(COLUMN_CATEGORIA_ID, tarefa.getCategoria().getId());

        int idTarefa = (int) db.insert(TABLE_TAREFA, null, tarefaValues);

        // Salvar as imagens da tarefa na tabela "tarefa_imagem"
        for (Imagem imagem : tarefa.getImagens()) {
            byte[] imagemData = convertBitmapToByteArray(imagem.getBitmap());

            ContentValues imagemValues = new ContentValues();
            imagemValues.put(COLUMN_TAREFA_ID, idTarefa);
            imagemValues.put(COLUMN_IMAGEM_DATA, imagemData);

            db.insert(TABLE_TAREFA_IMAGEM, null, imagemValues);
        }

        db.close();

        return  idTarefa;
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

            CategoriaDAO categoriaDAO = new CategoriaDAO(context);
            Categoria categoria = categoriaDAO.getCategoriaById(categoriaId);

            List<Imagem> imagens = getImagensByTarefaId(id);

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

    private List<Imagem> getImagensByTarefaId(int tarefaId) {
        List<Imagem> imagens = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TAREFA_IMAGEM + " WHERE " + COLUMN_TAREFA_ID + " = ?";
        String[] selectionArgs = {String.valueOf(tarefaId)};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                byte[] imagemData = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGEM_DATA));
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));

                Bitmap bitmap = BitmapFactory.decodeByteArray(imagemData, 0, imagemData.length);
                Imagem imagem = new Imagem(bitmap,id);
                imagens.add(imagem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return imagens;
    }

    public List<Tarefa> consultarTarefasVencidas() {
        SQLiteDatabase db = getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dataAtual = dateFormat.format(new Date());

        String query = "SELECT " +
                COLUMN_ID + ", " +
                COLUMN_DESCRICAO + ", " +
                COLUMN_DATA_FINAL +
                " FROM " + TABLE_TAREFA +
                " WHERE " + COLUMN_DATA_FINAL + " < '" + dataAtual + "'"+
                " AND " + COLUMN_NOTIFICACAO + " = 0";


        Cursor cursor = db.rawQuery(query, null);

        List<Tarefa> tarefas = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String descricao = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRICAO));
            String dataFinal = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_FINAL));

            Tarefa tarefa = new Tarefa();
            tarefa.setId(id);
            tarefa.setDescricao(descricao);

            try {
                Date dataFinalDate = dateFormat.parse(dataFinal);
                tarefa.setDataFinal(dataFinalDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            tarefas.add(tarefa);
        }

        cursor.close();
        db.close();

        return tarefas;
    }

    public void alterarStatusNotificacao(int idTarefa, boolean notificacaoAtiva) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICACAO, notificacaoAtiva ? 1 : 0);

        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(idTarefa)};

        db.update(TABLE_TAREFA, values, whereClause, whereArgs);

        db.close();
    }

    public Tarefa getTarefaById(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TAREFA +
                " WHERE " + COLUMN_ID + " = " + id;

        Cursor cursor = db.rawQuery(query, null);

        Tarefa tarefa = null;

        if (cursor.moveToFirst()) {
            int tarefaId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String descricao = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRICAO));
            String observacoes = cursor.getString(cursor.getColumnIndex(COLUMN_OBSERVACOES));
            String dataInicialString = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_INICIAL));
            String dataFinalString = cursor.getString(cursor.getColumnIndex(COLUMN_DATA_FINAL));
            String situacao = cursor.getString(cursor.getColumnIndex(COLUMN_SITUACAO));
            int categoriaId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORIA_ID));

            CategoriaDAO categoriaDAO = new CategoriaDAO(context);
            Categoria categoria = categoriaDAO.getCategoriaById(categoriaId);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date dataInicial = null;
            Date dataFinal = null;
            try {
                dataInicial = dateFormat.parse(dataInicialString);
                dataFinal = dateFormat.parse(dataFinalString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Imagem> imagens = getImagensByTarefaId(id);

            tarefa = new Tarefa(tarefaId, descricao, observacoes, dataInicial, dataFinal, situacao, categoria,imagens);
        }

        cursor.close();
        db.close();

        return tarefa;
    }

    public void removerImagem(int imagemId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TAREFA_IMAGEM, COLUMN_ID + " = ?", new String[]{String.valueOf(imagemId)});
        db.close();
    }

    public int salvarImagem(Imagem imagem, int idTarefa) {
        SQLiteDatabase db = getWritableDatabase();

        byte[] imagemData = convertBitmapToByteArray(imagem.getBitmap());

        ContentValues imagemValues = new ContentValues();
        imagemValues.put(COLUMN_TAREFA_ID, idTarefa);
        imagemValues.put(COLUMN_IMAGEM_DATA, imagemData);

        long imagemId = db.insert(TABLE_TAREFA_IMAGEM, null, imagemValues);

        db.close();

        return (int) imagemId;
    }


    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}











