package ru.pavlenty.surfacegame2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

public class Enemy {

    private Bitmap bitmap;
    private Rect detectCollision;
    private int x;
    private int y;
    private int speed = 0;

    private int maxX;
    private int maxY;

    private int minX;
    private int minY;

    public Enemy(Context context, int screenX, int screenY) {
        Log.d("RRR screenX",Integer.toString(screenX));

        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        this.maxX = screenX;
        this.maxY = screenY;
        this.minX = 0;
        this.minY = 0;
        Random r = new Random();
        speed = r.nextInt(6);
        x = screenX;
        y = r.nextInt(this.maxY);
        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(int playerSpeed) {
        x -= playerSpeed * 2;
        System.out.println(playerSpeed);
        if (x < -this.bitmap.getWidth()) {
            Random generator = new Random();
            y = generator.nextInt(maxY);
            x = maxX + generator.nextInt(maxX);
        }
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
