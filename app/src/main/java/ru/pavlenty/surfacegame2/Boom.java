package ru.pavlenty.surfacegame2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import android.content.Context;

public class Boom {
    private Bitmap bitmap;
    private Rect detectCollision;
    private int x;
    private int y;

    private int maxX;
    private int maxY;

    private int minX;
    private int minY;

    public Boom(Context context, int x, int y) {
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boom);
        this.x = x; this.y = y;
    }
    public void update(int x, int y) {this.x = x; this.y = y;}

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
