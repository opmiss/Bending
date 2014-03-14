package edu.gatech.cc.model;
import processing.core.PApplet;
import edu.gatech.cc.geo.v2d;

public class Bezier2d {
	
	v2d[] C; //control points 
	v2d[] P; //curve points 
	int num; 
	
	Bezier2d(v2d[] Ctrl, int N){
		C = Ctrl; 
		double d = 1.0/N;
		num = N+1; 
		P = new v2d[num]; 
		fillPts(d); 
	}
	
	public void fillPts(double d){
		int i=0; 
		for (double s=0; s<=1; s+=d){
			v2d m1 = C[0].s(s, C[1]); 
			v2d m2 = C[1].s(s, C[2]); 
			v2d m3 = C[2].s(s, C[3]); 
			P[i++] = (m1.s(s, m2)).s(s, m2.s(s, m3)); 
		}
	}
	
	int pid = -1; 
	public void pick(v2d M){
		double dis = 100; 
		int id = -1; 
		for (int i=0; i<4; i++){
			double d = C[i].disTo(M); 
			if (d<dis) {dis = d; id =i; } 
		}
		pid = id; 
	}
	
	public void move(v2d M){
		if (pid<0) return; 
		C[pid].setTo(M);
		fillPts(1.0/(num-1)); 
	}
	// curvature, normal
	double[] K; // curvature 
	v2d[] N; // normal
	v2d[] T; // tangent
	double[] L; // local stretch 
	
	public void compute(){
		
	}

	int n(int i) {
		return (i >= num - 1)?(num-1):(i+1);
	}
	
	int p(int i) {
		return (i <= 0)?0:(i-1); 
	}

	double L(int i) { 
		if (i==0) return P[0].disTo(P[n(0)]);
		if (i==num-1) return P[num-1].disTo(P[p(num-1)]);
		return (P[p(i)].disTo(P[i]) + P[i].disTo(P[n(i)]));
	}

	v2d T(int i) { //return unit tangent at Pi
		//TODO: check boundary
		v2d p = new v2d(P[p(i)], P[n(i)]);
		p.unit(); return p; 
	}

	v2d N(int i) {
		return T(i).left();
	}

	double K(int i) {
		if (i==0) return K(1);
		if (i==num-1) return K(num-2); 
		return 1.0 /-v2d.radius(P[p(i)], P[i], P[n(i)]);
	}
	
	int closest(v2d X){
		//TODO: compute parameters 
		return 0; 
	}
	
	public void show(PApplet pa){
		pa.beginShape();
		for (int i=0; i<num; i++){
			P[i].vert(pa);
		}
		pa.endShape(); 
	}
	
}
