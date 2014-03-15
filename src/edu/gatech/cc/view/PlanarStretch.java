package edu.gatech.cc.view;
import processing.core.PApplet;
import edu.gatech.cc.geo.v2d;
import edu.gatech.cc.model.Bezier2d;

public class PlanarStretch extends PApplet{
	v2d[] C; 
	float width = 1000; 
	float height = 800; 
	Bezier2d spine; 
	public void setup() {
		size((int) width, (int)height, P2D); 
		C = new v2d[4]; 
		C[0] = new v2d(width/2-300, height/2); 
		C[1] = new v2d(width/2-100, height/2); 
		C[2] = new v2d(width/2+100, height/2); 
		C[3] = new v2d(width/2+300, height/2); 
		spine = new Bezier2d(C, 100); 
		spine.computeGrid();
		//smooth(); 
	}
	public void mousePressed() {
		spine.pick(new v2d(this.mouseX, this.mouseY));
	}
	public void mouseDragged() {
		if (this.mousePressed) spine.move(new v2d(this.mouseX, this.mouseY));
	}
	public void mouseReleased(){
		spine.drop();
	}
	public void draw() {
		smooth(); 
		background(255); 
		spine.show(this);
		spine.showCtrl(this);
		spine.showGrid(this);
	}
	
	public void showOriginal(){
		
	}
	
	public void showBend(){
		
	}
	
	public void keyPressed(){
		
	}

}
