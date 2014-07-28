package edu.gatech.cc.view;

import processing.core.PApplet;
import edu.gatech.cc.geo.color;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;
import edu.gatech.cc.model.Icosahedron;
import edu.gatech.cc.model.Mesh;
import edu.gatech.cc.model.Shape2d;
import edu.gatech.cc.model.Shape3d;
import edu.gatech.cc.model.Surface;
import edu.gatech.cc.solver.Register;

public class Spheres extends PApplet{
	Shape3d S0 = new Shape3d(this); 
	Icosahedron[] IS = new Icosahedron[9]; 
	Mesh M0; 
	Register R;  
	Surface C0; 
	v3d[][] P; 
	public void setup() {
		this.size(1280, 720, PApplet.P3D); 
		view.initView();
		S0.declareVectors(); 
		S0.loadMeshVTS(true, this); 
		S0 = S0.computeBox();
		P = new v3d[4][4]; 
		for (int i=0; i<4; i++){
			for (int j=0; j<4; j++){
				P[i][j] = v3d.pt(S0.Wbox);
				P[i][j].add((-0.9+i*0.6)*S0.rbox, view.J, (-0.9+j*0.6)*S0.rbox, view.I); 
			}
		}
		C0 = new Surface(P, 95); 
		M0 = new Mesh(this, C0);
		R = new Register(M0); 
		for (int i=0; i<3; i++){
			for (int j=0; j<3; j++){
				IS[i*3+j] = new Icosahedron(this, 40);
				v3d c = v3d.pt(S0.Wbox); 
				c=c.add(20, view.K); 
				IS[i*3+j].init(c.add((-0.6+i*0.6)*S0.rbox, view.J, (-0.6+j*0.6)*S0.rbox, view.I));
				IS[i*3+j].refine(); IS[i*3+j].refine(); IS[i*3+j].refine();
				R.addObj(IS[i*3+j]);
			}
		}  
		R.register();
		textAlign(PApplet.LEFT, PApplet.TOP);
	}
	public void draw() {  
	  background(255);
	  view.setupView(this);
	  this.noStroke(); 
	  fill(155, 200); 
	  M0.showFront(this);

	  if (showc) {
		  if (show0) showColorSpheres0(); 
		  else showColorSpheres(); 
	  }
	  else showSpheres(); 
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
	}
	public void transform(){
		M0.set(C0);
		R.reconstruct();
	}
	
	public void showSpheres(){
		  fill(255, 255, 0); 
		  for (Icosahedron is:IS) is.showTs(this);
	}
	public void showColorSpheres(){
		for (int i=0; i<9; i++){
			color.fill(color.findColor((float)((R.volume.get(i) - R.volume0.get(i))/R.volume0.get(i)*5)), this); 
			IS[i].showTs(this);
		}
	}
	public void showColorSpheres0(){
		for (int i=0; i<9; i++){
			color.fill(color.findColor((float)((R.volume.get(i) - R.volume0.get(i))/R.volume0.get(i))), this); 
			IS[i].showTs(this);
		}
	}
	
	public void mouseDragged(){
		if (keyPressed && key == 'p'){
			C0.translate(this);
			transform(); 
		}
		else if (!keyPressed) { 
			C0.move(this); 
			transform(); 
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
	boolean show0 = true; 
	boolean showc=false; 
	public void keyPressed() {
		if (key=='f') { 
			show0 = !show0; 
		}
		if (key=='c'){
			System.out.println("save a frame");
			this.saveFrame("sphere-####.png"); 
		}
		if (key=='v'){
		}
		if (key=='b'){
			showc=!showc;
		}
	}
}

