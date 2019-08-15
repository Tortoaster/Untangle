package com.tortoaster.untangle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;

public class UntangleView extends View implements View.OnTouchListener {
	
	public interface UntangleListener {
		void move();
		void win();
	}
	
	private float vertexSize;
	
	private int scale, moves;
	
	@ColorInt
	private int validColor, invalidColor;
	
	private Graph graph;
	
	private Vertex selected;
	
	private Paint paint;
	
	private UntangleListener listener;
	
	public UntangleView(Context context) {
		this(context, null);
	}
	
	public UntangleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public UntangleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.UntangleView);
		
		float edgeWidth = array.getDimension(R.styleable.UntangleView_edgeWidth, 20);
		
		vertexSize = array.getDimension(R.styleable.UntangleView_vertexSize, 30);
		validColor = array.getColor(R.styleable.UntangleView_validColor, Color.WHITE);
		invalidColor = array.getColor(R.styleable.UntangleView_invalidColor, Color.RED);
		
		array.recycle();
		
		paint = new Paint();
		paint.setStrokeWidth(edgeWidth);
		paint.setAntiAlias(true);
		
		setOnTouchListener(this);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent e) {
		switch(e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				selected = graph.getVertexAt(e.getX() / scale, e.getY() / scale, vertexSize / scale);
				
				break;
			case MotionEvent.ACTION_MOVE:
				if(selected != null) {
					selected.moveTo(Math.min(scale - vertexSize - getPaddingRight(), Math.max(vertexSize + getPaddingLeft(), e.getX())) / scale, Math.min(scale - vertexSize - getPaddingBottom(), Math.max(vertexSize + getPaddingTop(), e.getY())) / scale);
					graph.isPlanar();
					postInvalidate();
				}
				
				break;
			case MotionEvent.ACTION_UP:
				if(selected != null) {
					selected = null;
					
					moves++;
					
					if(listener != null) {
						listener.move();
						
						if(graph.isPlanar()) {
							listener.win();
						}
					}
				}
		}
		
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for(Edge e : graph.getEdges()) {
			if(e.isValid()) {
				paint.setColor(validColor);
			} else {
				paint.setColor(invalidColor);
			}
			
			canvas.drawLine(e.getV1().getX() * scale, e.getV1().getY() * scale, e.getV2().getX() * scale, e.getV2().getY() * scale, paint);
		}
		
		for(Vertex v : graph.getVertices()) {
			if(v.isValid()) {
				paint.setColor(validColor);
			} else {
				paint.setColor(invalidColor);
			}
			
			canvas.drawCircle(v.getX() * scale, v.getY() * scale, vertexSize, paint);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldW, int oldH) {
		super.onSizeChanged(w, h, oldW, oldH);
		
		scale = w;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int size = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
		
		setMeasuredDimension(size, size);
	}
	
	public void setGraph(Graph graph) {
		this.graph = graph;
		this.graph.isPlanar();
		postInvalidate();
	}
	
	public void setListener(UntangleListener listener) {
		this.listener = listener;
	}
}
