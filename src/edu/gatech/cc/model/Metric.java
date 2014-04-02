package edu.gatech.cc.model;

import edu.gatech.cc.geo.color;

public class Metric {
	double[] error;
	double[] tones; 
	public Metric(double[] area, double area0, int n){
		error = new double[n]; 
		for (int i=0; i<n; i++){
			error[i] = (area[i]-area0)/area[i]; 
		}
		color[] tones = new color[n]; 
		for (int i=0; i<n; i++){
			tones[i] = color.find((float)error[i]); 
		}
	}
	double mae(){
		double s = 0; 
		for (double e:error){
			s+=Math.abs(e); 
		}
		return s/error.length; 
	}
}
