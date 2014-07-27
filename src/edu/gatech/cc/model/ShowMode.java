package edu.gatech.cc.model;
import processing.core.PApplet;
import edu.gatech.cc.geo.*;

public enum ShowMode {
	STRIP, CHECKER;
	public static color[] Color = {color.ruby(), color.yellow(), color.blue3(), color.green()}; 
	public static color blue = color.blue2(); 
	public static color red = color.red4(); 
	public void show(Tiles tile, PApplet pa){
		switch (this){
		case CHECKER:
			showChecker(tile.grid, tile.np, tile.slice_num, pa); 
			break; 
		case STRIP: 
			showStripe(tile.grid, tile.np, tile.slice_num, pa);
			break; 
		}
	}
	
	private void showChecker(v3d[][] surf, int np, int ns, PApplet pa){
		//pa.noStroke(); 
		/*for (int i=0; i<np-1; i++){
			int ni = i+1;
			for (int j=0; j<ns; j++){
				int nj = j+1; 
				if (j==ns-1) nj =0; 
				if (i%2==j%2) color.fill(Color[0], pa);
				else color.fill(Color[1], pa); 
				surf[ni][nj].show(10, pa);
				//showQuad(surf, i, ni, nj, j, pa); 
				//System.out.println(i+","+ni+","+nj+","+j); 
			}
		}*/
		pa.beginShape(PApplet.QUADS); 
		for (int i=0; i<np-1; i++){
			int ni = i+1;
			for (int j=0; j<ns; j++){
				int nj = j+1; 
				if (j==ns-1) nj =0; 
				if (i%2==j%2) color.fill(Color[0], pa);
				else color.fill(Color[1], pa); 
				surf[i][j].vert(pa); 
				surf[ni][j].vert(pa);  
				surf[ni][nj].vert(pa); 
				surf[i][nj].vert(pa);
			}
		}
		pa.endShape(); 
	}
	
	private void showStripe(v3d[][] surf, int np, int ns, PApplet pa){
		pa.noStroke(); 
		pa.beginShape(PApplet.QUADS);
		for (int i=0; i<np-1; i++){
			int ni = i+1; 
			for (int j=0; j<ns; j++){
				int nj = j+1; 
				if (j==ns-1) nj =0; 
				color.fill(Color[j%Color.length], pa);
				drawQuad(surf, i, ni, nj, j, pa); 
			}
		}
		pa.endShape();
	}
	
	public static void showRings(v3d[][] surf, int np, int ns, int[] id, PApplet pa){
		pa.beginShape(PApplet.QUADS);
		for (int k=0; k<id.length; k++){
			int i= id[k]; 
			if (i==np-1) --i; 
			int ni = i+1; 
			for (int j=0; j<ns; j++){
				int nj = j+1; 
				if (j==ns-1) nj=0; 
				drawQuad(surf, i, ni, nj, j, pa); 
			}
		}
		pa.endShape(); 
	}
	
	//show color stripe
	public static void showColorStripe(v3d[][] surf, int np, int ns, PApplet pa){
		int sn = ns/4; 
		pa.noStroke(); 
		pa.beginShape(PApplet.QUADS);
		for (int i=0; i<np-1; i++){
			int ni = i+1; 
			for (int j=0; j<ns; j++){
				int nj = j+1; 
				if (j==ns-1) nj =0; 
				color.fill(Color[j/sn], pa);
				drawQuad(surf, i, ni, nj, j, pa); 
			}
		}
		pa.endShape();
	}
	public static void drawQuad(v3d[][] surf, int i, int ni, int nj, int j, PApplet pa){
		surf[i][j].vert(pa); 
		surf[ni][j].vert(pa);  
		surf[ni][nj].vert(pa); 
		surf[i][nj].vert(pa); 
	}
}
