package edu.gatech.cc.model;
import processing.core.PApplet;
import edu.gatech.cc.geo.v2d;

public class Bezier2d {
	
	v2d[] C; //control points 
	v2d[] P; //curve points 
	int num; 
	
	public Bezier2d(v2d[] Ctrl, int N){
		C = Ctrl; 
		double d = 1.0/N;
		num = N+1; 
		P = new v2d[num]; 
		fillPts(d); 
	}
	
	public void fillPts(double d){
		int i=0; 
		for (double s=0; s<=1.0001; s+=d){
			v2d m1 = C[0].s(s, C[1]); 
			v2d m2 = C[1].s(s, C[2]); 
			v2d m3 = C[2].s(s, C[3]); 
			P[i++] = (m1.s(s, m2)).s(s, m2.s(s, m3)); 
		}
		num=i; 
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
	public void drop(){
		pid=-1; 
	}
	
	
	public void move(v2d M){
		if (pid<0) return; 
		C[pid].setTo(M);
		fillPts(1.0/(num-1)); 
		computeGrid(); 
	}
	// curvature, normal
	double[] K; // curvature 
	v2d[] N; // normal
	double[] L; // local stretch 
	
	public void compute(){
		N = new v2d[num]; 
		for (int i=0; i<num; i++){
			N[i] = normal(i); 
		}
		K = new double[num];
		for (int i=0; i<num; i++){
			K[i] = curvature(i); 
		}
		L = new double[num]; 
		for (int i=0; i<num; i++){
			L[i] = length(i); 
		}
	}

	int n(int i) {
		return (i >= num - 1)?(num-1):(i+1);
	}
	
	int p(int i) {
		return (i <= 0)?0:(i-1); 
	}

	double length(int i) { 
		if (i==0) return P[0].disTo(P[n(0)]);
		if (i==num-1) return P[num-1].disTo(P[p(num-1)]);
		return (P[p(i)].disTo(P[i]) + P[i].disTo(P[n(i)]));
	}

	v2d tangent(int i) {
		v2d p = new v2d(P[p(i)], P[n(i)]);
		p.unit(); return p; 
	}

	v2d normal(int i) {
		return tangent(i).left();
	}

	double curvature(int i) {
		if (i==0) return curvature(1);
		if (i==num-1) return curvature(num-2); 
		return 1.0 /-v2d.radius(P[p(i)], P[i], P[n(i)]);
	}
	
	v2d[][] Grid; 
	public void computeGrid(){
		compute(); 
		Grid = new v2d[4][num];
		double r = 20; 
		for (int j=0; j<4; j++){
			for (int i=0; i<num; i++){
				Grid[j][i] = P[i].a(j*r, N[i]); 
			}
		}
	}
	
	public void showGrid(PApplet pa){
		for (int j=0; j<3; j++){
			for (int i=0; i<num-1; i++){
				pa.beginShape();
				Grid[j][i].vert(pa);
				Grid[j][i+1].vert(pa);
				Grid[j+1][i+1].vert(pa);
				Grid[j+1][i].vert(pa);
				pa.endShape(PApplet.CLOSE); 
			}
		}
	}
	
	public void show(PApplet pa){
		pa.beginShape();
		for (int i=0; i<num; i++){
			P[i].vert(pa);
		}
		pa.endShape(); 
	}
	
	public void showCtrl(PApplet pa){
		for (int i=0; i<4; i++){
			C[i].show(pa);
		}
	}
	
}
