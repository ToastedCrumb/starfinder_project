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

    /** Simple constructor. */
    public RotatingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // The resource is embedded, but it can be easily moved in the constructor.
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_circle);
        //setImageBitmap(bitmap);
        // The same goes for the stroke width in dp.
        strokeWidthPx = (int) (STROKE_WIDTH_DP * getResources().getDisplayMetrics().density);
        int halfStrokeWidthPx = strokeWidthPx / 2;

        paintBorder = new Paint();
        paintBorder.setStyle(Paint.Style.STROKE);
        // Stroke width is in pixels.
        paintBorder.setStrokeWidth(strokeWidthPx);
        // Our color for the border.
        paintBorder.setColor(Color.BLUE);

        int totalWidth = bitmap.getWidth() + strokeWidthPx * 2;
        int totalHeight = bitmap.getHeight()  + strokeWidthPx * 2;

        // The rectangle that will be used for drawing the colored border.
        rectF = new RectF(halfStrokeWidthPx, halfStrokeWidthPx, totalWidth - halfStrokeWidthPx, totalHeight - halfStrokeWidthPx);
        rectSource = new Rect(0,0,totalWidth,totalHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(canvas.getWidth()/2, canvas.getHeight()/2);
        canvas.rotate(rotation(3));
        float scaleFactor = scale(0.01f);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(-canvas.getWidth()/2, -canvas.getHeight()/2);
        Log.d("Drawing","onDraw: ");
        canvas.drawBitmap(bitmap,rectSource,rectF,null);
        postInvalidateOnAnimation();
        super.onDraw(canvas);
    }

    private float scale(float delta) {
        scale = (scale + delta * directionScale);
        if (scale <= 0) {
            directionScale = 1;
            scale = 0;
        } else if (scale >= 1) {
            directionScale = -1;
            scale = 1;
        }
        return scale;
    }

    private int rotation(int delta) {
        rotationDegrees = (rotationDegrees + delta) % 360;
        return rotationDegrees;
    }
}
