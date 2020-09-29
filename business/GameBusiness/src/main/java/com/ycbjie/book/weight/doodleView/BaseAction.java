package com.ycbjie.book.weight.doodleView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 动作的基础类
 * <p>
 * Created by developerHaoz on 2017/7/12.
 */

abstract class BaseAction {

    public int color;

    BaseAction() {
        color = Color.WHITE;
    }

    BaseAction(int color) {
        this.color = color;
    }

    public abstract void draw(Canvas canvas);

    public abstract void move(float mx, float my);

}

class MyPoint extends BaseAction {
    private float x;
    private float y;

    MyPoint(float px, float py, int color) {
        super(color);
        this.x = px;
        this.y = py;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void move(float mx, float my) {

    }
}

    /**
     * 自由曲线
     */
    class MyPath extends BaseAction {
        private Path path;
        private int size;

        MyPath() {
            path = new Path();
            size = 1;
        }

        MyPath(float x, float y, int size, int color) {
            super(color);
            this.path = new Path();
            this.size = size;
            path.moveTo(x, y);
            path.lineTo(x, y);
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawPath(path, paint);
        }

        @Override
        public void move(float mx, float my) {
            path.lineTo(mx, my);
        }
    }

    /**
     * 直线
     */
    class MyLine extends BaseAction {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
        private int size;

        MyLine() {
            startX = 0;
            startY = 0;
            stopX = 0;
            stopY = 0;
        }

        MyLine(float x, float y, int size, int color) {
            super(color);
            this.startX = x;
            this.startY = y;
            stopX = x;
            stopY = y;
            this.size = size;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }

        @Override
        public void move(float mx, float my) {
            this.stopX = mx;
            this.stopY = my;
        }
    }

    /**
     * 方框
     */
    class MyRect extends BaseAction {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
        private int size;

        MyRect() {
            this.startX = 0;
            this.startY = 0;
            this.stopX = 0;
            this.stopY = 0;
        }

        MyRect(float x, float y, int size, int color) {
            super(color);
            this.startX = x;
            this.startY = y;
            this.stopX = x;
            this.stopY = y;
            this.size = size;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            canvas.drawRect(startX, startY, stopX, stopY, paint);
        }

        @Override
        public void move(float mx, float my) {
            stopX = mx;
            stopY = my;
        }
    }

    /**
     * 圆框
     */
    class MyCircle extends BaseAction {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
        private float radius;
        private int size;

        MyCircle() {
            startX = 0;
            startY = 0;
            stopX = 0;
            stopY = 0;
            radius = 0;
        }

        MyCircle(float x, float y, int size, int color) {
            super(color);
            this.startX = x;
            this.startY = y;
            this.stopX = x;
            this.stopY = y;
            this.radius = 0;
            this.size = size;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius, paint);
        }

        @Override
        public void move(float mx, float my) {
            stopX = mx;
            stopY = my;
            radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
                    + (my - startY) * (my - startY))) / 2);
        }
    }

    class MyFillRect extends BaseAction {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
        private int size;

        MyFillRect() {
            this.startX = 0;
            this.startY = 0;
            this.stopX = 0;
            this.stopY = 0;
        }

        MyFillRect(float x, float y, int size, int color) {
            super(color);
            this.startX = x;
            this.startY = y;
            this.stopX = x;
            this.stopY = y;
            this.size = size;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            canvas.drawRect(startX, startY, stopX, stopY, paint);
        }

        @Override
        public void move(float mx, float my) {
            stopX = mx;
            stopY = my;
        }
    }

    /**
     * 圆饼
     */
    class MyFillCircle extends BaseAction {
        private float startX;
        private float startY;
        private float stopX;
        private float stopY;
        private float radius;
        private int size;


        public MyFillCircle(float x, float y, int size, int color) {
            super(color);
            this.startX = x;
            this.startY = y;
            this.stopX = x;
            this.stopY = y;
            this.radius = 0;
            this.size = size;
        }

        @Override
        public void draw(Canvas canvas) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setStrokeWidth(size);
            canvas.drawCircle((startX + stopX) / 2, (startX + stopY) / 2, radius, paint);
        }

        @Override
        public void move(float mx, float my) {
            stopX = mx;
            stopY = my;
            radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
                    + (my - startY) * (my - startY))) / 2);
        }
    }





























