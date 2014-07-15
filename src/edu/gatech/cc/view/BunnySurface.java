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
	boolean register = false; 
	boolean showobj = true; 
	public void setup() {
		this.size(1280, 720, PApplet.P3D); 
		view.initView();
		S0.declareVectors(); 
		S0.loadMeshVTS(this); 
		S0 = S0.computeBox();
		for (int i=0; i<3; i++) S0 = S0.refine(); 
		P = new v3d[4][4]; 
		for (int i=0; i<4; i++){
			for (int j=0; j<4; j++){
				P[i][j] = v3d.pt(S0.Wbox);
				P[i][j].add((-0.9+i*0.6)*S0.rbox, view.J, (-0.9+j*0.6)*S0.rbox, view.I); 
			  //P[i][j].print(i+", "+j); P[i][j].toScreen(this).print(); 
			}
		}
		C0 = new Surface(P, 50); 
		textAlign(PApplet.LEFT, PApplet.TOP);
	}
	public void draw() {  
	  background(255);
	  view.setupView(this);
	  fill(255,255, 0); 
	  this.noStroke(); 
	 // scribe(); 
	  if (showobj) S0.showFront(this);
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
	  camera(); // 2D view to write help text
	  scribe();
	}
	public void scribe(){
		this.textSize(32);
		this.fill(0);
		this.text("vol error: "+S0.volError(), 10, 10);
	}
	public void mouseDragged(){
		if (keyPressed && key == 'p'){
			C0.translate(this);
			if (register) S0.transform(C0); 
		}
		else{ 
			C0.move(this);
			if (register) S0.transform(C0); 
		}
	}
	public void mousePressed() {
		C0.pick(this);
	}
	public void mouseReleased() {
		C0.drop(); 
	}
	public void keyReleased() {
		
	}
	public void keyPressed() {
		if (key=='s') { 
			S0.smoothen();
		}
		if (key=='r'){
			S0.register(C0);
			C0.saveFrames();
			register = true; 
		}
		if (key=='c'){
			System.out.println("save a frame");
			this.saveFrame("bunny_surface-####.png"); 
		}
		if (key==' '){
			showobj = !showobj; 
		}
	}
}
