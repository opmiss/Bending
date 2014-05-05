package edu.gatech.cc.model;
import processing.core.PApplet;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;

public class Surface {
	public v3d[][] C; //control points 
	public v3d[][] P; //surface points 
	
	int num; 
	double step; 
	public Surface(v3d[][] Ctrl, int N){
		C = Ctrl; 
		step = 1.0/N;
		num = N+1; 
		P = new v3d[num][num]; 
		fillPts(step); 
	}
	static final double dl = 6; 
	static final double two_dl = 12; 
	public void compute(){
		fillPts(step);
	}
	void fillPts(double d){
	/*	int i=0; 
		for (double s=0; s<=1.0001; s+=d){ 
			P[i++] = v3d.NI(0, C[0], 1.0/3, C[1], 2.0/3, C[2], 1.0, C[3], s); 
		}
		num=i; */
	}
	int pid = -1; 
	public void pick(){
		//TODO
	}
	public void drop(){
		pid=-1; 
	}
	
	public void move(int i, PApplet pa){
		//TODO
	}
	
	int n(int i) {
		return (i >= num - 1)?(num-1):(i+1);
	}
	
	int p(int i) {
		return (i <= 0)?0:(i-1); 
	}
	
	v3d normal(int i, int j){
		//TODO 
		return null; 
	}
	double area(int i, int j) {
		//TODO 
		return 0; 
	}
	double gaussian(int i, int j) { //gaussian curvature
		// TODO
		return 0; 
	}
	double mean(int i, int j){ //mean curvature 
		//TODO
		return 0; 
	}
}
