package edu.gatech.cc.view;
import processing.core.PApplet;
import edu.gatech.cc.geo.color;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;
import edu.gatech.cc.model.Curve3d;
import edu.gatech.cc.model.Shape3d;

public class Cylinder extends PApplet {
	Shape3d dummy = new Shape3d(this); 
	Curve3d cylinder; 
	int mode = 0; 
	String bmode = "no correction"; 
	v3d[] control = new v3d[4]; 
	public void setup() {
		this.size(1280, 720, PApplet.P3D); 
		view.initView();
		dummy.declareVectors(); 
		dummy.loadMeshVTS(true, this); 
		dummy = dummy.computeBox();
		control = new v3d[4]; 
		control[0] = v3d.pt(dummy.Wbox); control[0].add(-dummy.rbox*1.2, view.I); 
		control[1] = v3d.pt(dummy.Wbox); control[1].add(-dummy.rbox*0.4, view.I); 
		control[2] = v3d.pt(dummy.Wbox); control[2].add(dummy.rbox*0.4, view.I); 
		control[3] = v3d.pt(dummy.Wbox); control[3].add(dummy.rbox*1.2, view.I); 
		cylinder = new Curve3d(control, 100); 
		cylinder.setCylinder();
		cylinder.saveFrames();
		cylinder.tiles.saveVertices();
		textAlign(PApplet.LEFT, PApplet.TOP);
	}
	public void draw() {  
	  background(255);
	  view.setupView(this);
	  this.noStroke(); 
	  cylinder.show(this);
	  color.green().fill(this);
	  cylinder.showCtrlRings(this);
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
	  camera(); 
	  scribe(); 
	}
	public void mouseDragged(){
		if (keyPressed && key=='1'){ 
			cylinder.move(0, this);
			cylinder.tiles.transform(cylinder, mode); 
		}
		else if (keyPressed && key=='2'){
			cylinder.move(1, this);
			cylinder.tiles.transform(cylinder, mode); 
		}
		else if (keyPressed && key=='3'){
			cylinder.move(2, this);
			cylinder.tiles.transform(cylinder, mode); 
		}
		else if (keyPressed && key=='4'){	
			cylinder.move(3, this);
			cylinder.tiles.transform(cylinder, mode); 
		}
	}
	public void scribe(){
		this.textSize(32);
		this.fill(0);
		this.text("correction mode: "+bmode, 200, 20);
	}
	public void mousePressed() {
		
	}
	public void mouseReleased() {
		
	}
	public void keyReleased() {
		
	}
	public void keyPressed() {
		if (key=='c'){
			System.out.println("save a picture"); 
			this.saveFrame("cylinder-####.png"); 
		}
		if (key=='7'){
			mode = 1; bmode = "normal"; 
			cylinder.tiles.transform(cylinder, mode); 
		}
		else if (key=='8'){
			mode = 2; bmode = "binormal"; 
			cylinder.tiles.transform(cylinder, mode); 
		}
		else if (key=='9'){
			mode = 3; bmode = "radial"; 
			cylinder.tiles.transform(cylinder, mode); 
		}
		else if (key=='0'){
			mode = 0; bmode = "no correction"; 
			cylinder.tiles.transform(cylinder, mode); 
		}
	}
}