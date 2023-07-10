package com.example.trabalho3.Models;

import android.graphics.Bitmap;

public class Imagem {
    private Bitmap bitmap;
    private int idImage;

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public Imagem(Bitmap bitmap, int idImage) {
        this.bitmap = bitmap;
        this.idImage = idImage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
