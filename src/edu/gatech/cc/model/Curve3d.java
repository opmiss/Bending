package edu.gatech.cc.model;
import processing.core.PApplet;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;

public class Curve3d {
	public v3d[] C; //control points 
	v3d[] P; //curve points 
	int num; 
	double step; 
	public Curve3d(v3d[] Ctrl, int N){
		C = Ctrl; 
		step = 1.0/N;
		num = N+1; 
		P = new v3d[num]; 
		fillPts(step); 
		computeFrame();
		tiles = new Tiles(this, 4, 10, ShowMode.STRIP); 
	}
	static final double dl = 6; 
	static final double two_dl = 12; 
	public void compute(){
		fillPts(step);
		computeFrame();
		tiles.createTiles();
	}
	void fillPts(double d){
		int i=0; 
		for (double s=0; s<=1.0001; s+=d){ 
			P[i++] = v3d.NI(0, C[0], 1.0/3, C[1], 2.0/3, C[2], 1.0, C[3], s); 
		}
		num=i; 
	}
	int pid = -1; 
	public void pick(v3d M){
		double dis = 100; 
		int id = -1; 
		for (int i=0; i<4; i++){
			double d = v3d.dis(C[i], M); 
			if (d<dis) {dis = d; id =i; } 
		}
		pid = id; 
	}
	public void drop(){
		pid=-1; 
	}
	
	public void move(int i, PApplet pa){
		C[i] = C[i].add((pa.mouseY - pa.pmouseY)*0.5, view.J); 
		C[i] = C[i].add((pa.mouseX - pa.pmouseX)*0.5, view.I);
		compute();
	}
	
	int n(int i) {
		return (i >= num - 1)?(num-1):(i+1);
	}
	
	int p(int i) {
		return (i <= 0)?0:(i-1); 
	}

	double length(int i) { 
		if (i==0) return v3d.dis(P[0], P[n(0)])/dl;
		if (i==num-1) return v3d.dis(P[num-1], P[p(num-1)])/dl;
		return v3d.dis(P[p(i)], P[i]) + v3d.dis(P[i], P[n(i)])/two_dl;
	}
	
	v3d tangent(int i) {
		v3d p = v3d.vec(P[p(i)], P[n(i)]);
		return p; 
	}
	
	/*------------------------curve parameters---------------------------*/
	Frame[] pFrame0;
	Frame[] pFrame; 
	
	public void saveFrames(){
		pFrame0 = new Frame[num]; 
		for (int i=0;i<num; i++){ 
			pFrame0[i] = new Frame(pFrame[i]); 
		}
	}
	
	Frame getPFrame(int i){
		return pFrame[i]; 
	}
	
	public void computeFrame(){
		pFrame = new Frame[num]; 
		pFrame[0] = Frame.first(P[0], P[1], P[2]);
		for (int i=1; i<num-1; i++){
			pFrame[i] = Frame.median(pFrame[i-1], P[i-1], P[i], P[i+1]); 
		}
		pFrame[num-1] = Frame.last(pFrame[num-2], P[num-3], P[num-2], P[num-1]);  
	}

	/*----------------------------Display--------------------------------*/
	Tiles tiles; 
	public void show(PApplet pa){
		tiles.show(pa);
	}
	
	public void showCtrl(PApplet pa){
		
	}
}
