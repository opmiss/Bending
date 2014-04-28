package edu.gatech.cc.model;
import edu.gatech.cc.geo.v3d;
import edu.gatech.cc.geo.view;
import processing.core.PApplet;

public class Shape3d {
	// max sizes, counts, selected corners 
	private int maxnv = 10000; // max number of vertices
	private int maxnt = maxnv * 2; // max number of triangles
	private int nv = 0; // current number of vertices
	private int nt = 0; // current number of triangles
	public int nc = 0; // current number of corners (3 per triangle)
	private double vol = 0, surf = 0, edgeLength = 0; // vol and surface and average edge length
	
	// boundingbox
	public v3d Cbox, Lbox, Hbox; // min-max box center
	public double rbox = 1000; // half-diagonal of enclosing box
	private double r = 4; // detail size: radius of spheres for displaying vertices and of gaps between triangles when showing edges

	// rendering modes: showEdges shoes edges as gaps Smooth shading works only when !showEdge
	public Boolean flatShading = true;
	public Boolean showEdges = false;
	
	// primary tables
	v3d[] G = new v3d[maxnv]; // geometry table (vertices)
	int[] V = new int[3 * maxnt]; // V table (triangle/vertex indices)
	int[] O = new int[3 * maxnt]; // O table (opposite corner indices)
	
	// Normal vectors
	v3d[] Nv = new v3d[maxnv]; // vertex normals or laplace vectors
	v3d[] Nt = new v3d[maxnt]; // triangles normals
	
	// auxiliary vertices
	int[] W = new int[3 * maxnt]; // mid-edge vertex indices for subdivision (associated with corner opposite to edge)

	// vertex markers
	int[] vm = new int[3 * maxnt]; // vertex markers: 0=not marked, 1=interior, 2=border, 3=non manifold
	int[] Valence = new int[maxnv]; // vertex valence (count of incident triangles)
	boolean[] Border = new boolean[maxnv]; // vertex is border

	// triangle markers
	boolean[] visible = new boolean[maxnt]; // set if triangle visible, used to hide/unhide and then delete triangles
	
	// ================ INIT, CREATE, COPY ==================
	public Shape3d(PApplet p) {
		//rend = new render(pa); 
		Cbox = new v3d(p.width/2, p.height/2, 0);
	}

	public Shape3d declareVectors() {
		for (int i = 0; i < maxnv; i++) {
			G[i] = new v3d();
			Nv[i] = new v3d();
		} // init vertices and normals
		for (int i = 0; i < maxnt; i++)
			Nt[i] = new v3d(); // init triangle normals and skeleton lab els
		return this;
	}

	Shape3d emv3dy() {
		nv = 0;
		nt = 0;
		nc = 0;
		return this;
	}

	void resetCounters() {
		nv = 0;
		nt = 0;
		nc = 0;
	}
	public void printBorderCorners(){
		for (int c=0; c<nc; c++){
			if (b(c)) System.out.println("border: corner: "+c+", edge: "+v(p(c))+ ","+v(n(c))); 
			if (NME[c]) System.out.println("non-manifold corner: "+c+", vert: "+v(c)+", edge: "+v(p(c))+ ","+v(n(c))); 
		}
	}

	double averageEdgeLength() {
		double L = 0;
		for (int c = 0; c < nc; c++)
			L += v3d.dot(g(n(c)), g(p(c)));
		edgeLength = L / nc;
		return edgeLength;
	}
	
	void setr() {
		r = averageEdgeLength() / 10;
	}

	public Shape3d resetMarkers() { 
		for (int i = 0; i < nv; i++)
			vm[i] = 0;
		for (int i = 0; i < nt; i++)
			visible[i] = true;
		return this;
	}
	
	float ang(int c, int f) {
		v3d T = v3d.U(v3d.V(g(n(c)), g(p(c)))); // tangent to shared edge
		v3d N = v3d.U(v3d.cross(v3d.V(g(c), g(n(c))), v3d.V(g(c), g(p(c))))); // normal to t(c)
		v3d B = v3d.U(v3d.cross(N, T)); // binormal pointing inside triangle
		v3d Q = g(n(c));
		v3d P = g(f);
		v3d QP = v3d.V(Q, P);
		v3d V = v3d.A(QP, -v3d.dot(QP, T), T);
		float a = PApplet.atan2((float)v3d.dot(V, N), (float)v3d.dot(V, B));
		if (a < 0)
			a += PApplet.TWO_PI;
		return a;
	}

