package embedded.cse.cau.ac.kr.test3;


/**
 * Created by caucse on 2018-01-29.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TimerTask;


public class shapeview extends View {
    int time = 0;
    float hz;
    int x, y;
    int width = 1050, height = 900;
    int left = 1065, top = 895;

    ArrayList pitch = new ArrayList<Point>();
    ArrayList length = new ArrayList<Integer>();

    public shapeview(Context context){
        super(context);
        setBackgroundColor(Color.BLACK);
    }


    @SuppressLint("DrawAllocation")
    protected  void onDraw(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Point temp_Point = new Point(0, 0);

        x = left - time;
        y= top - (int)(((float)(height*3)/(2000 - 80 + 1))*hz);

        if(y < 20){
            y = 35;
        }

        if(y <= top - 3){
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(Color.argb(255, 171, 242, 0));
            canvas.drawRect(new Rect(533, y, 548, y-15), paint);
        }

        int size = pitch.size();

        for(int i=0; i<size && size ==length.size(); i++){
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            temp_Point = (Point) pitch.get(i);

            if(temp_Point.y != 895) {
                paint.setColor(Color.WHITE);
            }
            else{
                paint.setColor(Color.BLACK);
            }

            temp_Point.x = temp_Point.x - 15;
            if (temp_Point.x <= 30) {
                size--;
                pitch.remove(i);
                length.remove(i);
                i--;
                continue;
            }

            // 목소리 판별 부분, 1/3 지나기 전
            if((temp_Point.x - (int)length.get(i) <= 540) &&  (temp_Point.y != 895) && ((540 - (temp_Point.x - (int) length.get(i))) <= (int)length.get(i)/3)){
                canvas.drawRect(new Rect(temp_Point.x,  temp_Point.y, 540, temp_Point.y-15), paint);
                if(y - temp_Point.y > -6 && y - temp_Point.y < 6){
                    temp_Point.mode = 0;
                    paint.setColor(Color.argb(255, 0, 216, 255));
                    canvas.drawRect(new Rect(540,  temp_Point.y, temp_Point.x - (int)length.get(i), temp_Point.y-15), paint);
                }
                else if(y - temp_Point.y >= -13 && y - temp_Point.y <= 13){
                    temp_Point.mode = 1;
                    paint.setColor(Color.WHITE);
                    canvas.drawRect(new Rect(540,  temp_Point.y, temp_Point.x - (int)length.get(i), temp_Point.y-15), paint);
                    paint.setColor(Color.argb(255, 128, 65, 217));
                    canvas.drawRect(new Rect(540,  y, temp_Point.x - (int)length.get(i), y-15), paint);
                    temp_Point.dist = y - temp_Point.y;
                }
                else{
                    temp_Point.mode = 2;
                    paint.setColor(Color.RED);
                    canvas.drawRect(new Rect(540,  temp_Point.y, temp_Point.x - (int)length.get(i), temp_Point.y-15), paint);
                }
            }

            /*
            // 목소리 판별 부분, 1/3 지난 후
            else if((temp_Point.x - (int)length.get(i) <= 540) &&  (temp_Point.y != 895) && (temp_Point.x >= 540)){
                canvas.drawRect(new Rect(temp_Point.x,  temp_Point.y, 540, temp_Point.y-15), paint);
                if(y - temp_Point.y < -13 || y - temp_Point.y > 13){
                    temp_Point.mode = 2;
                    paint.setColor(Color.RED);
                    canvas.drawRect(new Rect(540,  temp_Point.y, temp_Point.x - (int)length.get(i), temp_Point.y-15), paint);
                }
                else if(((Point) pitch.get(i)).mode == 0){
                    paint.setColor(Color.argb(255, 0, 216, 255));
                    canvas.drawRect(new Rect(540,  temp_Point.y, temp_Point.x - (int)length.get(i), temp_Point.y-15), paint);
                }
                else if(((Point) pitch.get(i)).mode == 1){
                    paint.setColor(Color.WHITE);
                    canvas.drawRect(new Rect(540,  temp_Point.y, temp_Point.x - (int)length.get(i), temp_Point.y-15), paint);
                    paint.setColor(Color.argb(255, 128, 65, 217));
                    canvas.drawRect(new Rect(540,  temp_Point.y+temp_Point.dist, temp_Point.x - (int)length.get(i), temp_Point.y+temp_Point.dist-15), paint);
                }
                else{
                    paint.setColor(Color.RED);
                    canvas.drawRect(new Rect(540,  temp_Point.y, temp_Point.x - (int)length.get(i), temp_Point.y-15), paint);
                }
            }
            */

            // 목소리 판별 완료
            else if (temp_Point.x - (int)length.get(i) < 540 &&  temp_Point.y < 882){
                if(((Point) pitch.get(i)).mode == 0){
                    paint.setColor(Color.argb(255, 0, 216, 255));
                    canvas.drawRect(new Rect(temp_Point.x, temp_Point.y, temp_Point.x - (int) length.get(i), temp_Point.y - 15), paint);
                }
                else if(((Point) pitch.get(i)).mode == 1){
                    paint.setColor(Color.WHITE);
                    canvas.drawRect(new Rect(temp_Point.x, temp_Point.y, temp_Point.x - (int) length.get(i), temp_Point.y - 15), paint);
                    paint.setColor(Color.argb(255, 128, 65, 217));
                    canvas.drawRect(new Rect(temp_Point.x, temp_Point.y+temp_Point.dist, temp_Point.x - (int) length.get(i), temp_Point.y + temp_Point.dist - 15), paint);
                }
                else {
                    paint.setColor(Color.RED);
                    canvas.drawRect(new Rect(temp_Point.x, temp_Point.y, temp_Point.x - (int) length.get(i), temp_Point.y - 15), paint);
                }
            }

            // 목소리 판별 전
            else {
                canvas.drawRect(new Rect(temp_Point.x, temp_Point.y, temp_Point.x - (int) length.get(i), temp_Point.y - 15), paint);
            }
            pitch.set(i, temp_Point);
        }

        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.MAGENTA);
        canvas.drawLine(540, 20, 540, 920, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(new Rect(15, 20, 15+width, 20+height), paint);
    }
}
