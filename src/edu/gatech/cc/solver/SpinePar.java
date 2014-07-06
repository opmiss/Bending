package edu.gatech.cc.solver;

import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.model.Mesh;

public class SpinePar {
	int i1, i2, i3; 
	double l1, l2, l3; 
	v3d NO; 
	
	SpinePar(int i, int j, int k, v3d B, v3d N){
		i1 = i; i2 = j; i3 = k; 
		l1 = B.x; l2 = B.y; l3 = B.z; 
		NO=N;
	}
	
	v3d reconstruct(Mesh proxy){
		v3d A = proxy.G[i1]; 
		v3d B = proxy.G[i2]; 
		v3d C = proxy.G[i3];
		v3d O = v3d.vec(l1, A, l2, B, l3, C); 
		return O.add(NO);  
	}
	
	
}
