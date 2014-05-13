package edu.gatech.cc.solver;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.model.CurveFrame;
import edu.gatech.cc.model.SurfaceFrame;

public class bend {
	public static v3d map(v3d P, CurveFrame F, CurveFrame G, int mapUsed) {  
		return null; 
	}
	public static v3d map(v3d P, SurfaceFrame F, SurfaceFrame G, int mapUsed){
		//TODO
		return null; 
	}
	public static v3d unbend(v3d P, CurveFrame F, CurveFrame G, int mapUsed) {
		//TODO 
		return null; 
	}
	public static v3d transfer(v3d P, CurveFrame F, CurveFrame G){
		//TODO
		return null; 
	}
	public static v3d bend(v3d P, CurveFrame F, CurveFrame G) {
		//TODO
		return null; 
	}
	
	public static v3d normalUnbend(v3d P, CurveFrame F, CurveFrame G){
		//TODO
		return null; 
	}
	
	public static v3d normalBend(v3d P, CurveFrame F, CurveFrame G){
		//TODO 
		return null; 
	}
	
	public static v3d binormalUnbend(v3d P, CurveFrame F, CurveFrame G){
		//TODO
		return null; 
	}
	
	public static v3d binormalBend(){
		//TODO
		return null;
	}
	
	public static v3d radialUnbend(){
		//TODO
		return null;
	}
	public static v3d radialBend(){
		//TODO 
		return null; 
	}
	
	
	
	/*public static v3d Bxp(v3d Q, v3d C, v3d X) { //normal bend
		  vec N = vec.V(X, C); 
		  float r = geo.n(N); 
		  N=N.div(r); 
		  vec V = vec.V(C, Q); 
		  float x = geo.d(V, N);
		  vec Vn = N.mul(x); 
		  V=V.sub(Vn); 
		  float sx = forwardStratifyN(x/r); 
		  V=V.add(sx, Vn); 
		  return pt.P(C, V);
    }  
	public static v3d Uxp(v3d Q, v3d C, v3d X) { //normal unbend
		  vec N = vec.V(X, C); 
		  float r = geo.n(N); 
		  N=N.div(r); 
		  vec V = vec.V(C, Q); 
		  float x = geo.d(V, N);
		  vec Vn = N.mul(x); 
		  V=V.sub(Vn); 
		  float sx = backwardStratifyN(x/r); 
		  V=V.add(sx, Vn); 
		  return pt.P(C, V);
		  }
	public static v3d Byp(v3d Q, v3d C, v3d X) { //binormal bend
		  vec N = vec.V(X, C); 
		  float r = geo.n(N); 
		  N=N.div(r); 
		  vec V = vec.V(C, Q); 
		  float x = geo.d(V, N);
		  vec Vn = N.mul(x); 
		  vec Vb = V.sub(Vn); 
		  float sy = forwardStratifyB(x/r); 
		  Vb.mul(sy); 
		  V = Vn.add(Vb); 
		  return pt.P(C, V);
		  }
		  
	public static v3d Uyp(v3d Q, v3d C, v3d X) { //binormal unbend
		  vec N = vec.V(X, C); 
		  float r = geo.n(N); 
		  N=N.div(r); 
		  vec V = vec.V(C, Q); 
		  float x = geo.d(V, N);
		  vec Vn = N.mul(x); 
		  vec Vb = V.sub(Vn); 
		  float sy = backwardStratifyB(x/r); 
		  Vb.mul(sy); 
		  V = Vn.add(Vb); 
		  return pt.P(C, V);
		  }
		
	public static v3d Bsp(v3d Q, v3d C, v3d X) { //radial bend
		  vec N = vec.V(X, C); 
		  float r = geo.n(N); 
		  N=N.div(r); 
		  vec V = vec.V(C, Q); 
		  float x = geo.d(V, N);
		  float s = forwardRadial(x/r); 
		  V.mul(s);  
		  return pt.P(C, V);
		  }
		  
	public static v3d Usp(v3d Q, v3d C, v3d X) { //radial unbend
		  vec N = vec.V(X, C); 
		  float r = geo.n(N); 
		  N=N.div(r); 
		  vec V = vec.V(C, Q); 
		  float x = geo.d(V, N);
		  float s = backwardRadial(x/r); 
		  V.mul(s);  
		  return pt.P(C, V);
		  }

		// ****************helper functions **********************
	public static float forwardRadial(float krcosa) { 
		  if (Math.abs(krcosa)< root._bias) return 1.0f; 
		  float a = krcosa*2/3; 
		  if (Float.isNaN(a))  System.out.println("forwardRadial: a is NaN");
		  float x = root.cubicSolve(a); 
		  if (Float.isNaN(x))  System.out.println("forwardRadial ("+krcosa+") return NaN");
		  return x;
		  }
		  
	public static float backwardRadial(float krcosa) {
		  float s = (float) Math.sqrt(krcosa*2/3+1); 
		  if (Float.isNaN(s))  System.out.println("backwardRadial ("+krcosa+") retruns a NaN");
		  return s;
		  }
		  
	public static float forwardStratifyN(float kx) {
		  float a = kx/2; 
		  float x = root.squareSolve(a, 1.0f, -1.0f);  
		  return x;
		  }
		  
	public static float backwardStratifyN(float kx) {
		  return (1.0f+kx/2);
		  }

	public static float forwardStratifyB(float kx) {
		  return 1.0f/(kx+1);
		  }
		  
	public static float backwardStratifyB(float kx) {
		  return kx+1;
		  }*/
}
