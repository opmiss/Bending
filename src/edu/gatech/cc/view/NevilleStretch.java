package edu.gatech.cc.view;

import processing.core.PApplet;
import edu.gatech.cc.geo.v2d;
import edu.gatech.cc.model.Neville2d;

public class NevilleStretch extends PApplet{
	v2d[] C; 
	float width = 1000; 
	float height = 800; 
	Neville2d spine; 
	public void setup() {
		size((int) width, (int)height, P2D); 
		C = new v2d[4]; 
		C[0] = new v2d(width/2-300, height/2); 
		C[1] = new v2d(width/2-100, height/2); 
		C[2] = new v2d(width/2+100, height/2); 
		C[3] = new v2d(width/2+300, height/2); 
		spine = new Neville2d(C, 100); 
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
		this.stroke(100); 
		this.noFill(); 
		spine.showGrid(this);
		this.stroke(0, 0, 255);
		this.fill(255, 0);
		spine.show(this);
		this.fill(0, 0, 255);
		spine.showCtrl(this);
	}
	
	public void showOriginal(){
		
	}
	
	public void showBend(){
		
	}
	
	public void keyPressed(){
		if (key=='c') {
			System.out.println("save a frame"); 
			saveFrame("pic-####.png"); 
		}
		
	}

}
