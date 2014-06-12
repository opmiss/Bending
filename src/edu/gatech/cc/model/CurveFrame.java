package edu.gatech.cc.model;
import edu.gatech.cc.geo.*;
import processing.core.*;

public class CurveFrame {
		private v3d T;  //tangent 
		private v3d pN; //propagated normal
		private v3d pB; //propagated binormal
		private v3d fN; //frenet normal
		private v3d fB; //frenet binormal
		private v3d O;  //frame center 
		double K; //curvature 
		double L; //stretch
		
		private CurveFrame(v3d t, v3d n, v3d b, v3d o, double l, double k){
			T = v3d.U(t); 
			setPN(n.makeUnit()); 
			pB = b.makeUnit(); 
			O = o; 
			L = l; 
			K = k; 
			fN = pN; //-
			fB = pB; //-
		}
	   /*public Frame(v3d t, v3d n, v3d o){
			T = t.makeUnit(); 
			setN(n.makeUnit()); 
			pB = v3d.cross(T, getN()).makeUnit(); 
			O = o; 
		}*/
		public CurveFrame(CurveFrame f){
			T = v3d.vec(f.T);
			pN = v3d.vec(f.pN);
			pB = v3d.vec(f.pB); 
			O = v3d.vec(f.O); 
			L = f.L; 
			K = f.K; 
			fN = pN; //-
			fB = pB; //-
		}
	/*	public static Frame Frenet(v3d prev, v3d cur, v3d next){
			v3d t = v3d.vec(prev, cur); 
			v3d b = v3d.cross(t, v3d.vec(cur, next)); 
			if (b.square()<0.001f) b = v3d.cross(t, new v3d(0, 0, 1));
			if (b.square()<0.001f) b = v3d.cross(t, new v3d(0, 1, 0));
			if (b.square()<0.001f) b = v3d.cross(t, new v3d(1, 0, 0));
			v3d n = v3d.cross(t, b);
			return new Frame(t, n, b, cur); 
		}*/
		public void correctAfter(CurveFrame P){
			/* This computes the normal propagated frame*/
			setPN((P.getPN()).makeProject(T)); 
			getPN().makeUnit(); 
			pB = (P.pB).makeProject(T); 
			pB.makeUnit(); 
		}
		public static CurveFrame first(v3d prev, v3d cur, v3d next){
			v3d t = v3d.vec(prev, cur); 
			v3d b = v3d.cross(t, new v3d(0, 0, 1));
			v3d n = v3d.cross(t, b);
			double l = v3d.dis(prev, cur); 
			double k = v3d.curvature(prev, cur, next); 
			return new CurveFrame(t, n, b, prev, l, k); 
		}
	   /*public static Frame firstFrenet(v3d prev, v3d cur, v3d next){
			v3d t = v3d.vec(prev, cur); 
			v3d b = v3d.cross(t, v3d.vec(cur, next)); 
			if (b.square()<0.001) b = v3d.cross(t, new v3d(0, 0, 1));
			if (b.square()<0.001) b = v3d.cross(t, new v3d(0, 1, 0));
			if (b.square()<0.001) b = v3d.cross(t, new v3d(1, 0, 0));
			v3d n = v3d.cross(t, b);
			return new Frame(t, n, b, prev); 
		}*/
		public static CurveFrame median(CurveFrame F, v3d prev, v3d cur, v3d next){
			v3d t0=v3d.vec(prev, cur);
			v3d t1=v3d.vec(cur, next); 
			v3d B0 = v3d.copy(F.pB); 
			v3d B = v3d.cross(t0, t1); 
			if (B.square()>0.01){
				B.set(B.makeUnit()); 
				t0.set(t0.makeUnit());
				t1.set(t1.makeUnit()); 
				double m = v3d.mixed(B0, B, t0); 
				t1.add(-1, t0); 
				v3d C = v3d.cross(B, t1);
				B0.add(m, C); 
			}
			v3d T = v3d.vec(cur, next);
			v3d N = v3d.cross(T, B0); 
			double l = (v3d.dis(prev, cur) +v3d.dis(cur, next))/2;
			double k = v3d.curvature(prev, cur, next); 
			return new CurveFrame(T, N, B0, cur, l, k); 
		}
		public static CurveFrame last(CurveFrame F, v3d prev, v3d cur, v3d next){
			v3d t = v3d.vec(cur, next); 
			v3d b = v3d.cross(F.getPN(), t);
			double l = v3d.dis(cur, next); 
			double k = v3d.curvature(prev, cur, next); 
			return new CurveFrame(t, F.getPN(), b, next, l, k); 
		}
		public v3d[] slice(double r, int num){
			v3d[] ret = new v3d[num]; 
			double da = Math.PI*2/num;
			double sa = 0; 
			for (int i=0; i<num; i++){
				ret[i] = v3d.pt(O); 
				ret[i] = ret[i].add(r*Math.cos(sa), getPN(), r*Math.sin(sa), pB); 
				sa+=da; 
			}
			return ret; 
		}
		public v3d getPN() {
			return pN;
		}
		public v3d getPB(){
			return pB; 
		}
		public v3d getFN(){
			return fN; 
		}
		public v3d getFB(){
			return fB; 
		}
		public v3d getO(){
			return O; 
		}
		public double getL(){
			return L; 
		}
		public double getK(){
			return K; 
		}
		public void setPN(v3d n) {
			pN = n;
		}
		public v3d getT(){
			return T; 
		}
}
