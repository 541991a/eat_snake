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
	SnakeActivity activity; //��Activity������
	private TutorialThread thread;//ˢ֡���߳�
	int state;             //״̬ �� 0 ʧ�ܣ� 1 ��ʼ  ��2׼��    3��ͣ 
	int score;             //�÷�
    float StartX;          //��ͷ��ʼ��X��
    float StartY;         //��ͷ��ʼ��y��
    float length;     //�����ܳ���
    float sudo;             //�ƶ��ٶ�
    List<Dot> trandot = new ArrayList<Dot>();//�߽ڵ��б�
    int traildir;         //��β�ƶ�����
    int dir;                //�ƶ�����   0 ������   1������  2������ 3 ������
    List<Apple> applelist = new ArrayList<Apple>(); //ƻ���б�
    int applecount;                //ƻ������
    int banjing;                   //ƻ���뾶
    Snake snake;
    Guize guize;
    int viewwidth;
    int viewheight;
	public GameView(Context context,SnakeActivity activity) {	
		super(context);
		this.activity = activity;
		getHolder().addCallback(this);
		this.thread = new TutorialThread(getHolder(), this);//��ʼ��ˢ֡�߳�
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
		//����
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
	
	//��Ϸ����״̬�»��ƵĽ���
	public void drawbegin(Canvas canvas)
	{
		drawgameview(canvas);
		//�ж��Ƿ�����
		isdead();
		//�ж��Ƿ�Ե�ƻ��
		iseat();
		//ˢ����
		snake = guize.refreshSnake(snake);
		
	}
	//��ͣ״̬�»��ƵĽ���
	public void drawpause(Canvas canvas) {
		drawgameview(canvas);
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        p.setTextSize(15);
        canvas.drawText("��ͣ�С�����", 120, 200, p);
	}
	//׼��״̬�»��ƵĽ���
	public void drawready(Canvas canvas){
		drawgameview(canvas);
	}
	//����״̬�»��ƵĽ���
	public void drawdead(Canvas canvas)
	{
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setTextSize(25);
		canvas.drawText("��Ϸ����", 100, 80, p);
		canvas.drawText("���ķ���Ϊ"+String.valueOf(score), 80, 110, p);
		p.setColor(Color.GRAY);
		p.setTextSize(20);
		canvas.drawText("���¿�ʼ", 110, 200, p);
		canvas.drawText("�˳���Ϸ", 110, 230, p);
		
	}
	//���ƻ�����Ϸ����
    public void drawgameview(Canvas canvas)
	{
		//���Ʒ���
		Paint p_score = new Paint();
		p_score.setColor(Color.YELLOW);
		canvas.drawText("�÷֣�"+String.valueOf(score), 20, 25, p_score);
		//����ǽ��
		Paint p_wall = new Paint();
		p_wall.setColor(Color.GRAY);
		p_wall.setStrokeWidth(10);
		canvas.drawLine(0, 0, viewwidth, 0, p_wall);
		canvas.drawLine(0, 0, 0,viewheight, p_wall);
		canvas.drawLine(viewwidth,viewheight,viewwidth,0,p_wall);
		canvas.drawLine(viewwidth,viewheight,0,viewheight,p_wall);
		//����ƻ��
		Paint p_apple = new Paint();
		p_apple.setDither(true);
		p_apple.setColor(Color.RED);
		p_apple.setAntiAlias(true);  
		for (int i = 0;i<applelist.size();i++)
		{
			canvas.drawCircle(applelist.get(i).getMiddledot().getX(),applelist.get(i).getMiddledot().getY(), banjing, p_apple);
		}
		//������
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
	//�жϳ�ƻ���ķ���
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
    //�ж������ķ���
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
	//��ť�¼�����
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
	//�����¼�����
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
					//���¿�ʼ
					activity.myHandler.sendEmptyMessage(2);
				}
				else if(event.getX() >= 110 && event.getX() <= 200 && event.getY() >= 210 &&event.getY() <= 230)
				{
					//�˳���Ϸ
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
        this.thread.start();//����ˢ֡�߳�
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		 boolean retry = true;
	        thread.setFlag(false);//ֹͣˢ֡�߳�
	        while (retry) {
	            try {
	                thread.join();
	                retry = false;//����ѭ����־λΪfalse
	            } 
	            catch (InterruptedException e) {//���ϵ�ѭ����ֱ���ȴ����߳̽���
	            }
	        }
		
	}
	
	
	class TutorialThread extends Thread{//ˢ֡�߳�
		private int span = 50;//˯�ߵĺ����� 
		private SurfaceHolder surfaceHolder;//SurfaceHolder������
		private GameView gameView;//gameView������
		private boolean flag = false;//ѭ����־λ
        public TutorialThread(SurfaceHolder surfaceHolder, GameView gameView) {//������
            this.surfaceHolder = surfaceHolder;//�õ�SurfaceHolder����
            this.gameView = gameView;//�õ�GameView������
        }
        public void setFlag(boolean flag) {//����ѭ�����
        	this.flag = flag;
        }
		public void run() {//��д�ķ���
			Canvas c;//����
            while (this.flag) {//ѭ������
                c = null;
                try {
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder) {
                    	gameView.onDraw(c);//���û��Ʒ���
                    }
                } finally {//��finally��֤�������һ����ִ��
                    if (c != null) {
                    	//������Ļ��ʾ����
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try{
                	Thread.sleep(span);//˯��span����
                }catch(Exception e){//�����쳣��Ϣ
                	e.printStackTrace();//��ӡ�쳣��ջ��Ϣ
                }
            }
		}
	}

}
