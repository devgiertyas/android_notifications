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
        private static final String DATABASE_NAME = "categorias.db";
        private static final int DATABASE_VERSION = 1;

        private static final String TABLE_CATEGORIAS = "categorias";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_NOME = "nome";

        public CategoriaDAO(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableQuery = "CREATE TABLE " + TABLE_CATEGORIAS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOME + " TEXT)";
            db.execSQL(createTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_CATEGORIAS;
            db.execSQL(dropTableQuery);
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
