package com.swy.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class SnakeActivity extends Activity {
    /** Called when the activity is first created. */
	Handler myHandler = new Handler(){//��������UI�߳��еĿؼ�
        public void handleMessage(Message msg) {
        	if(msg.what == 1){	//WelcomeView��HelpView��GameView��������Ϣ���л���MenuView
        		initMenuView();//��ʼ�����л����˵�����
        	}
        	else if(msg.what == 2){//MenuView��������Ϣ���л���GameView
        		initGameView();//��ʼ�����л�����Ϸ����
        	}
        	else if(msg.what == 3){//MenuView��������Ϣ���л���HelpView
        		initHelpView();//��ʼ�����л�����������
        	}
        }
	}; 	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.initMenuView();
    }
    
    public void initMenuView() {
		this.setContentView(new MenuView(this, this));
		
	}

	public void initHelpView() {
		// TODO Auto-generated method stub
		
	}
	public void initGameView() {
		GameView gv = new GameView(this, this);
		gv.setFocusable(true);
		gv.setFocusableInTouchMode(true);
		this.setContentView(gv);
		
	}
}