	int addVertex(v3d P) {
		G[nv].set(P);
		nv++;
		return nv - 1;
	}

	int addVertex(double x, double y, double z) {
		G[nv] = G[nv].set(x, y, z); 
		nv++;
		return nv - 1;
	}

	void addTriangle(int i, int j, int k) {
		V[nc++] = i;
		V[nc++] = j;
		V[nc++] = k;
		visible[nt++] = true;
	} // adds a triangle

	void addTriangle(int i, int j, int k, int m) {
		V[nc++] = i;
		V[nc++] = j;
		V[nc++] = k;
		//tm[nt] = m;
		visible[nt++] = true;
	} // adds a triangle

	public Shape3d updateON() {
		computeO();
		computeNormals();
		return this;
	} // recomputes O and normals

	public void copyTo(Shape3d M) {
		for (int i = 0; i < nv; i++)
			M.G[i].set(G[i]);
		M.nv = nv;
		for (int i = 0; i < nc; i++)
			M.V[i] = V[i];
		M.nt = nt;
		for (int i = 0; i < nc; i++)
			M.O[i] = O[i];
		M.nc = nc;
		//M.resetMarkers();
	}
	// ===================== FLESHING ========================
	public Shape3d transform(Curve3d S0, Curve3d S1, int mapUsed, double s, double w) {
		/*for (int i = 0; i < nv; i++)
			G[i].set(bend.map(G[i], S0, S1, mapUsed, s, w));*/
		return this;
	}
	
	public Shape3d transform(Curve3d S1, int mapUsed, double s, double w) {
		/*for (int i = 0; i < nv; i++){
			v3d C0 = v3d.P(G[i].x, Cbox.y, Cbox.z); //hook point 
			double s0 = G[i].x - Lbox.x;  		
			G[i].set(bend.map(G[i], C0, s0, S1, mapUsed, s, w));
		}*/
		return this; 
	}
	// ==================== CORNER OPERATORS =========================
	// operations on a corner
	int t (int c) {return c/3;} // triangle of corner

	int n(int c) {
		return 3 * t(c) + (c + 1) % 3;
	} // next corner in the same t(c)

	int p(int c) {
		return n(n(c));
	} // previous corner in the same t(c)

	int v(int c) {
		return V[c];
	} // id of the vertex of c

	int o(int c) {
		return O[c];
	} // opposite (or self if it has no opposite)

	int l(int c) {
		return o(n(c));
	} // left neighbor (or next if n(c) has no opposite)

	int r(int c) {
		return o(p(c));
	} // right neighbor (or previous if p(c) has no opposite)

	int s(int c) {
		return n(l(c));
	} // swings around v(c) or around a border loop

	int u(int c) {
		return p(r(c));
	} // unswings around v(c) or around a border loop

	int c(int t) {
		return t * 3;
	} // first corner of triangle t

	boolean b(int c) {
		return O[c] == c;
	} // if faces a border (has no opposite)
	
	// operations on the selected corner cc
	
	// geometry for corner c
	v3d g(int c) {
		return G[v(c)];
	} // shortcut to get the point of the vertex v(c) of corner c

	// normals for t(c) (must be precomputed)
	v3d Nv(int c) {
		return (Nv[V[c]]);
	}

	v3d Nt(int c) {
		return (Nt[t(c)]);
	}
	// ================Bounding box and detail size =======================================
	
	Shape3d moveBy(v3d V) {
		for (int v = 0; v < nv; v++)
			G[v].add(V);
		Cbox.add(V);
		return this;
	}

	// ======================= O TABLE CONSTRUCTION=========================
	boolean[] NME = new boolean[3 * maxnt]; // E[c] is true when c faces a non-anifold edge
	void hook(int c, int d) {
		O[c] = d;
		O[d] = c;
	} // make opposite
	
