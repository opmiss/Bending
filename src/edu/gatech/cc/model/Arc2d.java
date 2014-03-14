package edu.gatech.cc.model;
import processing.core.PApplet;
import edu.gatech.cc.geo.v2d;
import static java.lang.Math.PI; 

public class Arc2d {
	/*O -- center of arc
	C -- midpoint of arc
	R -- radius 
	L -- length
	A -- radius 
	SA -- starting angle 
	EA -- end angle */
	
	public v2d O, C;
	public double R, L, A, SA, EA; 
	
	public static Arc2d Line(v2d c, double l){
		v2d o = new v2d(c.x, Double.MIN_VALUE); 
		double r = Double.MAX_VALUE; 
		double a = 0; 
		double sa = PI/2; 
		double ea = PI/2; 
		return new Arc2d(o, c, r, l, a, sa, ea); 
	}
	
	public Arc2d(v2d o, v2d c, double r, double l, double a, double sa, double ea){
		O = o; C = c; R = r; L = l; A = a; SA = sa; EA = ea; 
	}
	
	public Arc2d(Arc2d arc){
		O = new v2d(arc.O); C = new v2d(arc.C); 
		R = arc.R; L = arc.L; A = arc.A; SA = arc.SA; EA = arc.EA; 
	}
	
	public void show(PApplet pa){
		if (Math.abs(A) < 0.05) pa.line((float) (C.x - L/2), (float)C.y, (float)(C.x+L/2), (float)C.y);
		else pa.arc((float)O.x, (float)O.y, (float)R*2, (float)R*2, (float)(SA), (float)(EA));
	}
	public void bend(double da){
		A += da; 
		R = L/A; 
		if (R>0) {
			SA = PI/2 - A/2; 
			EA = PI/2 + A/2; 
		}
		else{
			SA = -PI/2 + A/2; 
			EA = -PI/2 - A/2; 
		}
		O.y = C.y - R;  
		if (R<0) R=-R; 
	}
	
	/*public void stretch(double dl){
		L+=dl*2; 
		double da = dl/R; 
		SA-=da; 
		EA+=da; 
		A = EA-SA;
		if (A<0) A=0; 
		L = A*R; 
	}*/
}
