package edu.gatech.cc.geo;
import processing.core.PApplet;


public class v3d {
	double x;
	double y;
	double z;
	public v3d() { x=y=z=0; };
	public v3d(double px, double py, double pz) {
		x = px;
		y = py;
		z = pz;
	};
	public static v3d pt(double px, double py, double pz){
		return new v3d(px, py, pz); 
	}
	public static v3d vec(double vx, double vy, double vz){
		return new v3d(vx, vy, vz); 
	}
	public static v3d vec(v3d p, v3d q){
		return new v3d(q.x-p.x, q.y-p.y, q.z-p.z); 
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
	public v3d normalize() {
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
	public static v3d V(double x, double y, double z) {
		return new v3d(x, y, z);
	}; // make v3dtor (x,y,z)
	public static v3d V(v3d V) {
		return new v3d(V.x, V.y, V.z);
	}; // make copy of v3dtor V
	public static v3d A(v3d A, v3d B) {
		return new v3d(A.x + B.x, A.y + B.y, A.z + B.z);
	}; // A+B
	public static v3d A(v3d U, double s, v3d V) {
		return V(U.x + s * V.x, U.y + s * V.y, U.z + s * V.z);
	}; // U+sV
	public static v3d M(v3d U, v3d V) {
		return V(U.x - V.x, U.y - V.y, U.z - V.z);
	}; // U-V
	v3d M(v3d V) {
		return V(-V.x, -V.y, -V.z);
	}; // -V
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
			return V(0, 0, 0);
		else
			return V(1.0f/n, V);
	}; // V/||V||
	public static v3d U(v3d A, v3d B) {
		return U(V(A, B));
	}
	public static v3d cross(v3d U, v3d V) {
		return V(U.y * V.z - U.z * V.y, U.z * V.x - U.x * V.z, U.x * V.y - U.y
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
	
	public String toString(){
		return Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z);
	}
	
	public static double area(v3d A, v3d B, v3d C) {
		return (v3d.normal(A, B, C)).norm() / 2;
	}; // area of triangle

	v3d R(v3d V) {
		return V(-V.y, V.x, V.z);
	} // rotated 90 degrees in XY plane
	
	public void vert(PApplet pa){
		pa.vertex((float)x, (float)y, (float)z);
	}

	public static v3d R(v3d V, double a, v3d I, v3d J) {
		double x = dot(V, I), y = dot(V, J);
		double c = Math.cos(a), s = Math.sin(a);
		return A(V, V(x * c - x - y * s, I, x * s + y * c - y, J));
	}; // Rotated V by a parallel to plane (I,J)
}
