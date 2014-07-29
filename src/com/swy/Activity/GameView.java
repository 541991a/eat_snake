package com.swy.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.swy.vo.Apple;
import com.swy.vo.Dot;
import com.swy.vo.Snake;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	SnakeActivity activity; //总Activity的引用
	private TutorialThread thread;//刷帧的线程
	int state;             //状态 、 0 失败， 1 开始  ，2准备    3暂停 
	int score;             //得分
    float StartX;          //蛇头开始点X轴
    float StartY;         //蛇头开始点y轴
    float length;     //蛇身总长度
    float sudo;             //移动速度
    List<Dot> trandot = new ArrayList<Dot>();//蛇节点列表
    int traildir;         //蛇尾移动方向
    int dir;                //移动方向   0 正横向   1正纵向  2负横向 3 负纵向
    List<Apple> applelist = new ArrayList<Apple>(); //苹果列表
    int applecount;                //苹果数量
    int banjing;                   //苹果半径
    Snake snake;
    Guize guize;
    int viewwidth;
    int viewheight;
	public GameView(Context context,SnakeActivity activity) {	
		super(context);
		this.activity = activity;
		getHolder().addCallback(this);
		this.thread = new TutorialThread(getHolder(), this);//初始化刷帧线程
		init();
	}

	public void init() {
		guize = new Guize();
		state = 1;
		score = 0;
		StartX = 50;
		StartY = 200;
		length = 50;
		sudo = 5;
		traildir = 0;
		dir = 0;
		applecount = 5;
		banjing = 5;
		viewwidth = activity.getWindowManager().getDefaultDisplay().getWidth()-1;
		viewheight = activity.getWindowManager().getDefaultDisplay().getHeight()-1;
		for(int i =0;i<applecount;i++)		
		{
			applelist = guize.addApple(viewwidth-10,viewheight-10,applelist, banjing);
		}
		trandot.add(new Dot(StartX-length,StartY));
		trandot.add(new Dot(StartX,StartY));
		snake = new Snake(new Dot(StartX-length,StartY), new Dot(StartX,StartY), trandot, length, sudo, dir ,traildir);
	    
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//清屏
		canvas.drawColor(Color.BLACK);
		
		switch (state)
		{
		case 0:
			drawdead(canvas);
			break;
		case 1:
			drawbegin(canvas);
			break;
		case 2:
			drawready(canvas);
			break;
		case 3:
			drawpause(canvas);
			break;
		}
	}
	
	//游戏进行状态下绘制的界面
	public void drawbegin(Canvas canvas)
	{
		drawgameview(canvas);
		//判断是否死亡
		isdead();
		//判断是否吃掉苹果
		iseat();
		//刷新蛇
		snake = guize.refreshSnake(snake);
		
	}
	//暂停状态下绘制的界面
	public void drawpause(Canvas canvas) {
		drawgameview(canvas);
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        p.setTextSize(15);
        canvas.drawText("暂停中。。。", 120, 200, p);
	}
	//准备状态下绘制的界面
	public void drawready(Canvas canvas){
		drawgameview(canvas);
	}
	//死亡状态下绘制的界面
	public void drawdead(Canvas canvas)
	{
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setTextSize(25);
		canvas.drawText("游戏结束", 100, 80, p);
		canvas.drawText("您的分数为"+String.valueOf(score), 80, 110, p);
		p.setColor(Color.GRAY);
		p.setTextSize(20);
		canvas.drawText("重新开始", 110, 200, p);
		canvas.drawText("退出游戏", 110, 230, p);
		
	}
	//绘制基本游戏界面
    public void drawgameview(Canvas canvas)
	{
		//绘制分数
		Paint p_score = new Paint();
		p_score.setColor(Color.YELLOW);
		canvas.drawText("得分："+String.valueOf(score), 20, 25, p_score);
		//绘制墙壁
		Paint p_wall = new Paint();
		p_wall.setColor(Color.GRAY);
		p_wall.setStrokeWidth(10);
		canvas.drawLine(0, 0, viewwidth, 0, p_wall);
		canvas.drawLine(0, 0, 0,viewheight, p_wall);
		canvas.drawLine(viewwidth,viewheight,viewwidth,0,p_wall);
		canvas.drawLine(viewwidth,viewheight,0,viewheight,p_wall);
		//绘制苹果
		Paint p_apple = new Paint();
		p_apple.setDither(true);
		p_apple.setColor(Color.RED);
		p_apple.setAntiAlias(true);  
		for (int i = 0;i<applelist.size();i++)
		{
			canvas.drawCircle(applelist.get(i).getMiddledot().getX(),applelist.get(i).getMiddledot().getY(), banjing, p_apple);
		}
		//绘制蛇
		Paint p_snake = new Paint();
		p_snake.setColor(Color.BLUE);
		p_snake.setDither(true);
		p_snake.setStrokeWidth(5);
		p_snake.setStrokeCap(Cap.ROUND);
		for(int i = snake.getTrandot().size()-1;i>0;i--)
		{
		    canvas.drawLine(snake.getTrandot().get(i-1).getX(), snake.getTrandot().get(i-1).getY(), snake.getTrandot().get(i).getX(), snake.getTrandot().get(i).getY(), p_snake);
		}
	}
	//判断吃苹果的方法
	public void iseat()
	{
		Iterator<Apple> it = applelist.iterator();
		boolean iseat = false;
    	while(it.hasNext())
    	{
    		Apple apple = it.next();
    		if (guize.getDotLength(snake.getHeaddot(),apple.getMiddledot()) <= apple.getBanjing()+2)
    		{
    			it.remove();
    			iseat = true;
    			break;
    		}
    	}
    	if (iseat)
    	{
    	  score = score + 10;
    	  snake.setLength(snake.getLength() + 10);
    	  snake.setSudo((float)(snake.getSudo() + 0.1));
    	  applelist = guize.addApple(viewwidth-10,viewheight-10,applelist, banjing);
    	}
	}
    //判断死亡的方法
    public void isdead()
    {
    	if (snake.getHeaddot().getX() <= 5 || snake.getHeaddot().getX() >= viewwidth-5 || snake.getHeaddot().getY() <= 5 || snake.getHeaddot().getY() >= viewheight-5)
    	{
    		state = 0;
    	}
    	if (snake.getTrandot().size()>4)
    	{
    		
	    	for (int i=snake.getTrandot().size()-3;i>0;i--)
	    	{
	    		switch(snake.getDir())
	    		{
	    		case 0:
	    		case 2:
	    			if((snake.getHeaddot().getX() >= snake.getTrandot().get(i).getX() &&  snake.getHeaddot().getX()-snake.getSudo() <= snake.getTrandot().get(i).getX())||(snake.getHeaddot().getX() <= snake.getTrandot().get(i).getX() &&  snake.getHeaddot().getX()+snake.getSudo() >= snake.getTrandot().get(i).getX()))
	 	    		{
	 	    			if ((snake.getHeaddot().getY() >= snake.getTrandot().get(i).getY() && snake.getHeaddot().getY() <= snake.getTrandot().get(i-1).getY())||(snake.getHeaddot().getY() <= snake.getTrandot().get(i).getY() && snake.getHeaddot().getY() >= snake.getTrandot().get(i-1).getY()))
	 		    		   {
	 	    				  Log.i("654321", String.valueOf(i));
	 		    			  state = 0;
	 		    		   }	
	 	    		}
	    			break;
	    		case 1:
	    		case 3:
	    			if ((snake.getHeaddot().getY() >= snake.getTrandot().get(i).getY() &&  snake.getHeaddot().getY()-snake.getSudo() <= snake.getTrandot().get(i).getY())||(snake.getHeaddot().getY() <= snake.getTrandot().get(i).getY() &&  snake.getHeaddot().getY()+snake.getSudo() >= snake.getTrandot().get(i).getY()))
		    		{			
		    			if ((snake.getHeaddot().getX() >= snake.getTrandot().get(i).getX() && snake.getHeaddot().getX() <= snake.getTrandot().get(i-1).getX())||(snake.getHeaddot().getX() <= snake.getTrandot().get(i).getX() && snake.getHeaddot().getX() >= snake.getTrandot().get(i-1).getX()))
		    		   {
		    			  Log.i("123456", String.valueOf(i));
		    			  state = 0;
		    		   }
		    		}
	    			break;
	    		}
	    		
	    	   
	    	}
    	}
    }
	//按钮事件监听
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
		{
			if (state == 1)
			{
				state = 3;
			}
			else if(state == 3)
			{
				state = 1;
			}
			return true;
		}
       return super.onKeyDown(keyCode, event);
		
	};
	//触摸事件监听
	public boolean onTouchEvent(MotionEvent event) {
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if (state == 1){
			switch (snake.getDir())
			{
			case 0:
				if(event.getY()>snake.getHeaddot().getY())
				{
					snake.getTrandot().add(snake.getHeaddot());
					snake.setDir(1);
				}
				else if(event.getY()<snake.getHeaddot().getY())
				{
					snake.getTrandot().add(snake.getHeaddot());
					snake.setDir(3);
				}
				break;
			case 1:
				if(event.getX()>snake.getHeaddot().getX())
				{
					snake.getTrandot().add(snake.getHeaddot());
					snake.setDir(0);
				}
				else if(event.getX()<snake.getHeaddot().getX())
				{
					snake.getTrandot().add(snake.getHeaddot());
					snake.setDir(2);
				}
				break;
			case 2:
				if(event.getY()>snake.getHeaddot().getY())
				{
					snake.getTrandot().add(snake.getHeaddot());
					snake.setDir(1);
				}
				else if(event.getY()<snake.getHeaddot().getY())
				{
					snake.getTrandot().add(snake.getHeaddot());
					snake.setDir(3);
				}
				break;
			case 3:
				if(event.getX()>snake.getHeaddot().getX())
				{
					snake.getTrandot().add(snake.getHeaddot());
					snake.setDir(0);
				}
				else if(event.getX()<snake.getHeaddot().getX())
				{
					snake.getTrandot().add(snake.getHeaddot());
					snake.setDir(2);
				}
				break;
			}
		    }
			if (state == 0)
			{
				if (event.getX() >= 110 && event.getX() <= 200 && event.getY() >= 180 &&event.getY() <= 200)
				{
					//重新开始
					activity.myHandler.sendEmptyMessage(2);
				}
				else if(event.getX() >= 110 && event.getX() <= 200 && event.getY() >= 210 &&event.getY() <= 230)
				{
					//退出游戏
					System.exit(0);
				}
			}
		}
		return super.onTouchEvent(event);
	}
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.thread.setFlag(true);
        this.thread.start();//启动刷帧线程
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		 boolean retry = true;
	        thread.setFlag(false);//停止刷帧线程
	        while (retry) {
	            try {
	                thread.join();
	                retry = false;//设置循环标志位为false
	            } 
	            catch (InterruptedException e) {//不断地循环，直到等待的线程结束
	            }
	        }
		
	}
	
	
	class TutorialThread extends Thread{//刷帧线程
		private int span = 50;//睡眠的毫秒数 
		private SurfaceHolder surfaceHolder;//SurfaceHolder的引用
		private GameView gameView;//gameView的引用
		private boolean flag = false;//循环标志位
        public TutorialThread(SurfaceHolder surfaceHolder, GameView gameView) {//构造器
            this.surfaceHolder = surfaceHolder;//得到SurfaceHolder引用
            this.gameView = gameView;//得到GameView的引用
        }
        public void setFlag(boolean flag) {//设置循环标记
        	this.flag = flag;
        }
		public void run() {//重写的方法
			Canvas c;//画布
            while (this.flag) {//循环绘制
                c = null;
                try {
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {
                    	gameView.onDraw(c);//调用绘制方法
                    }
                } finally {//用finally保证下面代码一定被执行
                    if (c != null) {
                    	//更新屏幕显示内容
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try{
                	Thread.sleep(span);//睡眠span毫秒
                }catch(Exception e){//不会异常信息
                	e.printStackTrace();//打印异常堆栈信息
                }
            }
		}
	}

}