	void computeO() { // computes O for oriented non-manifold meshes
		for (int c = 0; c < nc; c++)
			NME[c] = false; // assume initially that no edge is non-manifold
		int val[] = new int[nv];
		for (int v = 0; v < nv; v++) val[v] = 0;
		System.out.println("*2*");
		for (int c = 0; c < nc; c++) val[v(c)]++; // val[v] : vertex valence
		System.out.println("*3*");
		int fic[] = new int[nv];
		int rfic = 0;
		for (int v = 0; v < nv; v++) {
			fic[v] = rfic;
			rfic += val[v];
		} // fic[v] : head of list of incident corners
		for (int v = 0; v < nv; v++)
			val[v] = 0; // reset valences to be reused to track how many incident corners were already encountered for each vertex
		int[] C = new int[nc]; // will be filed with corner IDs c, sorted by v(c): for each vertex, the list of its val[v] incident corners starts at C[fic[v]]
		for (int c = 0; c < nc; c++)
			C[fic[v(c)] + val[v(c)]++] = c; // fill C, using val[v] to keep track of the end of the list for v
		for (int c = 0; c < nc; c++)
			O[c] = c; // init O table so that each corner has no opposite (i.e. faces a border edge)
		for (int v = 0; v < nv; v++)
			// for each vertex...
			for (int a = fic[v]; a < fic[v] + val[v] - 1; a++)
				for (int b = a + 1; b < fic[v] + val[v]; b++) { 
					// for each pair (C[a],C[b[]) of its incident corners
					int c = n(C[a]), d = p(C[b]); 
					// we'll test whether corners c and d match as unclaimed opposites
					if (v(n(c)) == v(p(d))) 
					// they are facing each other (we assume the mesh was properly oriented
						if (b(c) && b(d)) { 
							// both were unclaimed, so they can be hooked as opposites, but these are not ordered around their shared edge
							for (int e = b + 1; e < fic[v] + val[v]; e++) { 
								// look for a better opposite around the shared edge
								int f = n(C[e]);
								if (v(n(c)) == v(p(f)) && b(f))
									if (ang(c, f) < ang(c, d)){
										//pa.println("change:"+d+" to "+f);
										d = f; // *** JAREK
									}
							}
							hook(c, d);
						} else {
							System.out.println("since "+b(c)+" and "+b(d)+", we have non-manifold corners: "+c+", "+o(c)+", "+d+", "+o(d));
							NME[c] = true; 
							NME[o(c)] = true; 
							NME[d] = true; 
							NME[o(d)] = true; 
						} // if any one was claimed, mark them and their possible opposites as facing a non-manifold edge
				} // end of pairs
	} // end of computeO

	void computeOvis() { // Computes O for the visible triangles
		// resetMarkers();
		int val[] = new int[nv];
		for (int v = 0; v < nv; v++)
			val[v] = 0;
		for (int c = 0; c < nc; c++)
			if (visible[t(c)])
				val[v(c)]++; // valences
		int fic[] = new int[nv];
		int rfic = 0;
		for (int v = 0; v < nv; v++) {
			fic[v] = rfic;
			rfic += val[v];
		}// head of list of incident corners
		for (int v = 0; v < nv; v++)
			val[v] = 0; // valences wil be reused to track how many incident corners were encountered for each vertex
		int[] C = new int[nc];
		for (int c = 0; c < nc; c++)
			if (visible[t(c)])
				C[fic[v(c)] + val[v(c)]++] = c; // for each vertex: the list of val[v] incident corners starts at C[fic[v]]
		for (int c = 0; c < nc; c++)
			O[c] = c; // init O table to -1 meaning that a corner has no opposite (i.e. faces a border)
		for (int v = 0; v < nv; v++)
			// for each vertex...
			for (int a = fic[v]; a < fic[v] + val[v] - 1; a++)
				for (int b = a + 1; b < fic[v] + val[v]; b++) { // for each pair (C[a],C[b[]) of its incident corners
					if (v(n(C[a])) == v(p(C[b]))) {
						O[p(C[a])] = n(C[b]);
						O[n(C[b])] = p(C[a]);
					} // if C[a] follows C[b] around v, then p(C[a]) and n(C[b]) are opposite
					if (v(n(C[b])) == v(p(C[a]))) {
						O[p(C[b])] = n(C[a]);
						O[n(C[a])] = p(C[b]);
					}
				}
	}

	// =================== DISPLAY TRIANGLES ==========================
	// displays triangle if marked as visible using flat or smooth shading depending on flatShading variable
	void shade(int t, PApplet pa) { // displays triangle t if visible
		pa.beginShape();
		g(3 * t).vert(pa);
		g(3 * t + 1).vert(pa);
		g(3 * t + 2).vert(pa);
		pa.endShape(PApplet.CLOSE);
	}
	
