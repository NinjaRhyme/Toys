package games.nbody;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.annotation.NonNull;

import java.util.ArrayList;


// F = G * M * m / r ^ 2
// G = 6.67259 * 10 ^ -11 (N * m ^ 2 / kg ^ 2)
//----------------------------------------------------------------------------------------------------
public class NBody extends View implements View.OnTouchListener, GestureDetector.OnGestureListener {

    //----------------------------------------------------------------------------------------------------
    protected class Body {
        //----------------------------------------------------------------------------------------------------
        public float x;
        public float y;
        public float radius;
        public float mass;
        public float velocityX;
        public float velocityY;

        //----------------------------------------------------------------------------------------------------
        public Body(float _x, float _y) {
            x = _x;
            y = _y;
            radius = 5.f;
            mass = 1.f;
            velocityX = 0.f;
            velocityY = 0.f;
        }

        //----------------------------------------------------------------------------------------------------
        public void update(long _deltaTime) {
            x += velocityX * _deltaTime;
            y += velocityY * _deltaTime;
        }

    }

    //----------------------------------------------------------------------------------------------------
    private GestureDetector m_gestureDetector;
    private Paint m_paint = new Paint();
    private Handler m_handler = new Handler();
    private long m_currentTime;
    private float m_offsetX = 0.f;
    private float m_offsetY = 0.f;
    private ArrayList<Body> m_bodies = new ArrayList<>();
    private int m_mode;

    //----------------------------------------------------------------------------------------------------
    public NBody(Context context) {
        super(context);
        initialize(context);
    }

    public NBody(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public NBody(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    //----------------------------------------------------------------------------------------------------
    boolean initialize(Context context) {
        setOnTouchListener(this);
        m_gestureDetector = new GestureDetector(context, this);

        m_paint.setAntiAlias(true);

        Runnable task = new Runnable() {
            public void run() {
                long deltaTime = System.currentTimeMillis() - m_currentTime;
                m_currentTime += deltaTime;

                for (Body body : m_bodies) {
                    body.update(deltaTime);
                }
                invalidate();

                deltaTime += System.currentTimeMillis() - m_currentTime;
                m_handler.postDelayed(this, deltaTime - 16 > 0 ? deltaTime - 16 : 0);
            }
        };
        m_currentTime = System.currentTimeMillis();
        m_handler.post(task);

        return true;
    }

    // Draw
    //----------------------------------------------------------------------------------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);

        drawFrame(canvas);
        drawBodies(canvas);
    }

    protected void drawFrame(Canvas canvas) {
        canvas.translate(m_offsetX, m_offsetY);
        canvas.drawColor(0xFF333333);

        m_paint.setColor(0xF2F2F2F2);
        m_paint.setStyle(Paint.Style.STROKE);
        m_paint.setStrokeWidth(4);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() * 2 / 5, m_paint);
        // canvas.drawArc(new RectF(200, 200, 500, 500), 0, 360, false, m_paint);
    }

    protected void drawBodies(Canvas canvas) {
        for(int i = 0; i < m_bodies.size(); ++i){
            m_paint.setColor(0xFFB1CB4E);
            m_paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(m_bodies.get(i).x, m_bodies.get(i).y, m_bodies.get(i).radius, m_paint);
        }
    }

    // Touch
    //----------------------------------------------------------------------------------------------------
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return m_gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        m_bodies.add(new Body(e.getX() - m_offsetX, e.getY() - m_offsetY));

        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Body body = new Body(e1.getX() - m_offsetX, e1.getY() - m_offsetY);
        body.velocityX = velocityX / 3000;
        body.velocityY = velocityY / 3000;
        m_bodies.add(body);

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //m_offsetX -= distanceX;
        //m_offsetY -= distanceY;

        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return true;
    }
}
