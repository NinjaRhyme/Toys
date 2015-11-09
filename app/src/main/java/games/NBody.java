package games;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Vector;


// F = G * M * m / r ^ 2
// G = 6.67259 * 10 ^ -11 (N * m ^ 2 / kg ^ 2)
public class NBody extends View {

    //----------------------------------------------------------------------------------------------------
    Paint m_paint = new Paint();
    Vector m_bodies;

    //----------------------------------------------------------------------------------------------------
    public NBody(Context context) {
        super(context);
        initialize();
    }

    public NBody(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public NBody(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    //----------------------------------------------------------------------------------------------------
    boolean initialize() {
        m_paint.setAntiAlias(true);

        return true;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);

        drawFrame(canvas);
    }

    //----------------------------------------------------------------------------------------------------
    protected void drawFrame(Canvas canvas) {
        canvas.drawColor(0xFF333333);

        m_paint.setColor(0xF2F2F2F2);
        m_paint.setStyle(Paint.Style.STROKE);
        m_paint.setStrokeWidth(4);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() * 2 / 5, m_paint);
        // canvas.drawArc(new RectF(200, 200, 500, 500), 0, 360, false, m_paint);
    }
}
