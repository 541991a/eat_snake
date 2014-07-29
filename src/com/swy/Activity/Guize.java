package com.swy.Activity;
import java.util.List;
import java.util.Random;

import com.swy.vo.Apple;
import com.swy.vo.Dot;
import com.swy.vo.Snake;

public class Guize {
	//ˢ���ߵĸ�������
	public Snake refreshSnake(Snake snake)
	{
		//�����ߵ��ƶ�������ƶ��ٶ�ˢ���ߵ�ͷ�������
		switch (snake.getDir())
		{
		    case 0:
		    	snake.setHeaddot(new Dot(snake.getHeaddot().getX() + snake.getSudo(),snake.getHeaddot().getY()));
		        break;
		    case 1:
		    	snake.setHeaddot(new Dot(snake.getHeaddot().getX(),snake.getHeaddot().getY()+ snake.getSudo()));
		    	break;
		    case 2:
		    	snake.setHeaddot(new Dot(snake.getHeaddot().getX() - snake.getSudo(),snake.getHeaddot().getY()));
		    	break;
		    case 3:
		    	snake.setHeaddot(new Dot(snake.getHeaddot().getX(),snake.getHeaddot().getY() - snake.getSudo()));
		    	break;
		}
		
		//�����б��е�ͷ�������
		snake.getTrandot().set(snake.getTrandot().size()-1, snake.getHeaddot());
		//���ݸ����ڵ������β���ƶ�����ˢ��β���������
		snake = getTraildot(snake);
		return snake;
	}
	
	//ˢ����β����
	public Snake getTraildot(Snake snake){
		    float x = 0,y = 0;
			float length = snake.getLength();
			
			if (snake.getTrandot().size()>2)
			{
				for (int i = snake.getTrandot().size()-1;i>1;i--)
				{
					length = length - getDotLength(snake.getTrandot().get(i), snake.getTrandot().get(i-1));
				}
				if (length<=0)
				{
					snake.setTraildir(gettraildir(snake.getTrandot().get(1), snake.getTrandot().get(2)));
					snake.getTrandot().remove(1);
					getTraildot(snake);
				}
			}
			
			
			switch (snake.getTraildir())
			{
			  case 0:
				  x = snake.getTrandot().get(1).getX()-length;
				  y = snake.getTrandot().get(1).getY();
				  break;
			  case 1:
				  x = snake.getTrandot().get(1).getX();
				  y = snake.getTrandot().get(1).getY()-length;
				  break;
			  case 2:
				  x = snake.getTrandot().get(1).getX()+length;
				  y = snake.getTrandot().get(1).getY();
				  break;
			  case 3:
				  x = snake.getTrandot().get(1).getX();
				  y = snake.getTrandot().get(1).getY()+length;
				  break;
			}
			snake.setTraildot(new Dot(x, y));
			snake.getTrandot().set(0, snake.getTraildot()); 
		    return snake;
		
	}
    //��������
	public float getDotLength(Dot dot1,Dot dot2){
	     return (float) Math.sqrt(Math.pow((dot1.getX()-dot2.getX()),2)+ Math.pow((dot1.getY()-dot2.getY()),2));
	}
	
	//�������������ж��䷽��
	public int gettraildir(Dot dot1,Dot dot2){
		int traildir = 0;
		if (dot2.getX()>dot1.getX()&& dot2.getY()==dot1.getY())
		{
			traildir = 0;
		}
		else if(dot2.getX()==dot1.getX()&& dot2.getY()>dot1.getY())
		{
			traildir = 1;
		}
		else if(dot2.getX()<dot1.getX()&& dot2.getY()==dot1.getY())
		{
			traildir = 2;
		}
		else if(dot2.getX()==dot1.getX()&& dot2.getY()<dot1.getY())
		{
			traildir = 3;
		}
		return traildir;
	} 
    //���һ��ƻ��
    public List<Apple> addApple(int width,int height,List<Apple> applelist,int banjing)
    {
    	Random random = new Random();
    	float x = (float)(random.nextInt(width-banjing-5)%(width-banjing-5-banjing-5+1) + banjing+5);
    	float y = (float)(random.nextInt(height-banjing-5)%(height-banjing-5-banjing-5+1) + banjing+5);
    	Dot dot = new Dot(x, y);
    	if (applelist.size()>0)
    	{
    		for (int i = 0;i<applelist.size()-1;i++)
    		{
    			if(getDotLength(applelist.get(i).getMiddledot(), dot)<=5)
    			{
    				addApple(width,height,applelist, banjing);
    			}
    		}
    	}
    	Apple apple = new  Apple(dot, banjing);
    	applelist.add(apple);
    	return applelist;
    	
    }
}
