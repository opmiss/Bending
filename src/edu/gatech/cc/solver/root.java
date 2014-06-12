package edu.gatech.cc.solver;
import processing.core.PApplet;

public class Root {
	 public static double _bias = 0.000001f; 
	 public static int _max_count = 1000;
	 public static double newtonSolve(double a, double b, double c, double d) {
	   //solve for cubic equation: ax^3+bx^2+cx+d=0
	   double x;  
	   if (c!=0) x = -d/c;
	   else if (d<0 && b>0) x = (double) Math.sqrt(-d/b); 
	   else {
	     x = 0; 
	    // System.out.println("invalid coefficients Newton Solve!");
	   }
	   double dx = 1;
	   double f = ((a*x+b)*x+c)*x + d;
	   int count = 0;
	   while (dx > _bias || Math.abs (f) > _bias) {
	     double df = (a*x*3+b*2)*x+c;
	     if (Math.abs(df)<_bias) return x; 
	     double xnew = x - (f/df);
	     dx = Math.abs(x - xnew);
	     x = xnew;
	     f = ((a*x+b)*x+c)*x + d;
	     count++;
	     if (count > _max_count) {
	      System.out.print("no real solution, error:"+dx); 
	       break;
	     }
	   }
	   if (Double.isNaN(x))System.out.println("NewtonSolve("+a+","+b+","+c+","+d+") returned NaN");
	   return x;
	   } 
	 
	 public static double solve(double a){
			//solve for cubic equation: ax^3+x^2-1=0
			//in case there is no real root, we cap the solution
			 if (a==0) return 1; 
			 if (a > 0) return newtonSolve(a, 1, 0, -1);
			 double max = (double) Math.sqrt(3); 
			 if (a < -0.385f) {System.out.println("invalid coefficient!"); return max; }
			 double x = 1.0f; 
			 double dx = 0.5f;
			 double f = (a*x+1)*x*x-1 ;
			 int count = 0;
			 while (dx > _bias || Math.abs(f) > _bias){
			    double df = (a*x*3+2)*x;
			    while (Math.abs(df)<_bias) {
			    	x = Math.random()*max; 
			    	df = (a*x*3+2)*x;
			    }
			    double xnew = x - (f/df);
			    if (xnew >max) xnew = max-_bias; 
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
			  return x; 
			} 
	   
	 public static double cubicSolve(double a) {
	   //solve for cubic equation: ax^3+x^2-1=0
	   //in case there is no real root, we cap the solution
	   if (a==0) return 1; 
	   if (a > 0) return newtonSolve(a, 1, 0, -1);
	   double max_x = (double) Math.sqrt(3); 
	   if (a < -0.385f) {
	   // System.out.println("invalid coefficient Cubic solve!"); 
	     return max_x;
	   }
	   double x = 1.0f; 
	   double dx = 0.5f;
	   double f = (a*x+1)*x*x-1 ;
	   int count = 0;
	   while (dx > _bias || Math.abs (f) > _bias) {
	     double df = (a*x*3+2)*x;
	     while (Math.abs (df)<_bias) {
	       x = (double) 0.5*max_x; 
	       df = (a*x*3+2)*x;
	     }
	     double xnew = x - (f/df);
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
	   if (Double.isNaN(x)) System.out.println("CubicSolve("+a+") returned NaN");
	   return x;
	   } 
	   
	 public static double squareSolve(double a, double b, double c) {
	   //solve for quadratic equation: ax^2+bx+c=0
	   if (Math.abs(a) < _bias) return -c/b; 
	   double D = b*b - 4*a*c;
	   if(D<0) System.out.println("SquareSolve("+a+","+b+","+c+") has D<0");
	   return (-b+ (double)Math.sqrt(D))/2/a;
	 }
	 
	public static double forwardRadial(double krcosa) {
		if (Math.abs(krcosa) < _bias)
			return 1.0f;
		double a = krcosa * 2 / 3;
		double x = solve(a);
		return x;
	}

	public static double backwardRadial(double krcosa) {
		double a = krcosa * 2 / 3;
		return (double) Math.sqrt(a + 1);
	}

	public static double forwardStratifyN(double kx) {
		double a = kx / 2;
		double x = squareSolve(a, 1, -1);
		return x;
	}

	public static double backwardStratifyN(double kx) {
		return (1.0f + kx / 2);
	}

	public static double forwardStratifyB(double kx) {
		return 1.0f / (kx + 1);
	}

	public static double backwardStratifyB(double kx) {
		return kx + 1;
	}	 
	 
}
