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
		if (pi <0 || pj <0) return; 
		C[pi][pj] = (C[pi][pj]).add( (pa.mouseY - pa.pmouseY), view.J);
		C[pi][pj] = (C[pi][pj]).add( (pa.mouseX - pa.pmouseX), view.I); 
		this.fillPts(step);
		this.computeFrame(); 
	}
	
	public void translate(PApplet pa){
		if (pi<0 || pj <0) return; 
		C[pi][pj] = (C[pi][pj]).add((pa.mouseX - pa.pmouseX), view.K);
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
		if (i==num-1) return gaussian(i-1, j); 
		if (i==0) return gaussian(1, j);
		if (j==num-1) return gaussian(i, j-1);
		if (j==0) return gaussian(i, 1);
		double a1 = v3d.angle(P[i][j], P[i-1][j], P[i][j+1]);
		double a2 = v3d.angle(P[i][j], P[i][j+1], P[i+1][j]); 
		double a3 = v3d.angle(P[i][j], P[i+1][j], P[i][j-1]); 
		double a4 = v3d.angle(P[i][j], P[i][j-1], P[i-1][j]); 
		return (Math.PI+Math.PI-a1-a2-a3-a4); 
	}
	double mean(int i, int j){ //mean curvature 
		if (i==num-1) return mean(i-1, j); 
		if (i==0) return mean(1, j);
		if (j==num-1) return mean(i, j-1);
		if (j==0) return mean(i, 1);
		v3d v1 = v3d.vec(P[i][j], P[i-1][j]).mul(
				v3d.cot(P[i][j-1], P[i][j], P[i-1][j]) + v3d.cot(P[i][j+1], P[i][j], P[i-1][j]) );
		v3d v2 = v3d.vec(P[i][j], P[i][j+1]).mul(
				v3d.cot(P[i-1][j], P[i][j], P[i][j+1]) + v3d.cot(P[i+1][j], P[i][j], P[i][j+1]));
		v3d v3 = v3d.vec(P[i][j], P[i+1][j]).mul(
				v3d.cot(P[i][j+1], P[i][j], P[i+1][j]) + v3d.cot(P[i][j-1], P[i][j], P[i+1][j]));
		v3d v4 = v3d.vec(P[i][j], P[i][j-1]).mul(
				v3d.cot(P[i+1][j], P[i][j], P[i][j-1]) + v3d.cot(P[i-1][j], P[i][j], P[i][j-1]));
		double m = v3d.dot(v1.add(v2).add(v3).add(v4), v3d.cross(T1(i, j), T2(i, j))); 
		return m/area(i, j); 
	}
	
	public color[][] mean_color = null;
	public color[][] gaussian_color = null; 
	public color[][] area_color = null; 
	
	public void computeColor(PApplet pa){
		double[][] mean_value = new double[num][num]; 
		double[][] gaussian_value = new double[num][num]; 
		double[][] area_value = new double[num][num]; 
		for (int i=0; i<num; i++){
			for (int j=0; j<num; j++){
				mean_value[i][j] = this.mean(i, j); 
				gaussian_value[i][j] = this.gaussian(i, j); 
				area_value[i][j] = this.area(i, j); 
			}
		}
		mean_color = color.map(mean_value); 
		gaussian_color = color.map(gaussian_value); 
		area_color = color.map(area_value); 
	}
	/*------------frame---------------*/
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
		pa.fill(150, 200);
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
	
	public void showColor(color[][] tone, PApplet pa){
		pa.noStroke(); 
		pa.beginShape(PApplet.QUADS); 
		for (int i=0; i<num-1; i++){
			for (int j=0; j<num-1; j++){
				color.fill(tone[i][j], pa);
				P[i][j].vert(pa); 
				color.fill(tone[i][j+1], pa);
				P[i][j+1].vert(pa);
				color.fill(tone[i+1][j+1], pa);
				P[i+1][j+1].vert(pa);
				color.fill(tone[i+1][j], pa);
				P[i+1][j].vert(pa);
			}
		} 
		pa.endShape(); 
		pa.sphereDetail(10);
		color.fill(color.ruby(), pa);
		for (int i=0; i<4; i++){
			for (int j=0; j<4; j++){
				C[i][j].show(6, pa);
			}
		}
	}
	
	public void showCtrl(PApplet pa){
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
