package starfinderproject.curl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by lumber-zach on 18/01/18.
 */

public class RotatingImageView extends android.support.v7.widget.AppCompatImageView {

    private int rotationDegrees = 0;
    private float scale;
    private int directionScale;
    private static final int STROKE_WIDTH_DP = 6;
    private Paint paintBorder;
    private Bitmap bitmap;
    private int strokeWidthPx;
    private RectF rectF;
    private Rect rectSource;
    private boolean isDrawn= false;
    private int style = 0;


    private long curTime;
    private long elapsedTime;
    private long lastTime;

    /** Simple constructor. */
    public RotatingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        // The resource is embedded, but it can be easily moved in the constructor.
        selectBitmap();
        //setImageBitmap(bitmap);
        // The same goes for the stroke width in dp.
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isDrawn){
            return;
        }
        curTime = System.currentTimeMillis();
        elapsedTime = curTime - lastTime;
        lastTime = curTime;
        if (elapsedTime < 0) {
            elapsedTime = 0;
        }
        else if (elapsedTime > 200) {
            elapsedTime = 200;
        }
        canvas.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        canvas.rotate(rotation(3));
        float scaleFactor = scale(0.01f);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(-canvas.getWidth()/2, -canvas.getHeight()/2);
        canvas.drawBitmap(bitmap,rectSource,rectF,null);
    }

    private float scale(float delta) {
        scale = (scale + delta * directionScale);
        if (scale <= 0) {
            directionScale = 1;
            scale = 0;
            selectBitmap();
        } else if (scale >= 0.5) {
            directionScale = -1;
            scale = 0.5f;
        }
        return scale;
    }

    private int rotation(int delta) {
        rotationDegrees = (rotationDegrees + delta) % 360;
        return rotationDegrees;
    }

    private void selectBitmap(){
        style = new Random().nextInt(4);
        switch(style){
            case 0:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_star);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue_star);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.green_star);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_star);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_star);
        }
        strokeWidthPx = (int) (STROKE_WIDTH_DP * getResources().getDisplayMetrics().density);
        int halfStrokeWidthPx = strokeWidthPx / 2;

        paintBorder = new Paint();
        paintBorder.setStyle(Paint.Style.STROKE);
        // Stroke width is in pixels.
        paintBorder.setStrokeWidth(strokeWidthPx);
        // Our color for the border.
        paintBorder.setColor(Color.BLUE);

        int totalWidth = bitmap.getWidth();
        int totalHeight = bitmap.getHeight();

        // The rectangle that will be used for drawing the colored border.
        rectF = new RectF(0,0, getWidth(), getHeight());
        rectSource = new Rect(0,0,totalWidth,totalHeight);
        isDrawn = true;
    }
}
