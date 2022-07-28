package com.yc.selectviewlib;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class SelectRecyclerView extends RecyclerView {
    private static final boolean LOGGING = false;
    private static final int AUTO_SCROLL_DELAY = 25;
    private int lastDraggedIndex = -1;
    private SelectRecyclerViewAdapter<?> adapter;
    private int initialSelection;
    private boolean dragSelectActive;
    private int minReached;
    private int maxReached;
    private int hotspotHeight;
    private int hotspotOffsetTop;
    private int hotspotOffsetBottom;
    private int hotspotTopBoundStart;
    private int hotspotTopBoundEnd;
    private int hotspotBottomBoundStart;
    private int hotspotBottomBoundEnd;
    private int autoScrollVelocity;
    private boolean inTopHotspot;
    private boolean inBottomHotspot;
    private RectF topBoundRect;
    private RectF bottomBoundRect;
    private Paint debugPaint;
    private boolean debugEnabled = false;
    private FingerListener fingerListener;
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;

    public SelectRecyclerView(Context context) {
        super(context);
        this.autoScrollRunnable = new NamelessClass_1();
        this.init(context, (AttributeSet)null);
    }

    public SelectRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.autoScrollRunnable = new NamelessClass_1();
        this.init(context, attrs);
    }

    public SelectRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.autoScrollRunnable = new NamelessClass_1();
        this.init(context, attrs);
    }

    class NamelessClass_1 implements Runnable {
        NamelessClass_1() {
        }
        @Override
        public void run() {
            if (SelectRecyclerView.this.autoScrollHandler != null) {
                if (SelectRecyclerView.this.inTopHotspot) {
                    SelectRecyclerView.this.scrollBy(0, -SelectRecyclerView.this.autoScrollVelocity);
                    SelectRecyclerView.this.autoScrollHandler.postDelayed(this, 25L);
                } else if (SelectRecyclerView.this.inBottomHotspot) {
                    SelectRecyclerView.this.scrollBy(0, SelectRecyclerView.this.autoScrollVelocity);
                    SelectRecyclerView.this.autoScrollHandler.postDelayed(this, 25L);
                }

            }
        }
    }

    private void init(Context context, AttributeSet attrs) {
        this.autoScrollHandler = new Handler();
        int defaultHotspotHeight = context.getResources().getDimensionPixelSize(R.dimen.spotHeight);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SelectRecyclerView, 0, 0);

            try {
                boolean autoScrollEnabled = a.getBoolean(R.styleable.SelectRecyclerView_scrollEnabled, true);
                if (!autoScrollEnabled) {
                    this.hotspotHeight = -1;
                    this.hotspotOffsetTop = -1;
                    this.hotspotOffsetBottom = -1;
                } else {
                    this.hotspotHeight = a.getDimensionPixelSize(R.styleable.SelectRecyclerView_spotHeight, defaultHotspotHeight);
                    this.hotspotOffsetTop = a.getDimensionPixelSize(R.styleable.SelectRecyclerView_spot_offsetTop, 0);
                    this.hotspotOffsetBottom = a.getDimensionPixelSize(R.styleable.SelectRecyclerView_spot_offsetBottom, 0);
                }
            } finally {
                a.recycle();
            }
        } else {
            this.hotspotHeight = defaultHotspotHeight;
        }

    }

    public void setFingerListener(@Nullable FingerListener listener) {
        this.fingerListener = listener;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (this.hotspotHeight > -1) {
            this.hotspotTopBoundStart = this.hotspotOffsetTop;
            this.hotspotTopBoundEnd = this.hotspotOffsetTop + this.hotspotHeight;
            this.hotspotBottomBoundStart = this.getMeasuredHeight() - this.hotspotHeight - this.hotspotOffsetBottom;
            this.hotspotBottomBoundEnd = this.getMeasuredHeight() - this.hotspotOffsetBottom;
        }

    }

    public boolean setDragSelectActive(boolean active, int initialSelection) {
        if (active && this.dragSelectActive) {
            return false;
        } else {
            this.lastDraggedIndex = -1;
            this.minReached = -1;
            this.maxReached = -1;
            if (!this.adapter.isIndexSelectable(initialSelection)) {
                this.dragSelectActive = false;
                this.initialSelection = -1;
                this.lastDraggedIndex = -1;
                return false;
            } else {
                this.adapter.setSelected(initialSelection, true);
                this.dragSelectActive = active;
                this.initialSelection = initialSelection;
                this.lastDraggedIndex = initialSelection;
                if (this.fingerListener != null) {
                    this.fingerListener.onDragSelectFingerAction(true);
                }

                return true;
            }
        }
    }

    /** @deprecated */
    @Deprecated
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof SelectRecyclerViewAdapter)) {
            throw new IllegalArgumentException("Adapter must be a DragSelectRecyclerViewAdapter.");
        } else {
            this.setAdapter((SelectRecyclerViewAdapter)adapter);
        }
    }

    public void setAdapter(SelectRecyclerViewAdapter<?> adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    private int getItemPosition(MotionEvent e) {
        View v = this.findChildViewUnder(e.getX(), e.getY());
        if (v == null) {
            return -1;
        } else if (v.getTag() != null && v.getTag() instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder)v.getTag();
            return holder.getAdapterPosition();
        } else {
            throw new IllegalStateException("Make sure your adapter makes a call to super.onBindViewHolder(), and doesn't override itemView tags.");
        }
    }

    public final void enableDebug() {
        this.debugEnabled = true;
        this.invalidate();
    }

    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (this.debugEnabled) {
            if (this.debugPaint == null) {
                this.debugPaint = new Paint();
                this.debugPaint.setColor(-16777216);
                this.debugPaint.setAntiAlias(true);
                this.debugPaint.setStyle(Style.FILL);
                this.topBoundRect = new RectF(0.0F, (float)this.hotspotTopBoundStart, (float)this.getMeasuredWidth(), (float)this.hotspotTopBoundEnd);
                this.bottomBoundRect = new RectF(0.0F, (float)this.hotspotBottomBoundStart, (float)this.getMeasuredWidth(), (float)this.hotspotBottomBoundEnd);
            }

            c.drawRect(this.topBoundRect, this.debugPaint);
            c.drawRect(this.bottomBoundRect, this.debugPaint);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (this.adapter.getItemCount() == 0) {
            return super.dispatchTouchEvent(e);
        } else {
            if (this.dragSelectActive) {
                int itemPosition = this.getItemPosition(e);
                if (e.getAction() == 1) {
                    this.dragSelectActive = false;
                    this.inTopHotspot = false;
                    this.inBottomHotspot = false;
                    this.autoScrollHandler.removeCallbacks(this.autoScrollRunnable);
                    if (this.fingerListener != null) {
                        this.fingerListener.onDragSelectFingerAction(false);
                    }

                    return true;
                }

                if (e.getAction() == 2) {
                    if (this.hotspotHeight > -1) {
                        float simulatedY;
                        float simulatedFactor;
                        if (e.getY() >= (float)this.hotspotTopBoundStart && e.getY() <= (float)this.hotspotTopBoundEnd) {
                            this.inBottomHotspot = false;
                            if (!this.inTopHotspot) {
                                this.inTopHotspot = true;
                                this.autoScrollHandler.removeCallbacks(this.autoScrollRunnable);
                                this.autoScrollHandler.postDelayed(this.autoScrollRunnable, 25L);
                            }

                            simulatedY = (float)(this.hotspotTopBoundEnd - this.hotspotTopBoundStart);
                            simulatedFactor = e.getY() - (float)this.hotspotTopBoundStart;
                            this.autoScrollVelocity = (int)(simulatedY - simulatedFactor) / 2;
                        } else if (e.getY() >= (float)this.hotspotBottomBoundStart && e.getY() <= (float)this.hotspotBottomBoundEnd) {
                            this.inTopHotspot = false;
                            if (!this.inBottomHotspot) {
                                this.inBottomHotspot = true;
                                this.autoScrollHandler.removeCallbacks(this.autoScrollRunnable);
                                this.autoScrollHandler.postDelayed(this.autoScrollRunnable, 25L);
                            }

                            simulatedY = e.getY() + (float)this.hotspotBottomBoundEnd;
                            simulatedFactor = (float)(this.hotspotBottomBoundStart + this.hotspotBottomBoundEnd);
                            this.autoScrollVelocity = (int)(simulatedY - simulatedFactor) / 2;
                        } else if (this.inTopHotspot || this.inBottomHotspot) {
                            this.autoScrollHandler.removeCallbacks(this.autoScrollRunnable);
                            this.inTopHotspot = false;
                            this.inBottomHotspot = false;
                        }
                    }

                    if (itemPosition != -1 && this.lastDraggedIndex != itemPosition) {
                        this.lastDraggedIndex = itemPosition;
                        if (this.minReached == -1) {
                            this.minReached = this.lastDraggedIndex;
                        }

                        if (this.maxReached == -1) {
                            this.maxReached = this.lastDraggedIndex;
                        }

                        if (this.lastDraggedIndex > this.maxReached) {
                            this.maxReached = this.lastDraggedIndex;
                        }

                        if (this.lastDraggedIndex < this.minReached) {
                            this.minReached = this.lastDraggedIndex;
                        }

                        if (this.adapter != null) {
                            this.adapter.selectRange(this.initialSelection, this.lastDraggedIndex, this.minReached, this.maxReached);
                        }

                        if (this.initialSelection == this.lastDraggedIndex) {
                            this.minReached = this.lastDraggedIndex;
                            this.maxReached = this.lastDraggedIndex;
                        }
                    }

                    return true;
                }
            }

            return super.dispatchTouchEvent(e);
        }
    }
}
