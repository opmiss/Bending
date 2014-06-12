package edu.gatech.cc.model;
import edu.gatech.cc.geo.v2d;
import edu.gatech.cc.geo.v3d;
import processing.core.PApplet;

public class Icosahedron extends Shape3d {
	v3d topPoint; 
	v3d[] topPent = new v3d[5]; 
	v3d bottomPoint; 
	v3d[] bottomPent = new v3d[5]; 
	double angle =0, radius = 150; 
	double triDist, triHt, a, b, c; 
	public Icosahedron(PApplet p, double r){
		super(p);
		super.declareVectors(); 
		radius = r;
	}
	public void init(v3d center){
		initG(); 
		initV(); 
		this.moveBy(center); 
	}
	public void initG(){
		v2d p0 = new v2d(radius, 0.0); 
		v2d p1 = new v2d(Math.cos(PApplet.radians(72))*radius, Math.sin(PApplet.radians(72))*radius); 
		c = p0.disTo(p1); 
		b = radius; 
		a = Math.sqrt(c*c-b*b);
		triHt = Math.sqrt(c*c - c*c/4);
		for (int i=0; i<5; i++){
			topPent[i] = new v3d(Math.cos(angle)*radius, Math.sin(angle)*radius, triHt/2.0); 
			angle += PApplet.radians(72); 
			addVertex(topPent[i]); 
		}
		topPoint = new v3d(0, 0, triHt/2.0+a);
		addVertex(topPoint); 
		angle = PApplet.radians(72.0f/2.0f);
		for (int i=0; i<5; i++){
			bottomPent[i] = new v3d(Math.cos(angle)*radius, Math.sin(angle)*radius, -triHt/2.0);
			angle += PApplet.radians(72); 
			addVertex(bottomPent[i]); 
		}
		bottomPoint = new v3d(0, 0, -(triHt/2.0+a)); 
		addVertex(bottomPoint); 
	}
	
	public void initV(){
		int k=0; 
		for (int i=0; i<4; i++){
			V[k++] = i; V[k++] = 5; V[k++] = i+1; 
		}
		V[k++] = 4; V[k++] = 5; V[k++] = 0;  
		for (int i=6; i<10; i++){
			V[k++] = 11; V[k++] = i;  V[k++] = i+1; 
		}
		V[k++] = 11; V[k++] = 10; V[k++] = 6; 
		nc = k; 
		nt = nc/3; 
		
		addTriangle(0, 6, 10); 
		addTriangle(6, 0, 1); 
		addTriangle(1, 7, 6); 
		addTriangle(7, 1, 2); 
		addTriangle(2, 8, 7); 
		addTriangle(8, 2, 3); 
		addTriangle(3, 9, 8); 
		addTriangle(9, 3, 4); 
		addTriangle(4, 10, 9);
		addTriangle(10, 4, 0); 
	}
}
