package com.example.trabalho3.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.trabalho3.Models.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "tasktres.db";
        private static final int DATABASE_VERSION = 2;
        private static final String TABLE_CATEGORIAS = "categorias";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_NOME = "nome";

    private static final String TABLE_CATEGORIA = "categorias";
    private static final String TABLE_TAREFA = "tarefa";
    private static final String COLUMN_DESCRICAO = "descricao";
    private static final String COLUMN_OBSERVACOES = "observacoes";
    private static final String COLUMN_DATA_INICIAL = "data_inicial";
    private static final String COLUMN_DATA_FINAL = "data_final";
    private static final String COLUMN_SITUACAO = "situacao";
    private static final String COLUMN_CATEGORIA_ID = "categoria_id";

    private static final String TABLE_TAREFA_IMAGEM = "tarefa_imagem";
    private static final String COLUMN_TAREFA_ID = "tarefa_id";
    private static final String COLUMN_IMAGEM_DATA = "imagem_data";

        public CategoriaDAO(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

        public void insertCategoria(Categoria categoria) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOME, categoria.getNome());
            db.insert(TABLE_CATEGORIAS, null, values);
            db.close();
        }

        public List<Categoria> getAllCategorias() {
            List<Categoria> categorias = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_CATEGORIAS, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String nome = cursor.getString(cursor.getColumnIndex(COLUMN_NOME));
                    Categoria categoria = new Categoria(id, nome);
                    categorias.add(categoria);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return categorias;
        }

    public void updateCategoria(Categoria categoria) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, categoria.getNome());
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(categoria.getId()) };
        db.update(TABLE_CATEGORIAS, values, whereClause, whereArgs);
        db.close();
    }

    public Categoria getCategoriaById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(TABLE_CATEGORIAS, null, selection, selectionArgs, null, null, null);
        Categoria categoria = null;
        if (cursor.moveToFirst()) {
            int categoriaId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String nome = cursor.getString(cursor.getColumnIndex(COLUMN_NOME));
            categoria = new Categoria(categoriaId, nome);
        }
        cursor.close();
        db.close();
        return categoria;
    }

}
