package edu.gatech.cc.solver;

public class root {
	 public static float _bias = 0.000001f; 
	 public static int _max_count = 1000;
	 public static float newtonSolve(float a, float b, float c, float d) {
	   //solve for cubic equation: ax^3+bx^2+cx+d=0
	   float x;  
	   if (c!=0) x = -d/c;
	   else if (d<0 && b>0) x = (float) Math.sqrt(-d/b); 
	   else {
	     x = 0; 
	    // System.out.println("invalid coefficients Newton Solve!");
	   }
	   float dx = 1;
	   float f = ((a*x+b)*x+c)*x + d;
	   int count = 0;
	   while (dx > _bias || Math.abs (f) > _bias) {
	     float df = (a*x*3+b*2)*x+c;
	     if (Math.abs(df)<_bias) return x; 
	     float xnew = x - (f/df);
	     dx = Math.abs(x - xnew);
	     x = xnew;
	     f = ((a*x+b)*x+c)*x + d;
	     count++;
	     if (count > _max_count) {
	      System.out.print("no real solution, error:"+dx); 
	       break;
	     }
	   }
	   if (Float.isNaN(x))System.out.println("NewtonSolve("+a+","+b+","+c+","+d+") returned NaN");
	   return x;
	   } 
	   
	 public static float cubicSolve(float a) {
	   //solve for cubic equation: ax^3+x^2-1=0
	   //in case there is no real root, we cap the solution
	   if (a==0) return 1; 
	   if (a > 0) return newtonSolve(a, 1, 0, -1);
	   float max_x = (float) Math.sqrt(3); 
	   if (a < -0.385f) {
	   // System.out.println("invalid coefficient Cubic solve!"); 
	     return max_x;
	   }
	   float x = 1.0f; 
	   float dx = 0.5f;
	   float f = (a*x+1)*x*x-1 ;
	   int count = 0;
	   while (dx > _bias || Math.abs (f) > _bias) {
	     float df = (a*x*3+2)*x;
	     while (Math.abs (df)<_bias) {
	       x = (float) 0.5*max_x; 
	       df = (a*x*3+2)*x;
	     }
	     float xnew = x - (f/df);
	     if (xnew >max_x) xnew = max_x-_bias; 
	     if (xnew <1) xnew = 1+_bias; 
	     dx = Math.abs(x - xnew);
	     x = xnew;
	     f = (a*x+1)*x*x-1;
	     count++;
	     if (count > _max_count) {
	      System.out.println("no real solution, error:"+dx); 
	       break;
	     }
	   }
	   if (Float.isNaN(x)) System.out.println("CubicSolve("+a+") returned NaN");
	   return x;
	   } 
	   
	 public static float squareSolve(float a, float b, float c) {
	   //solve for quadratic equation: ax^2+bx+c=0
	   if (Math.abs(a) < _bias) return -c/b; 
	   float D = b*b - 4*a*c;
	   if(D<0) System.out.println("SquareSolve("+a+","+b+","+c+") has D<0");
	   return (-b+ (float)Math.sqrt(D))/2/a;
	   }
}
