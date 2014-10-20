package edu.gatech.cc.view;
import processing.core.PApplet;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;
import edu.gatech.cc.model.Curve3d;
import edu.gatech.cc.model.Shape3d;

public class ShapeBend extends PApplet{
	Shape3d shape = new Shape3d(this); 
	Curve3d spine; 
	int mode =3; 
	String bmode = "radial"; 
	v3d[] P = new v3d[4]; 
	public void setup() {
		this.size(1280, 720, PApplet.P3D); 
		view.initView();
		shape.declareVectors(); 
		shape.loadMeshVTS(true, this); 
		shape = shape.computeBox();
		P = new v3d[4]; 
		P[0] = v3d.pt(shape.Wbox); P[0].add(-shape.rbox*1.2, view.I); 
		P[1] = v3d.pt(shape.Wbox); P[1].add(-shape.rbox*0.4, view.I); 
		P[2] = v3d.pt(shape.Wbox); P[2].add(shape.rbox*0.4, view.I); 
		P[3] = v3d.pt(shape.Wbox); P[3].add(shape.rbox*1.2, view.I); 
		spine = new Curve3d(P, 100); 
		shape.register(spine);
		spine.saveFrames();
		textAlign(PApplet.LEFT, PApplet.TOP);
	}
	public void draw() {  
	  background(255);
	  view.setupView(this);
	  fill(255,255, 0); 
	  this.noStroke(); 
	  shape.showFront(this);
	  spine.show(this);
	  //spine.showPts(this);
	  //this.sphere(30);
	  if (keyPressed && key=='r'&& mousePressed) {
		  view.rotate(this);
	  } //rotate E around F
	  else if (keyPressed && key=='z' && mousePressed){
		  view.zoom(this);
	  }
	  else if (keyPressed && key=='t' && mousePressed){
		  view.translate(this);
	  }
	  camera(); 
	  scribe(); 
	}
	public void scribe(){
		this.textSize(32);
		this.fill(0);
		this.text("correction mode: "+bmode, 500, 10);
	}
	public void mouseDragged(){
		if (keyPressed && key=='1'){ 
			spine.bend(0, this);
			long t = System.currentTimeMillis(); 
			shape.transform(spine, mode); 
			System.out.println("Deformation takes "+(System.currentTimeMillis()-t)+" milliseconds." ); 	
		}
		else if (keyPressed && key=='2'){
			spine.bend(1, this);
			shape.transform(spine, mode); 
		}
		else if (keyPressed && key=='3'){
			spine.bend(2, this);
			shape.transform(spine, mode); 
		}
		else if (keyPressed && key=='4'){	
			spine.bend(3, this);
			shape.transform(spine, mode); 
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
			for (int i=0; i<3; i++) {
				shape = shape.refine(); 
			}
			System.out.println(shape.nv); 
			long t = System.currentTimeMillis(); 
			shape.register(spine);
			System.out.println("Registration takes "+(System.currentTimeMillis()-t)+" milliseconds." ); 
		}
		if (key=='c'){
			System.out.println("save a picture"); 
			this.saveFrame("bunny_curve-####.png"); 
		}
		if (key=='7'){
			mode = 1; bmode = "normal"; 
			shape.transform(spine, mode); 
		}
		if (key=='8'){
			mode = 2; bmode = "binormal"; 
			shape.transform(spine, mode); 
		}
		if (key=='9'){
			mode = 3; bmode = "radial"; 
			shape.transform(spine, mode); 
		}
	}

}
