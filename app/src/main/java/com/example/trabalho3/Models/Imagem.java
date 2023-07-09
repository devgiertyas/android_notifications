package com.example.trabalho3.Models;

import android.graphics.Bitmap;

public class Imagem {
    private Bitmap bitmap;

    public Imagem(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
