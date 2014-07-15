package edu.gatech.cc.view;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;
import edu.gatech.cc.model.Curve3d;
import edu.gatech.cc.model.Shape3d;
import processing.core.*;

public class BunnyCurve extends PApplet {
	Shape3d S0 = new Shape3d(this); 
	Curve3d C0; 
	int mode =1; 
	String bmode = "normal"; 
	v3d[] P = new v3d[4]; 
	public void setup() {
		this.size(1280, 720, PApplet.P3D); 
		view.initView();
		S0.declareVectors(); 
		S0.loadMeshVTS(this); 
		S0 = S0.computeBox();
		P = new v3d[4]; 
		P[0] = v3d.pt(S0.Wbox); P[0].add(-S0.rbox*1.2, view.I); 
		P[1] = v3d.pt(S0.Wbox); P[1].add(-S0.rbox*0.4, view.I); 
		P[2] = v3d.pt(S0.Wbox); P[2].add(S0.rbox*0.4, view.I); 
		P[3] = v3d.pt(S0.Wbox); P[3].add(S0.rbox*1.2, view.I); 
		C0 = new Curve3d(P, 100); 
		S0.register(C0);
		C0.saveFrames();
		textAlign(PApplet.LEFT, PApplet.TOP);
	}
	public void draw() {  
	  background(255);
	  view.setupView(this);
	  fill(255,255, 0); 
	  this.noStroke(); 
	  S0.showFront(this);
	  C0.show(this);
	 // this.sphere(30);
	  if (keyPressed && key=='r'&& mousePressed) {
		  view.rotate(this);
	  } // rotate E around F
	  else if (keyPressed && key=='z' && mousePressed){
		  view.zoom(this);
	  }
	  else if (keyPressed && key=='t' && mousePressed){
		  view.translate(this);
	  }
	  //
	  camera(); 
	  scribe(); 
	}
	public void scribe(){
		this.textSize(32);
		this.fill(0);
		//this.text("vol error: "+S0.volError(), 10, 10);
		this.text("correction mode: "+bmode, 500, 10);
	}
	public void mouseDragged(){
		if (keyPressed && key=='1'){ 
			C0.move(0, this);
			S0.transform(C0, mode); 
		}
		else if (keyPressed && key=='2'){
			C0.move(1, this);
			S0.transform(C0, mode); 
		}
		else if (keyPressed && key=='3'){
			C0.move(2, this);
			S0.transform(C0, mode); 
		}
		else if (keyPressed && key=='4'){	
			C0.move(3, this);
			S0.transform(C0, mode); 
		}
	}
	public void mousePressed() {
		
	}
	public void mouseReleased() {
		
	}
	public void keyReleased() {
		
	}
	public void keyPressed() {
		if (key=='f') { 
			for (int i=0; i<3; i++) S0 = S0.refine(); 
			S0.register(C0);
		}
		if (key=='c'){
			System.out.println("save a picture"); 
			this.saveFrame("bunny_curve-####.png"); 
		}
		if (key=='7'){
			mode = 1; bmode = "normal"; 
			S0.transform(C0, mode); 
		}
		if (key=='8'){
			mode = 2; bmode = "binormal"; 
			S0.transform(C0, mode); 
		}
		if (key=='9'){
			mode = 3; bmode = "radial"; 
			S0.transform(C0, mode); 
		}
	}
}