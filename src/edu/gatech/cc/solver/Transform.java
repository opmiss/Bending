package edu.gatech.cc.solver;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.model.CurveFrame;
import edu.gatech.cc.model.SurfaceFrame;

public class Transform {
	
	public static v3d map(v3d P0, CurveFrame F0, CurveFrame F1, int mapUsed) {  
		v3d Pu = unbend(P0, F0, mapUsed);
		v3d Pt = transfer(Pu, F0, F1, mapUsed);
		v3d P1 = bend(Pt, F1, mapUsed); 
		return P1; 
	}
	
	public static v3d map(v3d P0, SurfaceFrame F0, SurfaceFrame F1){
		v3d Pu = unbend(P0, F0);
		v3d Pt = transfer(Pu, F0, F1);
		v3d P1 = bend(Pt, F1); 
		return P1; 
	}
	
	public static v3d unbend(v3d P, CurveFrame F0, int mapUsed) {
		switch (mapUsed){
		case 1:
			return normalUnbend(P, F0); 
		case 2:
			return binormalUnbend(P, F0); 
		case 3:
			return radialUnbend(P, F0); 
		}
		return P; 
	}
	
	public static v3d transfer(v3d P, CurveFrame F0, CurveFrame F1, int mapUsed){
		v3d V = v3d.vec(F0.getO(), P); 
		double n = v3d.dot(V, F0.getPN());
		double b = v3d.dot(V, F0.getPB());
		double t = v3d.dot(V, F0.getT());
		double o = F1.getL()/F0.getL(); 
		t = t*o; 
		switch (mapUsed){
		case 1:
			n = n/o; break; 
		case 2:
			b = b/o; break; 
		case 3:
			n = n/Math.sqrt(o); 
			b = b/Math.sqrt(o); 
			break; 
		}
		V = v3d.vec(n, F1.getPN(), b, F1.getPB(), t, F1.getT()); 
		//V = v3d.vec(n, F1.getPN(), b, F1.getPB(), 0, F1.getT()); 
		v3d P1 = v3d.pt(F1.getO()); 
		P1.add(V); 
		return P1; 
	}
	
	public static v3d bend(v3d P, CurveFrame F1, int mapUsed) {
		switch (mapUsed){
		case 1:
			return normalBend(P, F1); 
		case 2:
			return binormalBend(P, F1); 
		case 3:
			return radialBend(P, F1); 
		}
		return P;  
	}
	
	public static v3d unbend(v3d P, SurfaceFrame F0) {
		
		return null; 
	}
	
	public static v3d bend(v3d P, SurfaceFrame F1) {
		
		return null; 
	}
	
	public static v3d transfer(v3d P, SurfaceFrame F0, SurfaceFrame F1){
		v3d V = v3d.vec(F0.getO(), P); 
		double h = v3d.dot(V, F0.getN()); 
		double t1 = v3d.dot(V, F0.getT1()); 
		double t2 = v3d.dot(V, F0.getT2()); 
		h = h*F0.getA()/F1.getA();
		t1 = t1/F0.getL1()*F0.getL2(); 
		t2 = t2/F0.getL2()*F0.getL2();
		V = v3d.vec(h, F1.getN(), t1, F1.getT1(), t2, F1.getT2());
		v3d P1 = v3d.pt(F1.getO()); 
		P1 = P1.add(V); 
		return P1; 
	}
	public static v3d normalUnbend(v3d P, CurveFrame F0){
		v3d V = v3d.vec(F0.getO(), P); 
		double x = v3d.dot(V, F0.getFN());
		v3d Vn = v3d.vec(F0.getFN()); 
		Vn = Vn.mul(x);
		V = V.sub(Vn); 
		double sx = Root.backwardStratifyN(x*F0.getK());
		V = V.add(sx, Vn);
		v3d Pu = v3d.pt(F0.getO());
		return Pu.add(V); 
	}
	public static v3d normalBend(v3d P, CurveFrame F1){
		v3d V = v3d.vec(F1.getO(), P);
		double x = v3d.dot(V, F1.getFN()); 
		v3d Vn = v3d.vec(F1.getFN()); 
		Vn = Vn.mul(x);
		V = V.sub(Vn); 
		double sx = Root.forwardStratifyN(x*F1.getK()); 
		V = V.add(sx, Vn);
		v3d Pu = v3d.pt(F1.getO()); 
		return Pu.add(V); 
	}
	
	public static v3d binormalUnbend(v3d P, CurveFrame F0){
		v3d V = v3d.vec(F0.getO(), P); 
		double x = v3d.dot(V, F0.getFN());
		v3d Vn = v3d.vec(F0.getFN()); 
		Vn = Vn.mul(x);
		v3d Vb = v3d.vec(V);
		Vb = Vb.sub(Vn);
		double sy = Root.backwardStratifyB(x*F0.getK());
		Vb = Vb.mul(sy);
		V = Vn.add(Vb);
		v3d Pu = v3d.pt(F0.getO()); 
		return Pu.add(V); 
	}
	public static v3d binormalBend(v3d P, CurveFrame F1){
		v3d V = v3d.vec(F1.getO(), P); 
		double x = v3d.dot(V, F1.getFN());
		v3d Vn = v3d.vec(F1.getFN()); 
		Vn = Vn.mul(x);
		v3d Vb = v3d.vec(V);
		Vb = Vb.sub(Vn);
		double sy = Root.backwardStratifyB(x*F1.getK());
		Vb = Vb.mul(sy);
		V = Vn.add(Vb);
		v3d Pu = v3d.pt(F1.getO()); 
		return Pu.add(V); 
	}
	public static v3d radialUnbend(v3d P, CurveFrame F0){
		v3d V = v3d.vec(F0.getO(), P);
		double x = v3d.dot(V, F0.getFN());
		double s = Root.backwardRadial(x*F0.getK()); 
		V.mul(s);
		v3d Pu = v3d.pt(F0.getO()); 
		return Pu.add(V); 
	}
	public static v3d radialBend(v3d P, CurveFrame F1){
		v3d V = v3d.vec(F1.getO(), P);
		double x = v3d.dot(V, F1.getFN());
		double s = Root.backwardRadial(x*F1.getK()); 
		V.mul(s);
		v3d Pu = v3d.pt(F1.getO()); 
		return Pu.add(V); 
	}
}
