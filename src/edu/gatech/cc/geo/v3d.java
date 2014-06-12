package edu.gatech.cc.geo;
import edu.gatech.cc.model.CurveFrame;
import edu.gatech.cc.model.SurfaceFrame;
import edu.gatech.cc.solver.Transform;
import processing.core.PApplet;


public class v3d {
	public double x;
	public double y;
	public double z;
	public v3d() { x=y=z=0; };
	public v3d(double px, double py, double pz){
		x = px;
		y = py;
		z = pz;
	};
	public static v3d pt(double px, double py, double pz){
		return new v3d(px, py, pz); 
	}
	public static v3d pt(v3d P){
		return new v3d(P.x, P.y, P.z); 
	}
	public static v3d vec(double vx, double vy, double vz){
		return new v3d(vx, vy, vz); 
	}
	public static v3d vec(v3d p, v3d q){
		return new v3d(q.x-p.x, q.y-p.y, q.z-p.z); 
	}
	public static v3d vec(double u, v3d U, double v, v3d V, double w, v3d W){
		return new v3d(u*U.x+v*V.x+w*W.x, u*U.y+v*V.y+w*W.y, u*U.z+v*V.z+w*W.z); 
	}
	public static v3d copy(v3d v){
		return new v3d(v.x, v.y, v.z); 
	}
	public v3d set(double px, double py, double pz) {
		x = px;
		y = py;
		z = pz;
		return this;
	};
	
