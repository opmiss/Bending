package edu.gatech.cc.view;

import processing.core.PApplet;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;
import edu.gatech.cc.model.Surface;
import edu.gatech.cc.model.Shape3d;

public class BunnySurface extends PApplet{
	Shape3d S0 = new Shape3d(this); 
	Surface C0; 
	v3d[][] P; 
	public void setup() {
		this.size(1280, 720, PApplet.P3D); 
		view.initView();
		S0.declareVectors(); 
		S0.loadMeshVTS(this); 
		S0 = S0.computeBox();
		P = new v3d[4][4]; 
		for (int i=0; i<4; i++){
			for (int j=0; j<4; j++){
				P[i][j] = v3d.pt(S0.Wbox);
				P[i][j].add((-0.9+i*0.6)*S0.rbox, view.J, (-0.9+j*0.6)*S0.rbox, view.I); 
			}
		}
		C0 = new Surface(P, 15); 
		//S0.register(C0);
	}
	public void draw() {  
	  background(255);
	  view.setupView(this);
	  fill(255,255, 0); 
	  this.noStroke(); 
	  S0.showFront(this);
	  this.stroke(50);
	  fill(150, 100); 
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
			//S0.transform(C0); 
		}
		else if (keyPressed && key=='2'){
			C0.move(1, this);
			//S0.transform(C0); 
		}
		else if (keyPressed && key=='3'){
			C0.move(2, this);
			//S0.transform(C0); 
		}
		else if (keyPressed && key=='4'){	
			C0.move(3, this);
			//S0.transform(C0); 
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
			//S0.register(C0);
		}
	}

}
