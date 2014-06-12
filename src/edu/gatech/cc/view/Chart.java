package edu.gatech.cc.view;

import edu.gatech.cc.solver.Root;
import processing.core.PApplet;

public class Chart extends PApplet{
	public void setup(){
		size(800, 800); 
		noLoop(); 
	}
	public void draw(){
		background(255); 
		int cx = width/8; 
		int cy = height/8; 
		double dk = -0.002; 
		double dp = PApplet.PI/6; 
		for (int i=0; i<4; i++ ){
			for (int j=0; j<4; j++){
				cx = width/8 + width/4*i; 
				cy = height/8 + height/4*j; 
				show2D(-0.002-dk*i, PApplet.PI/4, 60, 5, cx, cy); 
			}
		}
	}
	public void show2D(double cur, double phi, double rad, int n, int cx, int cy){
		double dr = rad/n; 
		double theta = 0; 
		double x0, y0, x1, y1, r, a, b, c, d, cosp = Math.cos(phi), sinp = Math.sin(phi);
		double dtheta = PApplet.TWO_PI/30; 
		noFill(); 
		for (r= dr; r<=rad; r+=dr){
			this.beginShape(); 
			for (theta =0; theta < PApplet.TWO_PI; theta+=dtheta){
				x0 = r*Math.cos(theta); 
				y0 = r*Math.sin(theta);
				//compute 
				a = -0.5*cur*cosp*cosp*sinp; 
				b = -0.5*cur*y0*cosp*cosp + (1-cur*x0)*sinp*cosp;
				c = (1-cur*x0)*y0*cosp + (1-0.5*cur*x0)*x0*sinp; 
				d = -0.5*cur*x0*x0*y0; 
				double l = Root.newtonSolve(a, b, c, d); 
				
				/* -\frac{1}{2} \kappa_1 \cos^2\phi\sin\phi l^3 
				 * + (-\frac{1}{2}\kappa_1 y_0 \cos^2\phi + (1-\kappa_1x_0)\sin\phi\cos\phi) l^2 
		+((1-\kappa_1x_0)y_0\cos\phi + (1-\frac{1}{2}\kappa_1x_0)x_0\sin\phi)l 
		- \frac{1}{2}\kappa_1x_0^2y_0 = 0 
				 */
				x1 = x0 + l*Math.cos(phi);
				y1 = y0 + l*Math.sin(phi); 
				vertex((float)(cx+y1), (float)(cy+x1));
			}
			this.endShape(CLOSE); 
		}
	}
}
