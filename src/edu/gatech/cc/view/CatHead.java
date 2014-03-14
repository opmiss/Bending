package edu.gatech.cc.view;
import edu.gatech.cc.geo.v2d;
import edu.gatech.cc.model.Arc2d;
import edu.gatech.cc.model.Shape2d;
import processing.core.*; 

public class CatHead extends PApplet {
	float width = 1000; 
	float height = 800; 
	Shape2d S0 = Shape2d.CatHead(new v2d(width/2,  height/2), 150, 100); 
	double areaS0 = S0.area(); 
	Shape2d S1 = new Shape2d(S0); 
	/*v2d O0 = new v2d(width/2, 0);
	double R0 = O0.disTo(S0.C); 
	double L0 = PI*2/5*R0;  
	double A0 = L0/R0; 
	double SA0 = PI/2-L0/R0/2; 
	double EA0 = PI/2+L0/R0/2; */
	Arc2d C0 = Arc2d.Line(S0.C, PI*2/5*S0.C.y); 
	Arc2d C1 = new Arc2d(C0); 
	public void setup() {
		size((int)width, (int)height, P2D);
		frameRate(20);
		textSize(24); 
		//register shape to the spine
	}
	public void mousePressed() {
		
	}
	public void draw() {
		background(255);
		text("error: "+ nf((float)(1 - S1.area()/areaS0), 1, 3) , 20, 50);
		smooth(); 
		showOriginal(); 
		showBend(); 
	}
	
	public void showOriginal(){
		//Show shape 
		noFill(); 
		stroke(0); 
		S0.show(this);
		//Show centroid
		noStroke(); 
		fill(200, 200, 0); 
		S0.C.show(20, this);
		//Show spine
		stroke(0, 0, 250);
		this.strokeWeight(4);
		C0.show(this);
	}
	
	public void showBend(){
		//Show shape 
		noFill(); 
		stroke(0); 
		S1.show(this);
		//Show centroid
		noStroke(); 
		fill(200, 200, 0); 
		S1.C.show(20, this);
		//Show spine
		noFill(); 
		stroke(0, 0, 250);
		this.strokeWeight(4);
		C1.show(this);
	}
	
	public void keyPressed(){
		if (key == 'a'){
			C1.bend(-1.2);
			S1 = S0.Map(C0, C1); 
		}
		/*if (key == 'b'){
			C1.bend(-0.1);
			S1 = S0.Map(C0, C1); 
		}
		if (key == 'c'){
			C1.stretch(5);
			S1 = S0.Map(C0, C1); 
		}
		if (key == 'd'){
			C1.stretch(-5);
			S1 = S0.Map(C0, C1); 
		}*/
	}
}
