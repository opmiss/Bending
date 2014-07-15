package edu.gatech.cc.model;

import processing.core.PApplet;
import edu.gatech.cc.geo.v3d;

public class Mesh extends Shape3d{
	
	public Mesh(PApplet p, Surface S) {
		super(p);
		super.declareVectors(); 
		init(S); 
	}
	int[][] id = null; 
	public Surface surface = null; 
	public void init(Surface S){
		surface = S; 
		id = new int[S.num][S.num]; 
		for (int i=0; i<S.num; i++){
			for (int j=0; j<S.num; j++){
				id[i][j] = addVertex(S.P[i][j]); 
			}
		}
		for (int i=0; i<S.num-1; i++){
			for (int j=0; j<S.num-1; j++){
				addTriangle(id[i][j], id[i+1][j], id[i][j+1]);
				addTriangle(id[i][j+1], id[i+1][j], id[i+1][j+1]); 
			}
		}
	}
	
	public void set(Surface S){
		this.nv=0; 
		for (int i=0; i<S.num; i++){
			for (int j=0; j<S.num; j++){
				id[i][j] = addVertex(S.P[i][j]); 
			}
		}
	}

	
/*	Mesh(Surface S){
 * 
		for (int i=0; i<S.num; i++){
			for (int j=0; j<S.num; j++){
				
			}
		}
	}
	public void showMesh(){
		
	}*/
}
