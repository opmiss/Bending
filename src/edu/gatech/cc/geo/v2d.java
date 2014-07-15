package edu.gatech.cc.geo;
import processing.core.PApplet;

public class v2d {
	public double x, y;
	
	public v2d(double a, double b){
		x=a; y=b; 
	}
	
	public v2d(v2d v){
		x=v.x; y=v.y; 
	}
	
	public v2d(v2d a, v2d b) {
		x = b.x - a.x;
		y = b.y - a.y;
	}
	
	public v2d s(double s, v2d P){
		return new v2d(this.x*(1.0-s) + P.x*s,  this.y*(1.0-s) + P.y*s); 
	}
	
	public double disTo(v2d p){
		return (new v2d(this, p).n());
	}
	
	public void setTo(v2d p){
		x = p.x; 
		y = p.y; 
	}
	
	public void setTo(double sx, double sy){
		x = sx; 
		y = sy; 
	}

	public void unit() {
		double n = this.n();
		if (n < 0.00001) {
			System.out.println("can not unit!"); 
			x = 0;
			y = 0;
		} else {
			x = x / n;
			y = y / n;
		}
	}

	double n() {
		return Math.sqrt(this.n2());
	}

	double n2() {
		return (x * x + y * y);
	}
	
	public v2d right(){
		return new v2d(-y, x); 
	}
	public v2d left(){
		return new v2d(y, -x); 
	}
	
	public void turnLeft(){
		double w = x;
		x = -y;
		y = w;
	}
	double dot(v2d v) {
		return x*v.x + y*v.y;
	}
	public static boolean isLeftTurn(v2d a, v2d b, v2d c) {
		v2d ab = new v2d(a, b);
		v2d bc = new v2d(b, c);
		ab.turnLeft();
		double d = ab.dot(bc);
		if (d >= 0)
			return true;
		else
			return false;
	}

	public void add(v2d V) {
		x += V.x;
		y += V.y;
	}
	
	public void back(){
		x = -x; 
		y = -y; 
	}

	public void add(double s, v2d V) {
		x += s * V.x;
		y += s * V.y;
	}
	
	public v2d a(double s, v2d V){
		return new v2d(x + s * V.x, y + s * V.y);
	}
	
	public static v2d mid(v2d a, v2d b){
		return new v2d((a.x+b.x)/2, (a.y+b.y)/2); 
	}
	
	public void add(double ax, double ay){
		x += ax; 
		y += ay;  
	}
	
	public static double solve(double kr){
		
		return (-1.0 + Math.sqrt(1+2.0*kr))/kr; 
	}
	
	public static double radius(v2d A, v2d B, v2d C) { // signed radius of curvature
		v2d AB = new v2d(A, B);
		v2d AC = new v2d(A, C);
		AC.unit();
		double v = A.disTo(C) / 2;
		double d = AB.dot(AC);
		v2d D = new v2d(A);
		D.add(d, AC);
		double h = (new v2d(B, D)).n();
		if (h < 0.00001)
			h = (double) 0.00001;
		double r = v * v / (double) 2 / h;
		if (r > 1000000)
			r = 1000001;
		if (isLeftTurn(A, B, C))
			return -r;
		else
			return r;
	}
	
	public void vert(PApplet pa){
		pa.vertex((float)x, (float)y);
	}
	
	public void show(PApplet pa){
		pa.ellipse((float)x, (float)y, 8, 8);
	}
	
	public void show(float r, PApplet pa){
		pa.ellipse((float)x, (float)y, r, r);
	}
	
	public v2d map(v2d C, v2d O, double R){
		//r -- original offset dis
		double r = this.y-C.y; 
		boolean Pabove = r<0?true:false; 
		if (Pabove) r=-r; 
		//l -- dis from projection to C
		double l = this.x-C.x; 
		double a = l/R; 
		v2d X = new v2d(C.x + R*Math.sin(a), C.y + R*(1-Math.cos(a))); 
		v2d oft = new v2d(O, X);
		oft.unit(); 
		boolean Obelow = O.y>C.y?true:false; 
		
		double h; 
		if (Obelow){
			if (Pabove){
				h = Math.sqrt(R*R + R*r*2)-R;  
				return X.a(h, oft); 
			}
			else {
				h = Math.sqrt(R*R - R*r*2)-R;  
				return X.a(h, oft);
			}
		}
		else {
			if (Pabove){
				h = Math.sqrt(R*R - R*r*2)-R;  
				return X.a(h, oft); 
			}
			else {
				h = Math.sqrt(R*R + R*r*2)-R;  
				return X.a(h, oft);
			}
		}
	}
	
	public v2d noMap(v2d C, v2d O, double R){
		//r -- original offset dis
		double r = this.y-C.y; 
		boolean Pabove = r<0?true:false; 
		if (Pabove) r=-r;
		//l -- dis from projection to C
		double l = this.x-C.x; 
		double a = l/R; 
		v2d X = new v2d(C.x + R*Math.sin(a), C.y + R*(1-Math.cos(a))); 
		v2d oft = new v2d(O, X);
		oft.unit(); 
		boolean Obelow = O.y>C.y?true:false; 
		if (Obelow){
			if (Pabove){
				return X.a(r, oft); 
			}
			else { 
				return X.a(-r, oft);
			}
		}
		else {
			if (Pabove){
				return X.a(-r, oft); 
			}
			else { 
				return X.a(r, oft);
			}
		}
	}
	
	@Override
	public String toString(){
		return Double.toString(x)+", "+Double.toString(y); 
	}
	public void print(){
		System.out.println(this.toString()); 
	}
	
	public v2d map(v2d O0, double R0, v2d O1, double R1){
		//TODO: dot product to fix the sign 
		//v2d v0 = new v2d(O0, this);
		double h = this.disTo(O0); 
		double t = R1/R0*(h*h) - R1*R0 + R1*R1;
		t = Math.sqrt(t); 
		v2d V = new v2d(O1, this);
		V.unit(); 
		v2d r = new v2d(O1);
		r.add(t, V);
		return r; 
	}

	public v2d map(v2d O0, double R0, double L0, v2d O1, double R1, double L1){
		//TODO: dot product to fix the sign 
		v2d v0 = new v2d(O0, this);
		double h = this.disTo(O0); 
		double t = L0/R0*(h*h) - L0*R0 + L1*R1;
		t = t*R1/L1;
		t = Math.sqrt(t); 
		v2d V = new v2d(O1, this);
		V.unit(); 
		v2d r = new v2d(O1);
		r.add(t, V);
		return r; 
	}
	// Interpolation non-uniform (Neville's algorithm)
	public static v2d NI(double a, v2d A, double b, v2d B, double t) {
		return A.s((t - a)/(b - a), B);
	} // P(a)=A, P(b)=B

	public static v2d NI(double a, v2d A, double b, v2d B, double c, v2d C, double t) {
		v2d P = NI(a, A, b, B, t);
		v2d Q = NI(b, B, c, C, t);
		return NI(a, P, c, Q, t);
	} // P(a)=A, P(b)=B, P(c)=C

	public static v2d NI(double a, v2d A, double b, v2d B, double c, v2d C, double d, v2d D, double t) {
		v2d P = NI(a, A, b, B, c, C, t);
		v2d Q = NI(b, B, c, C, d, D, t);
		return NI(a, P, d, Q, t);
	} // P(a)=A, P(b)=B, P(c)=C, P(d)=D
}
