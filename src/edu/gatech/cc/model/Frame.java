package edu.gatech.cc.model;
import edu.gatech.cc.geo.*;
import processing.core.*;
public class Frame {
		private v3d T; //tangent 
		private v3d pN; //normal
		private v3d pB; //binormal
		private v3d fN; 
		private v3d fB; 
		private v3d O; //frame center 
		double K; //curvature 
		double L; //stretch
		
		private Frame(v3d t, v3d n, v3d b, v3d o){
			T = v3d.U(t); 
			setN(n.makeUnit()); 
			pB = b.makeUnit(); 
			O = o; 
		}
		public Frame(v3d t, v3d n, v3d o){
			T = t.makeUnit(); 
			setN(n.makeUnit()); 
			pB = v3d.cross(T, getN()).makeUnit(); 
			O = o; 
		}
		public Frame(Frame f){
			T = v3d.vec(f.T);
			pN = v3d.vec(f.pN);
			pB = v3d.vec(f.pB); 
			O = v3d.vec(f.O); 
		}
		public static Frame Frenet(v3d prev, v3d cur, v3d next){
			v3d t = v3d.vec(prev, cur); 
			v3d b = v3d.cross(t, v3d.vec(cur, next)); 
			if (b.square()<0.001f) b = v3d.cross(t, new v3d(0, 0, 1));
			if (b.square()<0.001f) b = v3d.cross(t, new v3d(0, 1, 0));
			if (b.square()<0.001f) b = v3d.cross(t, new v3d(1, 0, 0));
			v3d n = v3d.cross(t, b);
			return new Frame(t, n, b, cur); 
		}
		public void correctAfter(Frame P){
			/* This computes the normal propagated frame*/
			setN((P.getN()).makeProject(T)); 
			getN().makeUnit(); 
			pB = (P.pB).makeProject(T); 
			pB.makeUnit(); 
		}
		public static Frame first(v3d prev, v3d cur, v3d next){
			v3d t = v3d.vec(prev, cur); 
			v3d b = v3d.cross(t, new v3d(0, 0, 1));
			v3d n = v3d.cross(t, b);
			return new Frame(t, n, b, prev); 
		}
		public static Frame firstFrenet(v3d prev, v3d cur, v3d next){
			v3d t = v3d.vec(prev, cur); 
			v3d b = v3d.cross(t, v3d.vec(cur, next)); 
			if (b.square()<0.001) b = v3d.cross(t, new v3d(0, 0, 1));
			if (b.square()<0.001) b = v3d.cross(t, new v3d(0, 1, 0));
			if (b.square()<0.001) b = v3d.cross(t, new v3d(1, 0, 0));
			v3d n = v3d.cross(t, b);
			return new Frame(t, n, b, prev); 
		}
		public static Frame median(Frame F, v3d prev, v3d cur, v3d next){
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
			return new Frame(T, N, B0, cur); 
		}
		public static Frame last(Frame F, v3d prev, v3d cur, v3d next){
			v3d t = v3d.vec(cur, next); 
			v3d b = v3d.cross(F.getN(), t);
			return new Frame(t, F.getN(), b, next); 
		}
		public v3d[] slice(double r, int num){
			v3d[] ret = new v3d[num]; 
			double da = Math.PI*2/num;
			double sa = 0; 
			for (int i=0; i<num; i++){
				ret[i] = v3d.pt(O); 
				ret[i] = ret[i].add(r*Math.cos(sa), getN(), r*Math.sin(sa), pB); 
				//ret[i].print(); 
				sa+=da; 
			}
			return ret; 
		}
		public v3d getN() {
			return pN;
		}
		public v3d getB(){
			return pB; 
		}
		public v3d getO(){
			return O; 
		}
		public void setN(v3d n) {
			pN = n;
		}
		public v3d getT(){
			return T; 
		}
}
