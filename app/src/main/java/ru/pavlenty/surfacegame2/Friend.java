package ru.pavlenty.surfacegame2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Random;

public class Friend {

    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed = 0;

    private int maxX;
    private int maxY;

    private int minX;
    private int minY;

    public Friend(Context context, int screenX, int screenY) {
        Log.d("RRR screenX",Integer.toString(screenX));

        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.friend);
        this.maxX = screenX;
        this.maxY = screenY;
        this.minX = 0;
        this.minY = 0;
        Random r = new Random();
        speed = r.nextInt(6);
        x = screenX;
        y = r.nextInt(this.maxY);
    }

    public void update(int playerSpeed) {
        speed = 5;
        if (playerSpeed > speed) {speed = playerSpeed;}
        x -= speed;
        System.out.println(playerSpeed);
        if (x < -this.bitmap.getWidth()) {
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);
        }
    }

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