	// display front and back triangles shrunken if showEdges
	Boolean frontFacing(int t) {
		return !v3d.clockwise(view.E, g(3 * t), g(3 * t + 1), g(3 * t + 2));
	}
	
	public void showFront(PApplet pa) {
		for (int t = 0; t < nt; t++)
			if (frontFacing(t))
				shade(t, pa);
	}
	void showTs(PApplet pa) {
		for (int t = 0; t < nt; t++) shade(t, pa);
	}
	void showBackTriangles(PApplet pa) {
		for (int t = 0; t < nt; t++)
			if (!frontFacing(t))
				shade(t, pa);
	}
	// ==================== PROCESS EDGES======================================
	public void flip(int c) { // flip edge opposite to corner c, FIX border cases
		if (b(c))
			return;
		V[n(o(c))] = v(c);
		V[n(c)] = v(o(c));
		int co = o(c);

		O[co] = r(c);
		if (!b(p(c)))
			O[r(c)] = co;
		if (!b(p(co)))
			O[c] = r(co);
		if (!b(p(co)))
			O[r(co)] = c;
		O[p(c)] = p(co);
		O[p(co)] = p(c);
	}
	// =================PROCESS TRIANGLES =================
	void writeTri(int i) {
		System.out.println("T" + i + ": V = (" + V[3 * i] + ":" + v(o(3 * i)) + ","
				+ V[3 * i + 1] + ":" + v(o(3 * i + 1)) + "," + V[3 * i + 2]
				+ ":" + v(o(3 * i + 2)) + ")");
	}
	// ================= NORMALS ===================================
	public void computeNormals() {
		computeTriNormals();
		computeVertexNormals();
	}
	void computeValenceAndResetNormals() { // caches valence of each vertex
		for (int i = 0; i < nv; i++) {
			Nv[i] = new v3d();
			Valence[i] = 0;
		}
		for (int i = 0; i < nc; i++) {
			Valence[v(i)]++;
		}
	}
	v3d triNormal(int t) {
		return v3d.cross(v3d.V(g(3 * t), g(3 * t + 1)), v3d.V(g(3 * t), g(3 * t + 2)));
	}
	void computeTriNormals() {
		for (int i = 0; i < nt; i++) {
			Nt[i].set(triNormal(i));
		}
	}
	void computeVertexNormals() { 
		// computes the vertex normals as sums of the normal vectors of incident tirangles scaled by area/2
		for (int i = 0; i < nv; i++) {
			Nv[i].set(0, 0, 0);
		}
		for (int i = 0; i < nc; i++) {
			Nv[v(i)].add(Nt[t(i)]);
		}
		for (int i = 0; i < nv; i++) {
			Nv[i].makeUnit();
		}
	}
	void normalizeTriNormals() {
		for (int i = 0; i < nt; i++)
			Nt[i].makeUnit();
	}
	// ================= VOLUME====================================
	public double volume() {
		double v = 0;
		for (int i = 0; i < nt; i++)
			v += triVol(i);
		vol = v/6;
		return Math.abs(vol);
	}
	
	double triVol(int t) {
		return v3d.mixed(new v3d(), g(3 * t), g(3 * t + 1), g(3 * t + 2));
	}

	double surfaceArea() {
		double s = 0;
		for (int i = 0; i < nt; i++)
			s += triSurf(i);
		surf = s;
		return surf;
	}
	
	double triSurf(int t) {
		if (visible[t])
			return v3d.area(g(3 * t), g(3 * t + 1), g(3 * t + 2));
		else
			return 0;
	}
	// =================== SMOOTHING=====================
	void computeLaplaceVectors() { 
		// computes the vertex normals as sums of the normal vectors of incident tirangles scaled by area/2
		computeValenceAndResetNormals();
		for (int i = 0; i < 3 * nt; i++) {
			Nv[v(p(i))].add(v3d.V(g(p(i)), g(n(i))));
		}
		for (int i = 0; i < nv; i++) {
			Nv[i].div(Valence[i]);
		}
	}
	
	void tuck(double s) {
		for (int i = 0; i < nv; i++)
			G[i].add(s, Nv[i]);
	} // displaces each vertex by a fraction s of its normal

