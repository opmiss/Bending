package edu.gatech.cc.model;

import processing.core.PApplet;
import edu.gatech.cc.geo.v3d;

public class Curve3d {
	v3d[] C; //control points 
	v3d[] P; //curve points 
	Frame[] F; //frames along the curve
	int num; 
	
	public Curve3d(v3d[] Ctrl, int N){
		C = Ctrl; 
		double d = 1.0/N;
		num = N+1; 
		P = new v3d[num]; 
		fillPts(d); 
	}
	
	static final double dl = 6; 
	static final double two_dl = 12; 
	
	public void fillPts(double d){
		int i=0; 
		for (double s=0; s<=1.0001; s+=d){ 
		//	P[i++] = v3d.NI(0, C[0], 1.0/3, C[1], 2.0/3, C[2], 1.0, C[3], s); 
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

	public void move(v3d M){
	}
	// curvature, normal
	double[] K; // curvature 
	v3d[] N; // normal
	double[] L; // local stretch, ratio of speed 

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

	/*v3d normal(int i) {
		//TODO
	}*/

	/*double curvature(int i) {
		if (i==0) return curvature(1);
		if (i==num-1) return curvature(num-2); 
		return 1.0 /-v3d.radius(P[p(i)], P[i], P[n(i)]);
	}*/
	
	public void show(PApplet pa){
		pa.beginShape();
		for (int i=0; i<num; i++){
			P[i].vert(pa);
		}
		pa.endShape(); 
	}
	
	public void showCtrl(PApplet pa){
	}
}
