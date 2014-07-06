package edu.gatech.cc.geo;
import processing.core.*;
public class view {
	public static v3d F = v3d.pt(0, 0, 0);
	static v3d T = v3d.pt(0, 0, 0);
	public static v3d E = v3d.pt(0, 0, 1000);
	public static v3d U = v3d.vec(0, 1, 0); // focus set with mouse when pressing ';', eye, and up vector
	public static v3d Q = v3d.pt(0, 0, 0);
	public static v3d I = v3d.vec(1, 0, 0);
	public static v3d J = v3d.vec(0, 1, 0);
	public static v3d K = v3d.vec(0, 0, 1); // picked surface point Q and screen aligned vectors {I,J,K} set when picked
	public static void initView() {
		Q = v3d.pt(0, 0, 0);
		I = v3d.vec(1, 0, 0);
		J = v3d.vec(0, 1, 0);
		K = v3d.vec(0, 0, 1);
		F = v3d.pt(0, 0, 0);
		E = v3d.pt(0, 0, 1000);
		U = v3d.vec(0, 1, 0);
	} // declares the local frames
	public static void setupView(PApplet pa){
		 pa.camera((float)E.x, (float)E.y, (float)E.z, 
				 (float)F.x, (float)F.y, (float)F.z, 
				 (float)U.x, (float)U.y, (float)U.z); // defines the view : eye, ctr, up
		 v3d Li=v3d.U(v3d.vec(E,F).add(0.1*v3d.dis(E,F),J));   // vec Li=U(A(vec.V(E,F),-d(E,F),J)); 
		 
		 pa.pointLight(255, 255, 255, (float)E.x, (float)E.y, (float)E.z); 
		 //pa.directionalLight(255,255,255,(float)Li.x,(float)Li.y,(float)Li.z); // direction of light: behind and above the viewer
		 
		 //pa.specular(255,255,0); pa.shininess(5);
	}
	public static void rotate(PApplet pa){
		 view.E = view.E.rotate(PApplet.PI * (pa.mouseX - pa.pmouseX) / pa.width, view.I, view.K, view.F);
		 view.E = view.E.rotate(-PApplet.PI * (pa.mouseY - pa.pmouseY) / pa.width, view.J, view.K, view.F);
	}
	public static void zoom(PApplet pa){
		 view.E = (view.E).add( -(pa.mouseY - pa.pmouseY), view.K);
	}
	public static void translate(PApplet pa){
		  view.E = (view.E).add( -(pa.mouseY-pa.pmouseY), view.J); 
		  view.F = (view.F).add( -(pa.mouseY-pa.pmouseY), view.J);
		  
		  view.E = (view.E).add( -(pa.mouseX-pa.pmouseX), view.I); 
		  view.F = (view.F).add( -(pa.mouseX-pa.pmouseX), view.I);
	}
}