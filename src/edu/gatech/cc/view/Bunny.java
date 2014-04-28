package edu.gatech.cc.view;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;
import edu.gatech.cc.model.Shape3d;
import processing.core.*;

public class Bunny extends PApplet {
	view render; 
	Shape3d S0 = new Shape3d(this); 
	public void setup() {
		this.size(1280, 720, PApplet.P3D); 
		render.initView();
		S0.declareVectors(); 
		S0.loadMeshVTS(this); 
	}
	public void draw() {  
	  background(255);
	  view.setupView(this);
	  fill(255,255, 0); 
	  this.noStroke(); 
	  S0.showFront(this);
	 // box(200); 
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
	public void mousePressed() {
	}
	public void mouseDragged() {
	}
	public void mouseReleased() {
	}
	public void keyReleased() {
	}
	public void keyPressed() {
		if (key=='f') for (int i=0; i<3; i++) S0 = S0.refine(); 
	}
}