	public v3d set(v3d V) {
		x = V.x;
		y = V.y;
		z = V.z;
		return this;
	};
	public v3d add(v3d V) {
		x += V.x;
		y += V.y;
		z += V.z;
		return this;
	};
	public v3d add(double s, v3d V) {
		x += s * V.x;
		y += s * V.y;
		z += s * V.z;
		return this;
	};
	public v3d add(double s, v3d V, double t, v3d U) {
		x += s * V.x + t * U.x;
		y += s * V.y + t * U.y;
		z += s * V.z + t * U.z;
		return this;
	};
	public v3d sub(v3d V) {
		x -= V.x;
		y -= V.y;
		z -= V.z;
		return this;
	};
	public v3d mul(double f) {
		x *= f;
		y *= f;
		z *= f;
		return this;
	};
	public v3d div(double f) {
		x /= f;
		y /= f;
		z /= f;
		return this;
	};
	v3d div(int f) {
		x /= f;
		y /= f;
		z /= f;
		return this;
	};
	v3d rev() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	};
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	};
	public double square(){
		return x*x + y*y + z*z;
	}
	public v3d makeUnit() {
		double n = norm();
		if (n > 0.000001) {
			div(n);
		};
		return this;
	};
	public static double mixed(v3d U, v3d V, v3d W) {
		return dot(U, v3d.cross(V, W));
	}; // (UxV)*W mixed product, determinant
	boolean clockwise(v3d U, v3d V, v3d W) {
		return mixed(U, V, W) >= 0;
	}; // (UxV)*W>0 U,V,W are clockwise
	public static boolean clockwise(v3d A, v3d B, v3d C, v3d D) {
		return mixed(v3d.vec(A, B), v3d.vec(A, C), v3d.vec(A, D)) >= 0;
	};
	static double volume(v3d A, v3d B, v3d C, v3d D) {
		return mixed(v3d.vec(A, B), v3d.vec(A, C), v3d.vec(A, D)) / 6;
	}; // volume of tet
	public static double mixed(v3d E, v3d A, v3d B, v3d C) {
		return mixed(v3d.V(E, A), v3d.V(E, B), v3d.V(E, C));
	} // det (EA EB EC) is >0 when E sees (A,B,C) clockwise
	public v3d rotate(double a, v3d I, v3d J) {
		double x = dot(this, I), y = dot(this, J);
		double c = Math.cos(a), s = Math.sin(a);
		add(x * c - x - y * s, I);
		add(x * s + y * c - y, J);
		return this;
	};
	public static v3d vec(v3d V) {
		return new v3d(V.x, V.y, V.z);
	}; // make copy of v3dtor V
	public static v3d A(v3d A, v3d B) {
		return new v3d(A.x + B.x, A.y + B.y, A.z + B.z);
	}; // A+B
	public static v3d mid(v3d A, v3d B) {
		return new v3d((A.x + B.x) / 2.0f, (A.y + B.y) / 2.0f,
				(A.z + B.z) / 2.0f);
	} // (A+B)/2
	public static v3d V(v3d A, double s, v3d B) {
		return new v3d(A.x + s * (B.x - A.x), A.y + s * (B.y - A.y), A.z + s
				* (B.z - A.z));
	}; // (1-s)A+sB
	v3d V(v3d A, v3d B, v3d C) {
		return new v3d((A.x+B.x+C.x)/3.0f, (A.y+B.y+C.y)/3.0f, (A.z+B.z+C.z)/3.0f);
	}; // (A+B+C)/3
	v3d V(v3d A, v3d B, v3d C, v3d D) {
		return V(V(A, B), V(C, D));
	}; // (A+B+C+D)/4
	public static v3d V(double s, v3d A) {
		return new v3d(s * A.x, s * A.y, s * A.z);
	}; // sA
	public static v3d V(double a, v3d A, double b, v3d B) {
		return A(V(a, A), V(b, B));
	} // aA+bB
	v3d V(double a, v3d A, double b, v3d B, double c, v3d C) {
		return A(V(a, A, b, B), V(c, C));
	} // aA+bB+cC
	public static v3d V(v3d P, v3d Q) {
		return new v3d(Q.x - P.x, Q.y - P.y, Q.z - P.z);
	}; // PQ
	public static v3d U(v3d V) {
		double n = V.norm();
		if (n < 0.000001)
			return vec(0, 0, 0);
		else
			return V(1.0f/n, V);
	}; // V/||V||
	public static v3d U(v3d A, v3d B) {
		return U(V(A, B));
	}
	public v3d makeProject(v3d n){
		//project to the plane with normal n
		n = n.makeUnit(); 
		double s= v3d.dot(this, n);
		n = n.mul(s); 
		return this.sub(n); 
	}
	public static v3d cross(v3d U, v3d V) {
		return vec(U.y * V.z - U.z * V.y, U.z * V.x - U.x * V.z, U.x * V.y - U.y
				* V.x);
	}; // UxV CROSS PRODUCT (normal to both)
	public static v3d normal(v3d A, v3d B, v3d C) {
		return cross(V(A, B), V(A, C));
	}; // normal to triangle (A,B,C), not normalized (proportional to area)
	v3d B(v3d U, v3d V) {
		return U(cross(cross(U, V), U));
	} // (UxV)xV unit normal to U in the plane UV
	public static double dot(v3d U, v3d V){
		return U.x*V.x+U.y*V.y+U.z*V.z; 
	}
	public static double dis(v3d P, v3d Q){
		double dx = (Q.x-P.x); 
		double dy = (Q.y-P.y); 
		double dz = (Q.z-P.z);
		return Math.sqrt(dx*dx+dy*dy+dz*dz); 
	}
	@Override 
	public String toString(){
		return Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z);
	}
	public void print(){
		System.out.println(this.toString()); 
	}
	public void print(String s){
		System.out.println(s+":"+this.toString()); 
	}
	public static double area(v3d A, v3d B, v3d C) {
		return (v3d.normal(A, B, C)).norm() / 2;
	}; // area of triangle
	
	public v2d toScreen(PApplet pa){
	/*	double ef = v3d.dis(view.E, view.F); 
		v3d EP = v3d.vec(view.E, this);
		double epf = v3d.dot(EP, view.K);
		//compute EPf
		v3d EPf = v3d.vec(view.K); 
		EPf = EPf.mul(epf); 
		//compute Pf
		v3d Pf = v3d.pt(view.E); 
		Pf = Pf.add(EPf); 
		
		v3d PPf = v3d.vec(Pf, this); 
		double x = v3d.dot(PPf, view.I);
		double y = v3d.dot(PPf, view.J);
		
		double s1 = ef/Math.abs(epf);*/
		
		v3d PF = v3d.vec(view.F, this); 
		double x = v3d.dot(PF, view.I); 
		double y = v3d.dot(PF, view.J); 
		
		double s = 59.0/49.0; 
		//System.out.println("s1:"+s1+", "+"s2:"+s2); 
		 
		x = x*s+pa.width/2; 
		y = y*s+pa.height/2;
		return new v2d(x, y); 
	}
	v3d R(v3d V) {
		return vec(-V.y, V.x, V.z);
	} // rotated 90 degrees in XY plane
	public void vert(PApplet pa){
		pa.vertex((float)x, (float)y, (float)z);
	}
	public v3d rotate(float a, v3d I, v3d J, v3d G) {
		double x = v3d.dot(v3d.vec(G, this), I), y = v3d.dot(v3d.vec(G, this), J);
		double c = Math.cos(a), s = Math.sin(a);
		return this.add(x * c - x - y * s, I, x * s + y * c - y, J);
	}; // Rotated by a around G in plane (I,J)
	public static v3d R(v3d V, double a, v3d I, v3d J) {
		double x = dot(V, I), y = dot(V, J);
		double c = Math.cos(a), s = Math.sin(a);
		return A(V, V(x * c - x - y * s, I, x * s + y * c - y, J));
	}; // Rotated V by a parallel to plane (I,J)
	public static v3d interpolate(v3d A, v3d B, double s){
		return new v3d(A.x +s*(B.x-A.x), A.y +s*(B.y-A.y), A.z +s*(B.z-A.z)); 
	}
	public v3d transform(v3d v, CurveFrame f, CurveFrame g){
		this.set(Transform.transfer(v, f, g, 3)); 
		return this; 
	}
	public v3d transform(v3d v, SurfaceFrame n, SurfaceFrame m){
		this.set(Transform.transfer(v, n, m)); 
		return this; 
	}
	// Interpolation non-uniform (Neville's algorithm)
	public static v3d NI(double a, v3d A, double b, v3d B, double t) {
		return interpolate(A, B, (t - a) / (b - a));
	} // P(a)=A, P(b)=B
	
	public static double curvature(v3d A, v3d B, v3d C) { // signed radius of curvature
		v3d AB = v3d.vec(A, B);
		v3d AC = v3d.vec(A, C);
		AC = AC.makeUnit();
		double v = v3d.dis(A, C)/2; 
		double d = v3d.dot(AB, AC); 
		v3d D = v3d.pt(A);
		D.add(d, AC);
		v3d H = v3d.vec(B, D); 
		double h = H.norm();
		if (h < 0.00001)
			h = (double) 0.00001;
		double k = (double) 2 * h/v/v;
		if (v3d.dot(H, view.J)>0) k = -k; 
		return k; 
	}

	public static v3d NI(double a, v3d A, double b, v3d B, double c, v3d C, double t) {
		v3d P = NI(a, A, b, B, t);
		v3d Q = NI(b, B, c, C, t);
		return NI(a, P, c, Q, t);
	} // P(a)=A, P(b)=B, P(c)=C

	public static v3d NI(double a, v3d A, double b, v3d B, double c, v3d C, double d, v3d D, double t) {
		v3d P = NI(a, A, b, B, c, C, t);
		v3d Q = NI(b, B, c, C, d, D, t);
		return NI(a, P, d, Q, t);
	} // P(a)=A, P(b)=B, P(c)=C, P(d)=D
	
	public void show(PApplet pa){
		pa.pushMatrix();
		pa.translate((float)x, (float)y, (float)z);
		pa.sphere(20);
		pa.popMatrix(); 
	}
	public void show(int r, PApplet pa){
		pa.pushMatrix();
		pa.translate((float)x, (float)y, (float)z);
		pa.sphere(r);
		pa.popMatrix(); 
	}
}
