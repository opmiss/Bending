package edu.gatech.cc.model;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.solver.Transform;
import processing.core.PApplet;


public class Tiles {
	private Curve3d spine; 
	public v3d[][] grid; 
	int slice_num = 18; 
	float offset_dis = 40.0f;   
	int np; 
	/*------------------------construction--------------------------*/
	public Tiles(Curve3d sp, int num, float r, ShowMode show){
		spine = sp; 
		np = sp.num; 
		slice_num = num; 
		offset_dis = r; 
		smode = show; 
		createTiles(); 
	}
	public void setSpine(Curve3d sp){
		spine = sp; 
		createTiles(); 
	}
	public void setSkin(v3d[][] surf){
		grid = surf; 
	}
	
	public void setSliceNum(int num){
		if (slice_num!=num) { 
			slice_num = num; 
			createTiles(); 
		}
	}
	public void setShowMode(ShowMode show){
		smode = show; 
	}
	public void createTiles(){ 
		grid = new v3d[np][slice_num]; 
		for (int i=0; i<np; i++){
			grid[i] = getSpine().getPFrame(i).slice(offset_dis, slice_num); 
		}
	}
	//----------------------transform----------------------------
	public v3d[][] grid0=null;
	public void saveVertices(){
		grid0 = new v3d[np][slice_num]; 
		for (int i=0; i<np; i++){
			for (int j=0; j<slice_num; j++){
				grid0[i][j] = v3d.pt(grid[i][j]); 
			}
		}
	}
	public Tiles transform(Curve3d C){
		for (int i=0; i<np; i++){
			for (int j=0; j<slice_num; j++){
				grid[i][j] = Transform.map(grid0[i][j], C.frame0[i], C.frame[i], 3); 
			}
		}
		return this; 	
	}
	//---------------------display--------------------------------
	ShowMode smode=ShowMode.CHECKER; 
	public void show(PApplet pa){
		smode.show(this, pa); 
	}
	public void showCubes(int is, int js, float rs, int ie, int je, int re){
		
	}
	public Curve3d getSpine() {
		return spine;
	}
}
