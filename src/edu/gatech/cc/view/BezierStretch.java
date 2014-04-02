package edu.gatech.cc.view;
import processing.core.PApplet;
import edu.gatech.cc.geo.*;
import edu.gatech.cc.model.Bezier2d;


public class BezierStretch extends PApplet{
	v2d[] C; 
	float width = 1000; 
	float height = 800; 
	Bezier2d spine; 
	boolean showColor = false; 
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
		noFill(); 
		spine.show(this);
		spine.showCtrl(this);
		if (showColor) spine.showColorGrid(this);
		else spine.showGrid(this);
		color.showMap(this, 50);
	}
	
	public void keyPressed(){
		if (key=='c') {
			System.out.println("save a frame"); 
			saveFrame("pic-####.png"); 
		}
		if (key=='b'){
			spine.bend = !spine.bend; 
			if (spine.bend) System.out.println("bend mapping");
			else System.out.println("no bend mapping"); 
		}
		if (key=='m'){
			showColor = !showColor; 
			if (showColor) {
				System.out.println("show color..."); 
				spine.measure();
			}
			else {
				System.out.println("no color..."); 
			}
		}
	}

}
