package com.example.arrangethelines;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ThreadLocalRandom;

public class Canvas1 extends View {

    Rect[] lines = new Rect[6];
    Context context;



    float initialY;
    int currnt_move=-1;
    float time=10;
    int score=0;

    int[] random_pos = {0,0,0,0,0,0};
    Paint paint_blue;
    Paint paint_red;
    Paint paint_green;
    Paint paint_game;
    private Dialog dialog;

    public Canvas1(Context context) {
        super(context);
        this.context=context;
        for(int i=0;i<6;i++)
            lines[i] = new Rect();

        paint_blue = new Paint();
        paint_red = new Paint();
        paint_green = new Paint();
        paint_game = new Paint();
        dialog = new Dialog(context);

        paint_game.setColor(Color.BLACK);
        paint_blue.setColor(Color.BLUE);
        paint_green.setColor(Color.GREEN);
        paint_red.setColor(Color.RED);

        paint_game.setTextSize(40);
        paint_game.setFakeBoldText(true);
        paint_green.setStrokeWidth(10);
        paint_red.setStrokeWidth(10);
        paint_blue.setStrokeWidth(10);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void randomise_position() {
        for(int i=0;i<6;i++)
            random_pos[i] = ThreadLocalRandom.current().nextInt(0, getHeight() + 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.purple_200));
        if(random_pos[0]==0)randomise_position();

        for(int i=0;i<6;i++) {
            lines[i].left = 40;
            lines[i].right = getWidth()-40;
            lines[i].top = random_pos[i];
            lines[i].bottom = random_pos[i] + 30;
        }

        canvas.drawRect(lines[0],paint_blue);
        canvas.drawRect(lines[1],paint_blue);
        canvas.drawRect(lines[2],paint_green);
        canvas.drawRect(lines[3],paint_green);
        canvas.drawRect(lines[4],paint_red);
        canvas.drawRect(lines[5],paint_red);

        canvas.drawText("Score :  "+score,450,60,paint_game);
        canvas.drawText("Time : "+String.format("%.02f", time),750,60,paint_game);

        if(!checkGameOver()) canvas.drawText("",5,60,paint_game);
        else {
            canvas.drawText("",5,40,paint_game);

        }

        if(time<0){
            Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(400);


            dialog();






        }
        update();

    }

    private void update() {
        time-=0.02;
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
                initialY = event.getY();
                for(int i=0;i<6;i++) {
                    if(Math.abs(random_pos[i]-event.getY())<=20) {
                        currnt_move = i;
                        break;
                    }
                    else currnt_move=-1;
                }
                break;

            case MotionEvent.ACTION_MOVE:

                float finalY = event.getY();


                if (initialY < finalY) {
                    if(currnt_move!=-1){
                        random_pos[currnt_move]-=initialY-finalY;
                        initialY=finalY;
                    }
                }

                if (initialY > finalY) {
                    if(currnt_move!=-1){
                        random_pos[currnt_move]+=finalY-initialY;
                        initialY=finalY;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if(checkGameOver()){
                    Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(200);

                    Toast.makeText(getContext(), "Succeeded", Toast.LENGTH_SHORT).show();
                    randomise_position();
                    score++;
                    time=10;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_OUTSIDE:
                break;
        }
        invalidate();

        return true;
    }

    private boolean checkGameOver() {

        for(int i=2;i<=5;i++){
            if((random_pos[0]<random_pos[i]&&random_pos[1]>random_pos[i]) ||
                    random_pos[1]<random_pos[i]&&random_pos[0]>random_pos[i])
                return false;
        }


        for(int i=0;i<=5;i++){
            if((random_pos[2]<random_pos[i]&&random_pos[3]>random_pos[i]) ||
                    random_pos[3]<random_pos[i]&&random_pos[2]>random_pos[i])
                return false;
        }


        for(int i=0;i<=3;i++){
            if((random_pos[4]<random_pos[i]&&random_pos[5]>random_pos[i]) ||
                    random_pos[5]<random_pos[i]&&random_pos[4]>random_pos[i])
                return false;
        }

        return true;
    }
    private void dialog() {

        dialog.setContentView(R.layout.dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(false);

        dialog.show();
    }

}