package com.swy.vo;
import java.util.List;

public class Snake {
	 Dot traildot;   //蛇尾点
	 Dot headdot;    //蛇头点
	 List<Dot> trandot;  //蛇的转折点的集合
	 float length;   //蛇身总长度
	 float sudo;     //移动速度
	 int dir;        //移动方向
	 int traildir;   //尾部移动方向
	 public int getTraildir() {
		return traildir;
	}
	public void setTraildir(int traildir) {
		this.traildir = traildir;
	}
	public int getDir() {
		return dir;
	}
	public void setDir(int dir) {
		this.dir = dir;
	}
	public List<Dot> getTrandot() {
		return trandot;
	}
	public void setTrandot(List<Dot> trandot) {
		this.trandot = trandot;
	}
	public Dot getTraildot() {
		return traildot;
	}
	public void setTraildot(Dot traildot) {
		this.traildot = traildot;
	}
	public Dot getHeaddot() {
		return headdot;
	}
	public void setHeaddot(Dot headdot) {
		this.headdot = headdot;
	}
	public float getLength() {
		return length;
	}
	public void setLength(float length) {
		this.length = length;
	}
	public float getSudo() {
		return sudo;
	}
	public void setSudo(float sudo) {
		this.sudo = sudo;
	}
	public Snake(Dot traildot, Dot headdot, List<Dot> trandot, float length,
			float sudo,int dir,int traildir) {
		super();
		this.traildot = traildot;
		this.headdot = headdot;
		this.trandot = trandot;
		this.length = length;
		this.sudo = sudo;
		this.dir = dir;
		this.traildir = traildir;
	}

}
