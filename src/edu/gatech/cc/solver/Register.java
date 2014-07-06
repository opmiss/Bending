package edu.gatech.cc.solver;

import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.model.Mesh;
import edu.gatech.cc.model.Shape3d;

public class Register {
	public Shape3d object; 
	public Mesh proxy; 
	public SpinePar[] parameters; 
	public Register(Shape3d obj, Mesh pro){
		object = obj; 
		proxy = pro; 
	}
	
	public void register(){
		v3d P, A, B, C, O, L; 
		parameters = new SpinePar[object.nv]; 
		for (int i=0; i<object.nv; i++){
			P = object.G[i]; 
			for (int t=0; t<proxy.nt; t++){
				int c = 3*t; 
				A = proxy.g(c);
				B = proxy.g(c+1);
				C = proxy.g(c+2);
				O = v3d.project(A, B, C, P); 
				L = v3d.barycentric(A, B, C, O);
				if (L.x <0 || L.y < 0 || L.z<0) continue; 
				else{
					parameters[i] = new SpinePar(proxy.V[c], proxy.V[c+1], proxy.V[c+2], L, v3d.vec(O, P));
					break; 
				}
			}
		}
		
	}
	
	public void reconstruct(){
		for (int i=0; i<object.nv;i++){
			object.G[i].set(parameters[i].reconstruct(proxy));
		}
	}
}
