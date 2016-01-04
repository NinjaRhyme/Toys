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
import java.util.Iterator;
//import java.lang.Math;


// F = G * M * m / r ^ 2
// G = 6.67259 * 10 ^ -11 (N * m ^ 2 / kg ^ 2)
//----------------------------------------------------------------------------------------------------
public class NBody extends View implements View.OnTouchListener, GestureDetector.OnGestureListener {

    //----------------------------------------------------------------------------------------------------
    protected class Body {
        //----------------------------------------------------------------------------------------------------
        public boolean isAlive;
        public float x;
        public float y;
        public float radius;
        public float mass;
        public float velocityX;
        public float velocityY;
        public float accelerationX;
        public float accelerationY;

        //----------------------------------------------------------------------------------------------------
        public Body(float _x, float _y) {
            isAlive = true;
            x = _x;
            y = _y;
            radius = 5.f;
            mass = 1.f;
            velocityX = 0.f;
            velocityY = 0.f;
            accelerationX = 0.f;
            accelerationY = 0.f;
        }

        // Update acceleration
        //----------------------------------------------------------------------------------------------------
        public void update(ArrayList<Body> _bodies) {
            accelerationX = 0.f;
            accelerationY = 0.f;

            for (Body body : _bodies) {
                if (body.isAlive && body != this) {
                    float distance = distance(body);
                    if (radius + body.radius < distance) {
                        float acceleration = 0.1f * body.mass / (distance * distance);
                        accelerationX +=  (body.x - x) / distance * acceleration;
                        accelerationY +=  (body.y - y) / distance * acceleration;
                    } else {
                        // Todo
                        isAlive = false;
                        body.isAlive = false;
                    }
                }
            }
        }

        // Update velocity and position
        //----------------------------------------------------------------------------------------------------
        public void update(long _deltaTime) {
            velocityX += accelerationX * _deltaTime;
            velocityY += accelerationY * _deltaTime;
            x += velocityX * _deltaTime;
            y += velocityY * _deltaTime;
        }

        //----------------------------------------------------------------------------------------------------
        public float distance(Body body) {
            float distanceX = body.x - x;
            float distanceY = body.y - y;

            return (float)Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        }
    }

    //----------------------------------------------------------------------------------------------------
    public enum NBodyMode {
        NBODY_MODE_VIEW,
        NBODY_MODE_ACTION
    }

    //----------------------------------------------------------------------------------------------------
    private GestureDetector m_gestureDetector;
    private Paint m_paint = new Paint();
    private Handler m_handler = new Handler();
    private long m_currentTime;
    //private float intervalTime = 0.01f;
    private float m_offsetX = 0.f;
    private float m_offsetY = 0.f;
    private ArrayList<Body> m_bodies = new ArrayList<>();
    private NBodyMode m_mode = NBodyMode.NBODY_MODE_VIEW;

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
    public void setMode(NBodyMode mode)
    {
        m_mode = mode;
    }

    //----------------------------------------------------------------------------------------------------
    public NBodyMode getMode()
    {
        return m_mode;
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
                    body.update(m_bodies);
                }

                for(Iterator<Body> it = m_bodies.iterator(); it.hasNext();) {
                    Body body = it.next();
                    if(body.isAlive) {
                        body.update(deltaTime);
                    } else {
                        it.remove();
                    }
                }

                invalidate();

                deltaTime += System.currentTimeMillis() - m_currentTime;
                m_handler.postDelayed(this, 16 - deltaTime > 0 ? 16 - deltaTime : 0);
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
        for(int i = 0; i < m_bodies.size(); ++i) {
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
        if (m_mode == NBodyMode.NBODY_MODE_ACTION) {
            m_bodies.add(new Body(e.getX() - m_offsetX, e.getY() - m_offsetY));
        }

        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (m_mode == NBodyMode.NBODY_MODE_ACTION) {
            Body body = new Body(e1.getX() - m_offsetX, e1.getY() - m_offsetY);
            body.velocityX = velocityX / 3000;
            body.velocityY = velocityY / 3000;
            m_bodies.add(body);
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (m_mode == NBodyMode.NBODY_MODE_VIEW) {
            m_offsetX -= distanceX;
            m_offsetY -= distanceY;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return true;
    }
}
