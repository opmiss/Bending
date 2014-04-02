package edu.gatech.cc.geo;
import processing.core.PApplet;

public class color {
	int r; 
	int g; 
	int b; 
	int a =-1; 
	public color(int r, int g, int b){
		this.r=r; this.g=g; this.b=b; 
	} 
	public color(int r, int g, int b, int a){
		this.r=r; this.g=g; this.b=b; this.a=a;  
	}
	public static color ruby(){
		return new color(209, 101, 135); 
	}
	public static color tGreen(){ 
		return new color(156, 217, 107, 200); 
	}
	public static color lGreen(){
		return new color(156, 217, 107); 
	}
	public static color green(){
		return new color(0, 208, 0); 
	}
	public static color tYellow(){
		return new color(240, 239, 136, 100);
	}
	public static color yellow(){
		return new color(255, 207, 0); 
	}
	
	public static color blue5(){
		return new color(0, 102, 153); 
	}
	public static color blue4(){
		return new color(51, 204, 204); 
	}
	public static color blue3(){
		return new color(0, 255, 255); 
	}
	public static color blue2(){
		return new color(102, 255, 255); 
	}
	public static color blue1(){
		return new color(204, 255, 255); 
	}
	public static color white(){
		return new color(255, 255, 255); 
	}
	public static color red1(){
		return new color(255, 204, 204);
	}
	public static color red2(){
		return new color(255, 153, 153);
	}
	public static color red3(){
		return new color(255, 102, 102);
	}
	public static color red4(){
		return new color(255, 80, 80);
	}
	public static color red5(){
		return new color(204, 0, 0);
	}
	public String toString(){
		return Integer.toString(r)+Integer.toString(g)+Integer.toString(b); 
	}
	
	public static color lerp(color c1, float s, color c2){
		return new color( c1.r+ (int)(s*(c2.r-c1.r)), 
				c1.g+ (int)(s*(c2.g-c1.g)),
				c1.b+ (int)(s*(c2.b-c1.b))
				); 
	}
	public static color find(float key){
		//map key value in (-1, 1) to color (blue5, red5)
		if (key<-0.83) return color.blue5(); 
		else if (key < -0.67) return color.blue4(); 
		else if (key < -0.5) return color.blue3(); 
		else if (key < -0.33) return color.blue2(); 
		else if (key < -0.17) return color.blue1(); 
		else if (key < 0.17) return color.white(); 
		else if (key < 0.33) return color.red1(); 
		else if (key < 0.5) return color.red2(); 
		else if (key < 0.67) return color.red3(); 
		else if (key < 0.83) return color.red4();
		else return color.red5(); 
	}
	public static color findColor(float key){
		if (key<-1) return color.blue5(); 
		else if (key < -0.8f) return color.lerp(blue5(), (key+1)*5, blue4()); 
		else if (key < -0.6f) return color.lerp(blue4(), (key+0.8f)*5, blue3()); 
		else if (key < -0.4f) return color.lerp(blue3(), (key+0.6f)*5, blue2()); 
		else if (key < -0.2f) return color.lerp(blue2(), (key+0.4f)*5, blue1()); 
		else if (key < 0) return color.lerp(blue1(), (key+0.2f)*5, white()); 
		else if (key < 0.2f) return color.lerp(white(), key*5, red1()); 
		else if (key < 0.4f) return color.lerp(red1(), (key-0.2f)*5, red2()); 
		else if (key < 0.6f) return color.lerp(red2(), (key-0.4f)*5, red3()); 
		else if (key < 0.8f) return color.lerp(red3(), (key-0.6f)*5, red4());
		else if (key < 1) return color.lerp(red4(), (key-0.8f)*5, red5()); 
		else return color.red5(); 
	}
	public static void showMap(PApplet pa, float height){
		pa.noStroke(); 
		pa.beginShape(PApplet.QUAD_STRIP); 
		int length = 200; 
		for (int i=-length; i<length; i+=5){
		//	System.out.println(findColor((float)i/length).toString()); 
			fill(findColor((float)i/length), pa); 
			pa.vertex(i+length, 0); 
			pa.vertex(i+length, height); 
		}
		pa.endShape(); 
	}
	public static void fill(color c, PApplet pa){
		if (c.a<0) pa.fill(c.r, c.g, c.b);
		else pa.fill(c.r, c.g, c.b, c.a); 
	}
	public static void fill(color c, int op, PApplet pa){
		pa.fill(c.r, c.g, c.b, op);
	}
	
	public static void stroke(color c, PApplet pa){
		pa.stroke(c.r, c.g, c.b); 
	}
}
