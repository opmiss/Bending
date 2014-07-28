package edu.gatech.cc.view;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;
import edu.gatech.cc.model.Shape3d;
import edu.gatech.cc.model.Surface;
import processing.core.*;

public class SurfaceView extends PApplet {
	Shape3d obj = new Shape3d(this); 
	Surface surface; 
	v3d[][] P; 
	int show_color=0; 

	public void setup() {
		this.size(1280, 720, PApplet.P3D); 
		view.initView();
		obj.declareVectors(); 
		obj.loadMeshVTS(true, this); 
		obj = obj.computeBox();
		P = new v3d[4][4]; 
		for (int i=0; i<4; i++){
			for (int j=0; j<4; j++){
				P[i][j] = v3d.pt(obj.Wbox);
				P[i][j].add((-0.9+i*0.6)*obj.rbox, view.J, (-0.9+j*0.6)*obj.rbox, view.I); 
			}
		}
		surface = new Surface(P, 50); 
		textAlign(PApplet.LEFT, PApplet.TOP);
	}
	public void draw() {  
	  background(255);
	  view.setupView(this);
	  switch(show_color){ 
	  case 1: 
		  surface.showColor(surface.area_color, this);
		  break; 
	  case 2: 
		  surface.showColor(surface.gaussian_color, this);
		  break; 
	  case 3: 
		  surface.showColor(surface.mean_color, this);
		  break; 
	  default:
		  surface.show(this);
		  break; 
	  }
	}
	public void scribe(){
		this.textSize(32);
		this.fill(0);
		this.text("vol error: "+obj.volError(), 10, 10);
	}
	public void mouseDragged(){
		if (keyPressed && key == 'p'){
			surface.translate(this);
		}
		else{ 
			surface.move(this);
		}
	}
	public void mousePressed() {
		surface.pick(this);
	}
	public void mouseReleased() {
		surface.drop(); 
	}
	public void keyReleased() {
		
	}
	public void keyPressed() {
		if (key>='0' && key<='3'){
			int p = show_color; 
			show_color = Character.getNumericValue(key); 
			System.out.println(show_color); 
			if (p==0 && show_color>0) surface.computeColor(this);
		}
		if (key=='s') { 

		}
		if (key=='r'){
		
		}
		if (key=='c'){
			this.saveFrame("surfaceview-####.png"); 

		}
		if (key==' '){

		}
	}

}
