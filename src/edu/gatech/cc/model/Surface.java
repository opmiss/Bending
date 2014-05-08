package edu.gatech.cc.model;
import processing.core.PApplet;
import edu.gatech.cc.geo.color;
import edu.gatech.cc.geo.v2d;
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
		frame = new SurfaceFrame[num][num]; 
		frame0 = new SurfaceFrame[num][num]; 
		computeFrame(); 
	}
	static final double dl = 6; 
	static final double two_dl = 12; 
	
	void fillPts(double d){
		int i=0, j=0; 
		v3d[] T = new v3d[4]; 
		for (double s=0; s<=1.0001; s+=d){ 
			for (int k=0; k<4; k++) T[k] = v3d.NI(0, C[k][0], 1.0/3, C[k][1], 2.0/3, C[k][2], 1.0, C[k][3],s); 
			for (double t=0; t<=1.0001; t+=d){
				P[i][j++] = v3d.NI(0, T[0], 1.0/3, T[1], 2.0/3, T[2], 1.0, T[3], t); 
			}
			i++; j=0;
		}
		num=i; 
	}
	int pi = -1, pj = -1; 
	public void pick(PApplet pa){
		double dis = 50; 
		for (int i=0; i<4; i++){
			for (int j=0; j<4; j++){
				double d = C[i][j].toScreen(pa).disTo(new v2d(pa.mouseX, pa.mouseY));  
				if (d<dis) {
					dis = d; pi = i; pj = j; 
				}
			}
		}
	}
	public void drop(){
		pi=-1; pj = -1;  
	}
	public void move(PApplet pa){
		if (pi <0 || pj <0) return ; 
		C[pi][pj] = (C[pi][pj]).add( (pa.mouseY - pa.pmouseY), view.J);
		C[pi][pj] = (C[pi][pj]).add( (pa.mouseX - pa.pmouseX), view.I); 
		this.fillPts(step);
		this.computeFrame(); 
	}
	int n(int i) {
		return (i >= num - 1)?(num-1):(i+1);
	}
	int p(int i) {
		return (i <= 0)?0:(i-1); 
	}
	v3d normal(int i, int j){
		return v3d.cross(T1(i, j), T2(i, j)).makeUnit(); 
	}
	double l1(int i, int j){
		double l = v3d.dis(P[p(i)][j], P[n(i)][j]); 
		if (i==0 || i==num-1) l*=2; 
		return l; 
	}
	double l2(int i, int j){
		double l = v3d.dis(P[i][p(j)], P[i][n(j)]); 
		if (j==0 || j==num-1) l*=2; 
		return l; 
	}
	v3d T1(int i, int j){
		v3d t = v3d.vec(P[p(i)][j], P[n(i)][j]); 
		t = t.makeUnit(); 
		return t; 
	}
	v3d T2(int i, int j){
		v3d t = v3d.vec(P[i][p(j)], P[i][n(j)]);
		t = t.makeUnit(); 
		return t; 
	}
	double area(int i, int j) {
		double area = v3d.area(P[p(i)][j], P[i][p(j)], P[i][j]) +
		v3d.area(P[p(i)][j], P[i][n(j)], P[i][j]) +
		v3d.area(P[n(i)][j], P[i][p(j)], P[i][j]) +
		v3d.area(P[n(i)][j], P[i][n(j)], P[i][j]);
		int scale=1; 
		if (i == num-1 || i==0) scale *=2; 
		if (j == num-1 || j==0) scale *=2; 
		area *=scale; 
		return area; 
	}
	
	SurfaceFrame computeFrame(int i, int j){
		v3d t1 = T1(i, j);
		v3d t2 = T2(i, j);
		v3d n = v3d.cross(t1, t2);
		n = n.makeUnit(); 
		return new SurfaceFrame(P[i][j], n, t1, t2, area(i, j), l1(i, j), l2(i, j)); 
	}
	
	double gaussian(int i, int j) { //gaussian curvature
		// TODO
		return 0; 
	}
	double mean(int i, int j){ //mean curvature 
		//TODO
		return 0; 
	}
	SurfaceFrame[][] frame0;
	SurfaceFrame[][] frame; 
	public void saveFrames(){
		for (int i=0; i<num; i++){
			for (int j=0; j<num; j++){
				frame0[i][j] = new SurfaceFrame(frame[i][j]); 
			}
		}
	}
	public void computeFrame(){
		for (int i=0; i<num; i++){
			for (int j=0; j<num; j++){
				frame[i][j] = computeFrame(i, j); 
			}
		}
	}
	
	/*----------display--------------*/
	public void show(PApplet pa){
		pa.stroke(200);
		pa.fill(150, 100);
		pa.beginShape(PApplet.QUADS); 
		for (int i=0; i<num-1; i++){
			for (int j=0; j<num-1; j++){
				P[i][j].vert(pa); 
				P[i][j+1].vert(pa);
				P[i+1][j+1].vert(pa);
				P[i+1][j].vert(pa);
			}
		} 
		pa.endShape(); 
		pa.noStroke();
		pa.sphereDetail(10);
		color.fill(color.ruby(), pa);
		for (int i=0; i<4; i++){
			for (int j=0; j<4; j++){
				C[i][j].show(6, pa);
			}
		}
	}
	
}