	public void smoothen() {
		computeNormals();
		computeLaplaceVectors();
		tuck(0.6f);
		computeLaplaceVectors();
		tuck(-0.6f);
	}

	// ==================== SUBDIVISION============================
	int w(int c) {
		return (W[c]);
	} // temporary indices to mid-edge vertices associated with corners during subdivision

	void splitEdges() { 
		// creates a new vertex for each edge and stores its ID in the W of the corner (and of its opposite if any)
		for (int i = 0; i < 3 * nt; i++) { // for each corner i
			if (b(i)) {
				G[nv] = v3d.mid(g(n(i)), g(p(i)));
				W[i] = nv++;
			} else {
				if (i < o(i)) {
					G[nv] = v3d.mid(g(n(i)), g(p(i)));
					W[o(i)] = nv;
					W[i] = nv++;
				}
			}
		}
	} // if this corner is the first to see the edge

	void bulge() { // tweaks the new mid-edge vertices according to the Butterfly mask
		for (int i = 0; i < 3 * nt; i++) {
			if ((!b(i)) && (i < o(i))) { // no tweak for mid-vertices of border edges
				if (!b(p(i)) && !b(n(i)) && !b(p(o(i))) && !b(n(o(i)))) {
					G[W[i]].add(0.25f, v3d.V(v3d.mid(v3d.mid(g(l(i)), g(r(i))), v3d.mid(g(l(o(i))), g(r(o(i))))), (v3d.mid(g(i), g(o(i))))));
				}
			}
		}
	}

	void splitTriangles() { // splits each tirangle into 4
		for (int i = 0; i < 3 * nt; i = i + 3) {
			V[3 * nt + i] = v(i);
			V[n(3 * nt + i)] = w(p(i));
			V[p(3 * nt + i)] = w(n(i));
			V[6 * nt + i] = v(n(i));
			V[n(6 * nt + i)] = w(i);
			V[p(6 * nt + i)] = w(p(i));
			V[9 * nt + i] = v(p(i));
			V[n(9 * nt + i)] = w(n(i));
			V[p(9 * nt + i)] = w(i);
			V[i] = w(i);
			V[n(i)] = w(n(i));
			V[p(i)] = w(p(i));
		}
		nt = 4 * nt;
		nc = 3 * nt;
	}

	public Shape3d refine() {
		updateON();
		splitEdges();
		bulge();
		splitTriangles();
		updateON();
		return this;
	}

	Shape3d split() {
		updateON();
		splitEdges();
		splitTriangles();
		updateON();
		return this;
	}
	
	// ===================== GARBAGE COLLECTION ===========================================
	void clean() {
		excludeInvisibleTriangles();
		System.out.println("excluded");
		compactVO();
		System.out.println("compactedVO");
		compactV();
		System.out.println("compactedV");
		computeNormals();
		System.out.println("normals");
		computeO();
		//resetMarkers();
	} // removes deleted triangles and unused vertices

	void excludeInvisibleTriangles() {
		for (int b = 0; b < nc; b++) {
			if (!visible[t(o(b))]) {
				O[b] = b;
			}
		}
	}

	void compactVO() {
		int[] U = new int[nc];
		int lc = -1;
		for (int c = 0; c < nc; c++) {
			if (visible[t(c)]) {
				U[c] = ++lc;
			}
		}
		for (int c = 0; c < nc; c++) {
			if (!b(c)) {
				O[c] = U[o(c)];
			} else {
				O[c] = c;
			}
		}
		int lt = 0;
		for (int t = 0; t < nt; t++) {
			if (visible[t]) {
				V[3 * lt] = V[3 * t];
				V[3 * lt + 1] = V[3 * t + 1];
				V[3 * lt + 2] = V[3 * t + 2];
				O[3 * lt] = O[3 * t];
				O[3 * lt + 1] = O[3 * t + 1];
				O[3 * lt + 2] = O[3 * t + 2];
				visible[lt] = true;
				lt++;
			}
		}
		nt = lt;
		nc = 3 * nt;
		System.out.println("...  NOW: nv=" + nv + ", nt=" + nt + ", nc=" + nc);
	}

