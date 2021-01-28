package ru.pavlenty.surfacegame2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private int needF = 150;
    private int needE = 50;
    private int friendNum = 0;
    private ArrayList<Star> stars = new ArrayList<Star>();
    private ArrayList<Friend> friends = new ArrayList<Friend>();
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private Boom boom = null;
    private int enemyNum = 0;

    int screenX;
    int screenY;
    int countMisses;

    boolean flag;
    private boolean isGameOver;
    int score;

    int highScore[] = new int[4];

    SharedPreferences sharedPreferences;

    static MediaPlayer gameOnsound;
    final MediaPlayer killedEnemysound;
    final MediaPlayer gameOversound;

    Context context;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        player = new Player(context, screenX, screenY);

        surfaceHolder = getHolder();
        paint = new Paint();

        int starNums = 175;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        this.screenX = screenX;
        this.screenY = screenY;
        countMisses = 0;
        isGameOver = false;


        score = 0;
        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);


        highScore[0] = sharedPreferences.getInt("Niki", 9999999);
        highScore[1] = sharedPreferences.getInt("Other people", 1242);
        highScore[2] = sharedPreferences.getInt("Other people", -12421);
        highScore[3] = sharedPreferences.getInt("Other people", -242341);
        this.context = context;


        gameOnsound = MediaPlayer.create(context,R.raw.gameon);
        killedEnemysound = MediaPlayer.create(context,R.raw.killedenemy);
        gameOversound = MediaPlayer.create(context,R.raw.gameover);


        gameOnsound.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                break;
        }

        if(isGameOver){
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,MainActivity.class));
            }
        }
        return true;
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);


            paint.setColor(Color.WHITE);
            paint.setTextSize(20);

            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }
            for (Friend f: friends) {
                canvas.drawBitmap(f.getBitmap(), f.getX(), f.getY(), paint);
            }
            for (Enemy e: enemies) {canvas.drawBitmap(e.getBitmap(), e.getX(), e.getY(), paint);
                if (Rect.intersects(player.getDetectCollision(), e.getDetectCollision()))
                {killedEnemysound.start();gameOversound.start();isGameOver=true;
                boom = new Boom(context, e.getX(), e.getY());}}
            if (boom != null) {canvas.drawBitmap(boom.getBitmap(), boom.getX(), boom.getY(), paint);}
            paint.setTextSize(30);
            canvas.drawText("Очки: "+score,100,50,paint);

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);


            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Конец игры",canvas.getWidth()/2f,yPos,paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }


    public static void stopMusic(){
        gameOnsound.stop();
    }

    private void update() {
        if (!isGameOver) {score++;}
        player.changeSpeed(score);
        player.update();

        // обновление у Friend
        for (Enemy e: enemies) {e.update(player.getSpeed()); }

        for (Friend f: friends) {f.update(player.getSpeed());}

        for (Star s : stars) {s.update(player.getSpeed());}
        if (score >= needF && friendNum < 10) {needF *= 3; friendNum += 1;}
        if (score >= needE && enemyNum < 6) {needE *= 4; enemyNum += 1;}
        System.out.println(score);
        for (int i = enemies.size(); i < enemyNum; i++) {
            Enemy e = new Enemy(context, screenX, screenY);
            enemies.add(e);}
        for (int i = friends.size(); i < friendNum; i++) {
            Friend f = new Friend(context, screenX, screenY);
            friends.add(f);
        }
    }

    private void control() {
        try {
            gameThread.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


}