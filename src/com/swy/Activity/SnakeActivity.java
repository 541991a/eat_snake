package com.swy.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class SnakeActivity extends Activity {
    /** Called when the activity is first created. */
	Handler myHandler = new Handler(){//用来更新UI线程中的控件
        public void handleMessage(Message msg) {
        	if(msg.what == 1){	//WelcomeView或HelpView或GameView传来的消息，切换到MenuView
        		initMenuView();//初始化并切换到菜单界面
        	}
        	else if(msg.what == 2){//MenuView传来的消息，切换到GameView
        		initGameView();//初始化并切换到游戏界面
        	}
        	else if(msg.what == 3){//MenuView传来的消息，切换到HelpView
        		initHelpView();//初始化并切换到帮助界面
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