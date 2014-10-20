package edu.gatech.cc.model;
import java.util.ArrayList;

import processing.core.PApplet;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;

public class Curve3d {
	public v3d[] ctrl; //control points 
	public int[] cid; 
	v3d[] pts; //curve points 
	int num; 
	double step; 
	public Curve3d(v3d[] Ctrl, int N){
		ctrl = Ctrl; 
		cid = new int[4]; 
		cid[0] = 0; cid[1] = N/3; cid[2] = cid[1]*2; cid[3] = N-1;  
		step = 1.0/N;
		num = N+1; 
		pts = new v3d[num]; 
		fillPts(step); 
		frame0 = new CurveFrame[num]; 
		frame = new CurveFrame[num];
		computeFrame();
		tiles = new Tiles(this, 4, 10, ShowMode.CHECKER);  
	}
	static final double dl = 6; 
	static final double two_dl = 12; 
	public void compute(){
		fillPts(step);
		computeFrame();
		tiles.createTiles();
	}
	
	public void setCylinder(){
		tiles = new Tiles(this, 16, 30, ShowMode.CHECKER);  
	}
	
	void fillPts(double d){
		int i=0;
		pts = new v3d[(int) (1.0/d)+1]; 
		for (double s=0; s<=1.0001; s+=d){ 
			pts[i++] = v3d.NI(0, ctrl[0], 1.0/3, ctrl[1], 2.0/3, ctrl[2], 1.0, ctrl[3], s); 
		}
		num=i; 
	}
	
	int pid = -1; 
	public void pick(v3d M){
		double dis = 100; 
		int id = -1; 
		for (int i=0; i<4; i++){
			double d = v3d.dis(ctrl[i], M); 
			if (d<dis) {dis = d; id =i; } 
		}
		pid = id; 
	}
	public void drop(){
		pid=-1; 
	}
	public void move(int i, PApplet pa){
		ctrl[i] = ctrl[i].add((pa.mouseY - pa.pmouseY)*0.5, view.J); 
		ctrl[i] = ctrl[i].add((pa.mouseX - pa.pmouseX)*0.5, view.I);
		compute();
	}
	public void bend(int i, PApplet pa){
		ctrl[i] = ctrl[i].add((pa.mouseY - pa.pmouseY)*0.5, view.J); 
		ctrl[i] = ctrl[i].add((pa.mouseX - pa.pmouseX)*0.5, view.I);
		this.fillPts(step);
		ArrayList<v3d> vlist = resample(dl);  
		num = vlist.size(); 
		pts = new v3d[num];
		for (int k=0; k<num; k++) pts[k] = vlist.get(k); 
		this.computeFrame();
		tiles.createTiles(); 
		//System.out.println("np---:"+tiles.np); 
	}
	public ArrayList<v3d> resample(double r) {
		ArrayList<v3d> NL = new ArrayList<v3d>();
		NL.add(pts[0]);
		if (num < 3){
			return NL;
		}
		v3d C = v3d.pt(pts[0]);
		int i = 0;
		boolean go = true;
		while (go) {
			int j = i;
			while (j < num-1 && v3d.dis(pts[j + 1], C) < r)
				j++; // last vertex in sphere(C,r);
			if (j >= num - 1 || NL.size() >=num )
				go = false;
			else {
				v3d A = pts[j], B = pts[j + 1];
				double a = v3d.dis2(A, B), b = v3d.dis(v3d.V(C, A), v3d.V(A, B)), c = v3d.dis2(C, A)
						- r*r;
				double s = (-b + Math.sqrt(b*b - a * c)) / a;
				if (Double.isNaN(s)) PApplet.println("resample distance: s is NaN");
				v3d ap = v3d.V(A, s, B); 
				NL.add(ap); 
				C.set(ap);
				i = j;
			}
		}
		return NL;
	}
	public void translate(int i, PApplet pa){
		ctrl[i] = ctrl[i].add((pa.mouseX - pa.pmouseX)*0.5, view.K); 
		compute(); 
	}
	
	int n(int i) {
		return (i >= num - 1)?(num-1):(i+1);
	}
	
	int p(int i) {
		return (i <= 0)?0:(i-1); 
	}

	double length(int i) { 
		if (i==0) return v3d.dis(pts[0], pts[n(0)])/dl;
		if (i==num-1) return v3d.dis(pts[num-1], pts[p(num-1)])/dl;
		return v3d.dis(pts[p(i)], pts[i]) + v3d.dis(pts[i], pts[n(i)])/two_dl;
	}
	
	v3d tangent(int i) {
		v3d p = v3d.vec(pts[p(i)], pts[n(i)]);
		return p; 
	}
	
	/*------------------------curve parameters---------------------------*/
	CurveFrame[] frame0;
	CurveFrame[] frame; 
	public void saveFrames(){
		for (int i=0;i<num; i++){ 
			frame0[i] = new CurveFrame(frame[i]); 
		}
	}
	CurveFrame getPFrame(int i){
		return frame[i]; 
	}
	public void computeFrame(){
		frame[0] = CurveFrame.first(pts[0], pts[1], pts[2]);
		for (int i=1; i<num-1; i++){
			frame[i] = CurveFrame.median(frame[i-1], pts[i-1], pts[i], pts[i+1]); 
		}
		frame[num-1] = CurveFrame.last(frame[num-2], pts[num-3], pts[num-2], pts[num-1]);  
	}
	/*----------------------------Display--------------------------------*/
	public Tiles tiles; 
	public void show(PApplet pa){
		tiles.show(pa);
	}
	public void showCtrl(PApplet pa){
		for (int i=0; i<4; i++) ctrl[i].show(32, pa);
	}
	public void showPts(PApplet pa){
		for (int i=0; i<num; i++) pts[i].show(3, pa);
	}
	public void showCtrlRings(PApplet pa){
		tiles.showCtrlRings(pa);
	}
	
}
