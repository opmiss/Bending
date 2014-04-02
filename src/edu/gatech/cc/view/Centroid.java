package edu.gatech.cc.view;
import edu.gatech.cc.geo.v2d;
import edu.gatech.cc.model.Arc2d;
import edu.gatech.cc.model.Shape2d;
import processing.core.*; 

public class Centroid extends PApplet {
	float width = 1000; 
	float height = 800; 
	//Shape2d S0 = Shape2d.CatHead(new v2d(width/2,  height/2), 150, 75); 
	Shape2d S0 = Shape2d.Ellipse(new v2d(width/2,  height/2), 200, 100); 
	double areaS0 = S0.area(); 
	Shape2d S1 = new Shape2d(S0); 
	
	Arc2d C0 = Arc2d.Line(S0.C, PI*2/5*S0.C.y); 
	Arc2d C1 = new Arc2d(C0);  
	
	int numLayers = 9; 
	Shape2d L0 = Shape2d.Layers(C0.C, C0.L, 280, 100, numLayers); 
	Shape2d L1 = new Shape2d(L0); 
	
	public void setup() {
		size((int)(width*1.28/2), (int)(height*1.2/2), P2D);
		//translate(width/4, height/4); 
		frameRate(20);
		textSize(24); 
		//register shape to the spine
	}
	public void mousePressed() {
		
	}
	boolean showO=true;
	boolean showB=true; 
	public void draw() {
		background(255);
		//text("error: "+ nf((float)(areaS0 - S1.area()), 1, 3) , 20, 50);
		translate(-width/5.5f, -height/5); 
		smooth(); 
		if (showO) showOriginal(); 
		if (showB) showBend(); 
	}
	
	public void showOriginal(){
		//show layers
		noFill(); 
		this.stroke(200);
	//	L0.showLayers(numLayers, 100, this);
		//Show shape 
		noFill(); 
		stroke(0); 
		S0.show(this);
		//Show centroid
		noStroke(); 
		fill(200, 200, 0); 
		S0.C.show(10, this);
		//Show spine
		stroke(0, 0, 250);
		this.strokeWeight(4);
		C0.show(this);
	}
	
	public void showBend(){
		//show layers
		noFill(); 
		this.stroke(200);
	//	L1.showLayers(numLayers, 100, this);
		//Show shape 
		noFill(); 
		stroke(0); 
		S1.show(this);
		//Show centroid
		noStroke(); 
		fill(200, 200, 0); 
		S1.C.show(10, this);
		//Show spine
		noFill(); 
		stroke(0, 0, 250);
		this.strokeWeight(4);
		C1.show(this);
	}
	
	public void keyPressed(){
		if (key == '1'){
			C1.bend(-1.5);
			S1 = S0.Map(C0, C1);  
			L1 = L0.Map(C0, C1); 
		}
		if (key == '2'){
			C1.bend(1.5);
			S1 = S0.Map(C0, C1); 
			L1 = L0.Map(C0, C1); 
		}
		if (key == 'o'){
			showO = !showO; 
		}
		if (key == 'b'){
			showB = !showB; 
		}
		if (key == 'c'){
			System.out.println("save frame");
			this.saveFrame("cathead-####.png");
		}
	}
}