	void compactV() {
		System.out.println("COMPACT VERTICES: nv=" + nv + ", nt=" + nt + ", nc=" + nc);
		int[] U = new int[nv];
		boolean[] deleted = new boolean[nv];
		for (int v = 0; v < nv; v++) {
			deleted[v] = true;
		}
		for (int c = 0; c < nc; c++) {
			deleted[v(c)] = false;
		}
		int lv = -1;
		for (int v = 0; v < nv; v++) {
			if (!deleted[v]) {
				U[v] = ++lv;
			}
		}
		for (int c = 0; c < nc; c++) {
			V[c] = U[v(c)];
		}
		lv = 0;
		for (int v = 0; v < nv; v++) {
			if (!deleted[v]) {
				G[lv].set(G[v]);
				deleted[lv] = false;
				lv++;
			}
		}
		nv = lv;
		System.out.println("      ...  NOW: nv=" + nv + ", nt=" + nt + ", nc=" + nc);
	}

	// =============== ARCHIVAL==================================
	boolean flipOrientation = false; // if set, save will flip all triangles

	public void saveMeshVTS(PApplet pa) {
		String savePath = pa.selectOutput("Select or specify .vts file where the mesh will be saved"); 																			// chooser
		if (savePath == null) {
			System.out.println("No output file was selected...");
			return;
		} else
			System.out.println("writing to " + savePath);
		saveMeshVTS(savePath, pa);
	}
	

	void saveMeshVTS(String fn, PApplet pa) {
		String[] inppts = new String[nv + 1 + nt + 1];
		int s = 0;
		inppts[s++] = PApplet.str(nv);
		for (int i = 0; i < nv; i++) {
			inppts[s++] = G[i].toString(); 
		}
		inppts[s++] = PApplet.str(nt);
		if (flipOrientation) {
			for (int i = 0; i < nt; i++) {
				inppts[s++] = PApplet.str(V[3 * i]) + "," + PApplet.str(V[3 * i + 2]) + ","
						+ PApplet.str(V[3 * i + 1]);
			}
		} else {
			for (int i = 0; i < nt; i++) {
				inppts[s++] = PApplet.str(V[3 * i]) + "," + PApplet.str(V[3 * i + 1]) + ","
						+ PApplet.str(V[3 * i + 2]);
			}
		}
		pa.saveStrings(fn, inppts);
	}

	public Shape3d loadMeshVTS(PApplet pa) {
		String loadPath = pa.selectInput("Select .vts mesh file to load"); 
		if (loadPath == null) {
			System.out.println("No input file was selected...");
			return this;
		} else
			System.out.println("reading from " + loadPath);
		loadMeshVTS(loadPath, pa);
		return this;
	}

	public Shape3d loadMeshVTS(String fn, PApplet pa) {
		System.out.println("loading: " + fn);
		String[] ss = pa.loadStrings(fn);
		int s = 0;
		int comma1, comma2;
		double x, y, z;
		int a, b, c;
		nv = Integer.parseInt(ss[s++]);
		System.out.println("nv=" + nv);
		for (int k = 0; k < nv; k++) {
			int i = k + s;
			comma1 = ss[i].indexOf(',');
			x = Float.parseFloat(ss[i].substring(0, comma1));
			String rest = ss[i].substring(comma1 + 1, ss[i].length());
			comma2 = rest.indexOf(',');
			y = Float.parseFloat(rest.substring(0, comma2));
			z = Float.parseFloat(rest.substring(comma2 + 1, rest.length()));
			G[k].set(x, y, z);
			pa.println(k+":"+"("+x+","+y+","+z+"),");
		}
		s = nv + 1;
		nt = Integer.parseInt(ss[s]);
		nc = 3 * nt;
		System.out.println("nt=" + nt);
		s++;
		for (int k = 0; k < nt; k++) {
			int i = k + s;
			comma1 = ss[i].indexOf(',');
			a = Integer.parseInt(ss[i].substring(0, comma1));
			String rest = ss[i].substring(comma1 + 1, ss[i].length());
			comma2 = rest.indexOf(',');
			b = Integer.parseInt(rest.substring(0, comma2));
			c = Integer.parseInt(rest.substring(comma2 + 1, rest.length()));
			V[3 * k] = a;
			V[3 * k + 1] = b;
			V[3 * k + 2] = c;
		}
		System.out.println("done loading");
		return this;
	}

}
