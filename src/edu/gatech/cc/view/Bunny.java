package edu.gatech.cc.view;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;
import edu.gatech.cc.model.Curve3d;
import edu.gatech.cc.model.Shape3d;
import processing.core.*;

public class Bunny extends PApplet {
	Shape3d S0 = new Shape3d(this); 
	Curve3d C0; 
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
	}
	public void mouseDragged(){
		if (keyPressed && key=='1'){ 
			C0.move(0, this);
		}
		else if (keyPressed && key=='2'){
			C0.move(1, this);
		}
		else if (keyPressed && key=='3'){
			C0.move(2, this);
		}
		else if (keyPressed && key=='4'){	
			C0.move(3, this);
		}
	}
	public void mousePressed() {
	
	}
	public void mouseReleased() {
	}
	public void keyReleased() {
	}
	public void keyPressed() {
		if (key=='f') for (int i=0; i<3; i++) S0 = S0.refine(); 
	}
}