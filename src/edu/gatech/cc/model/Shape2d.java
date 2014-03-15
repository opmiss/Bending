package edu.gatech.cc.model;
//import java.util.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.PI;

import processing.core.PApplet;
import edu.gatech.cc.geo.v2d;

public class Shape2d {
	public v2d[] P;
	public int num; 
	
	public v2d C; //centroid
	public double A; //area
	
	public Shape2d(v2d[] pts, int n){
		P = pts; 
		num = n; 
		C = this.centroid(); 
		A = this.area(); 
	}
	
	
	public Shape2d(Shape2d shape){
		P = new v2d[shape.num]; 
		num = shape.num; 
		for (int i=0 ; i< num; i++) P[i] = new v2d(shape.P[i]); 
		C = this.centroid(); 
		A = this.area(); 
	}
	
	public static Shape2d Layers(v2d O, double L, double H, int n, int m){
		v2d[] pts = new v2d[m*n]; 
		double dl = L/(n-1);
		double dh = H/(m-1);
		int i=0; 
		for (double y = O.y-H/2; y <= O.y+H/2; y+=dh){
			for (double x = O.x - L/2; x <= O.x + L/2; x+=dl){
				pts[i++] = new v2d(x, y); 
			}
		}
		return new Shape2d(pts, i); 
	}
	
	public void showLayers(int m, int n, PApplet pa){
		for (int i=0; i<m; i++){
			pa.beginShape(); 
			for (int j=0; j<n; j++){
				P[i*n+j].vert(pa);
			}
			pa.endShape(); 
		}
		pa.beginShape(); 
		for (int i=0; i<m; i++){
			P[i*n].vert(pa);
		}
		pa.endShape(); 
		pa.beginShape(); 
		for (int i=0; i<m; i++){
			P[i*n+(n-1)].vert(pa);
		}
		pa.endShape(); 
	}

	public static Shape2d CatHead(v2d O, double A, double B){
		v2d[] pts = new v2d[400]; 
		double da = Math.PI/180; 
		double a, x, y; 
		int i=0;  
		for (a= -PI/6; a < PI*6.9/6; a+=da){
			x = O.x + A*Math.cos(a);
			y = O.y + B*Math.sin(a); 
			pts[i] = new v2d(x, y); 
			i++; 
		}
		v2d left, right, peak, oft; 
		left = new v2d(O.x + A*cos(PI*7/6), O.y + B*Math.sin(PI*7/6)); 
		right = new v2d(O.x + A*cos(PI*8/6), O.y + B*Math.sin(PI*8/6)); 
		peak = v2d.mid(left, right); 
		oft = new v2d(right, left);
		oft.turnLeft();
		peak.add(1.2, oft);
		oft = new v2d(left, peak); 
		x = left.x; y = left.y; 
		double dx = oft.x/8, dy = oft.y/8; 
		for (int k=0; k<8; k++){
			pts[i] = new v2d(x, y); 
			x +=dx; y+=dy; i++; 
		}
		oft = new v2d(peak, right); 
		x = peak.x; y = peak.y; 
		dx = oft.x/8; dy = oft.y/8; 
		for (int k=0; k<8; k++){
			pts[i] = new v2d(x, y); 
			x +=dx; y+=dy; i++;
		}
		for (a = PI*8/6; a < PI*9.9/6; a+=da){
			x = O.x + A*cos(a);
			y = O.y + B*sin(a); 
			pts[i] = new v2d(x, y); 
			i++; 
		}
		left = new v2d(O.x + A*cos(PI*10/6), O.y + B*Math.sin(PI*10/6)); 
		right = new v2d(O.x + A*cos(PI*11/6), O.y + B*Math.sin(PI*11/6)); 
		peak = v2d.mid(left, right); 
		oft = new v2d(right, left);
		oft.turnLeft();
		peak.add(1.2, oft);
		oft = new v2d(left, peak); 
		x = left.x; y = left.y; 
		dx = oft.x/8; dy = oft.y/8; 
		for (int k=0; k<8; k++){
			pts[i] = new v2d(x, y); 
			x +=dx; y+=dy; i++; 
		}
		oft = new v2d(peak, right); 
		x = peak.x; y = peak.y; 
		dx = oft.x/8; dy = oft.y/8; 
		for (int k=0; k<8; k++){
			pts[i] = new v2d(x, y); 
			x +=dx; y+=dy; i++;
		}
		return new Shape2d(pts, i); 
	}
	
	public v2d centroid(){
		double cx = 0; 
		double cy = 0; 
		for (int i=0; i<num; i++){
			double xi = P[i].x; 
			double yi = P[i].y; 
			double xni, yni; 
			if (i==num-1){
				xni = P[0].x;
				yni = P[0].y; 
			}
			else{
				xni = P[i+1].x; 
				yni = P[i+1].y; 
			}
			cx += (xi+xni)*(xi*yni-xni*yi); 
			cy += (yi+yni)*(xi*yni-xni*yi); 
		}
		double area = area(); 
		return new v2d(cx/area/6, cy/area/6); 
	}

	public double area() {
		double A = 0;
		for (int i = 0; i < num; i++) {
			double xi = P[i].x;
			double yi = P[i].y;
			double xni, yni;
			if (i == num - 1) {
				xni = P[0].x;
				yni = P[0].y;
			} else {
				xni = P[i + 1].x;
				yni = P[i + 1].y;
			}
			A += (xi * yni - xni * yi);
		}
		return Math.abs(A) / 2;
	}
	
	public Shape2d Map(Arc2d C0, Arc2d C1){
		v2d[] pts = new v2d[num]; 
		for (int i=0; i<num; i++) {
			pts[i] = P[i].map(C0.C, C1.O, C1.R); 
			//pts[i] = P[i].noMap(C0.C, C1.O, C1.R); 
		}
		return new Shape2d(pts, num); 
	}

	public void show(PApplet pa){
		pa.beginShape();
		for (int i=0; i<num; i++){
			P[i].vert(pa);
		}
		pa.endShape(PApplet.CLOSE); 
	}
	
}
