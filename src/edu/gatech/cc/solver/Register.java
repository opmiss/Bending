package edu.gatech.cc.solver;

import java.util.ArrayList;

import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.model.Mesh;
import edu.gatech.cc.model.Shape3d;

public class Register {
	public ArrayList<Shape3d> objects; 
	public Mesh proxy; 
	public ArrayList<SpinePar[]> parameters; 
	public ArrayList<Double> volume0; 
	public ArrayList<Double> volume; 
	boolean register = false; 
	public Register(Shape3d obj, Mesh pro){
		objects = new ArrayList<Shape3d>(); 
		objects.add(obj); 
		proxy = pro; 
	}
	public Register(Mesh pro){
		objects = new ArrayList<Shape3d>(); 
		proxy = pro; 
	}
	
	public void addObj(Shape3d obj){
		objects.add(obj); 
	}
	
	public void register() {
		parameters = new ArrayList<SpinePar[]>(); 
		volume0 = new ArrayList<Double>(); 
		for (Shape3d object : objects) {
			v3d P, A, B, C, O, L;
			SpinePar[] par = new SpinePar[object.nv];
			for (int i = 0; i < object.nv; i++) {
				P = object.G[i];
				for (int t = 0; t < proxy.nt; t++) {
					int c = 3 * t;
					A = proxy.g(c);
					B = proxy.g(c + 1);
					C = proxy.g(c + 2);
					O = v3d.project(A, B, C, P);
					L = v3d.barycentric(A, B, C, O);
					if (L.x < 0 || L.y < 0 || L.z < 0)
						continue;
					else {
						double a = v3d.area(A, B, C); 
						par[i] = new SpinePar(proxy.V[c],
								proxy.V[c + 1], proxy.V[c + 2], L,
								v3d.vec(O, P), a);
						break;
					}
				}
			}
			parameters.add(par); 
			volume0.add(object.volume()); 
		}
		register = true; 
	}
	
	public void reconstruct(){
		if (!register) return; 
		int o=0; 
		volume = new ArrayList<Double>(); 
		for (Shape3d object:objects){
			for (int i=0; i<object.nv;i++){
				object.G[i].set(parameters.get(o)[i].reconstruct(proxy));
			}
			o++; 
			volume.add(object.volume()); 
		}
	}
	public void _reconstruct(){
		if (!register) return; 
		int o=0; 
		for (Shape3d object:objects){
			for (int i=0; i<object.nv;i++){
				object.G[i].set(parameters.get(o)[i]._reconstruct(proxy));
			}
			o++; 
		}
	}
}
