package com.swy.vo;
import java.util.List;

public class Snake {
	 Dot traildot;   //��β��
	 Dot headdot;    //��ͷ��
	 List<Dot> trandot;  //�ߵ�ת�۵�ļ���
	 float length;   //�����ܳ���
	 float sudo;     //�ƶ��ٶ�
	 int dir;        //�ƶ�����
	 int traildir;   //β���ƶ�����
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
