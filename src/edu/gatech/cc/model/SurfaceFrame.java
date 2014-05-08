package edu.gatech.cc.model;

import edu.gatech.cc.geo.v3d;

public class SurfaceFrame {
	v3d O; 
	v3d N;
	v3d T1; 
	v3d T2; 
	double G; 
	double M; 
	double A; 
	double L1; 
	double L2; 
	public SurfaceFrame(){
		
	}
	public SurfaceFrame(SurfaceFrame n){
		O = v3d.pt(n.O);
		N = v3d.vec(n.N); 
		T1 = v3d.vec(n.T1);
		T2 = v3d.vec(n.T2);
		A = n.A; 
		L1 = n.L1; 
		L2 = n.L2; 
		G = n.G; 
		M = n.M;
	}
	public SurfaceFrame(v3d o, v3d n, v3d t1, v3d t2, double a, double l1, double l2){
		O = o; 
		N = n; 
		T1 = t1; 
		T2 = t2; 
		A = a; 
		L1 = l1; 
		L2 = l2; 
	}
	public void setCurvatures(double g, double m){
		G = g; 
		M = m; 
	}
	public v3d getO(){
		return O; 
	}
	public v3d getN(){
		return N; 
	}
	public v3d getT1(){
		return T1; 
	}
	public v3d getT2(){
		return T2; 
	}
	public double getL1(){
		return L1; 
	}
	public double getL2(){
		return L2; 
	}
	public double getG(){
		return G; 
	}
	public double getM(){
		return M; 
	}
	public double getA(){
		return A; 
	}
}
