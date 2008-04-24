package colladaLoader;


import processing.core.*;
import processing.xml.*;
import processing.opengl.*;
import javax.media.opengl.*;
import java.util.Vector;
import java.nio.*;



/**
 * @author Matt Ditton AKA polymonkey
 * @author matt@polymonkey.com
 * @version 005
 */

/**
 * TODO Support more than one material. (just like OBJ loader)
 * correct implementation is to load all elements into data and use library_visual_scene to join it all together
 * TODO Camera loading and drawing support
 * TODO Lights loading and drawing
 * TODO Animation framework system (will take a LARGE amount of work)
 * 
 * google code address
 * http://code.google.com/p/polymonkey-collada-importer/
 */


public class ColladaModel implements PConstants {

	Vector vertVector, normVector, triListVector, uvVector;

	ModelGroup[] modelGroup = new ModelGroup[0];
	
	Point3D v;

	Normal3D n;

	TriangleList t;

	Material m;

	UV uv;
	
	FloatBuffer vertFB, normFB, uvFB;
	
	IntBuffer triListIB;

	BoundingBox boundingBox;

	PApplet parent;
	
	PGraphicsOpenGL pgl;
	
	GL gl;

	int mode = PApplet.TRIANGLES;

	public float normalLength = 20;

	public float modelScale = 1;

	public boolean showWireFrame, showVertNormals, showFaceNormals, showBoundingBox;

	public boolean showMaterial = true;

	public boolean showModels = true;

	String filePath;

	Debug debug;



	// -------------------------------------------------------------------------
	// ------------------------------------------------------------- Constructor
	// -------------------------------------------------------------------------

	public ColladaModel(PApplet parent) {

		this.parent = parent;

		vertVector = new Vector();

		normVector = new Vector();

		triListVector = new Vector();

		uvVector = new Vector();

		m = new Material(this.parent);

		boundingBox = new BoundingBox();

		debug = new Debug();

		debug.enabled = true;

		String message[] = {"Processing Collada Loader",
							"matt ditton AKA polymonkey",
							"matt@polymonkey.com",
							"version 005 - 23/04/08",
							"http://code.google.com/p/polymonkey-collada-importer/"};

		debug.println(message);


	}

	public void setUpVBO()
	{
		
		/*
		 * lots of crazy temp stuff going here. 
		 * This is not fit for human consumption. But it does work..... ish
		 * 
		 * 
		 */

		 pgl = (PGraphicsOpenGL) this.parent.g;
		  
		 gl = pgl.gl;
		
		debug.println();
		
		int count = 0;
		
		//Building a float array for the verts
		float[] tempVerts = new float[ vertVector.size() * 3];
		
		for(int i = 0; i < tempVerts.length - 3; i += 3 ){
			
			Point3D p = (Point3D)vertVector.elementAt(count);	
				
			count++;
			
			tempVerts[i] = p.x;
			tempVerts[i+1] = p.y;
			tempVerts[i+2] = p.z;
			
		}
		
		debug.println("out");
		
		debug.println("one");
		
		vertFB = ByteBuffer.allocateDirect(4 * tempVerts.length).order( ByteOrder.nativeOrder() ).asFloatBuffer();
		vertFB.put(tempVerts);  
		vertFB.rewind();

		
//		normFB = ByteBuffer.allocateDirect(4*normVector.size()).order(ByteOrder.nativeOrder( )).asFloatBuffer();
//		normFB.put(normVector);  
//		normFB.rewind();
//		
//		uvFB = ByteBuffer.allocateDirect(4*uvVector.size()).order(ByteOrder.nativeOrder( )).asFloatBuffer();
//		uvFB.put(uvVector);  
//		uvFB.rewind();
//
//		gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
//		gl.glNormalPointer(GL.GL_FLOAT, 0, normFB);
//		
//		gl.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY);
//		gl.glColorPointer(3, GL.GL_FLOAT, 0, uvFB);
		
		
		//Build an int array for the indexes
		
		int[] tempVertIndex = new int[ triListVector.size() ];	
		
		debug.println(tempVertIndex.length);
		
		for(int i = 0; i < triListVector.size(); i ++ ){
			
			TriangleList t = (TriangleList) triListVector.elementAt(i);

			tempVertIndex[i] = t.points;
					
		}
		
		
		triListIB = ByteBuffer.allocateDirect(4 * tempVertIndex.length).order( ByteOrder.nativeOrder() ).asIntBuffer();
		triListIB.put(tempVertIndex);
		triListIB.rewind();
		
		
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, vertFB);

		
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_SMOOTH);
		
		debug.println("five");

	}
	
	
	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------- Draw
	// -------------------------------------------------------------------------

	public void drawOPENGL(){
		
		parent.pushMatrix();

		parent.scale(modelScale);
		
		if (showModels){
		
			pgl.beginGL();

			//gl.glColor3f(0.5f, 0.5f, 0.5f);
			
			if (showWireFrame) {
				

				gl.glDrawElements(GL.GL_POINTS, triListVector.size(), GL.GL_UNSIGNED_INT, triListIB); 

			}
			else{
			
				gl.glDrawElements(GL.GL_TRIANGLES, triListVector.size(), GL.GL_UNSIGNED_INT, triListIB); 
				
			}
			
			pgl.endGL();

			
		}

		if (showVertNormals) {

			drawVertexNormals();

		}

		if (showFaceNormals) {

			drawFaceNormals();

		}

		if (showBoundingBox) {

			drawBoundingBox();

		}
		
		parent.popMatrix();
		
	}
	
	public void draw() {

		parent.pushMatrix();

		parent.scale(modelScale);

		if (showModels){

			drawModel();
		}

		if (showVertNormals) {

			drawVertexNormals();

		}

		if (showFaceNormals) {

			drawFaceNormals();

		}

		if (showBoundingBox) {

			drawBoundingBox();

		}

		parent.popMatrix();
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------- Draw Model
	// -------------------------------------------------------------------------

	void drawModel() {

		if (showMaterial) {

			m.draw();

		}

		if (showWireFrame) {

			parent.stroke(128);

		}

		if (m.textured && showMaterial) {

			parent.textureMode(NORMALIZED);

		}

		parent.beginShape(mode);

		if (m.textured && showMaterial) {

			parent.texture(m.img);

		}

		for (int i = 0; i < triListVector.size(); i++) {

			t = (TriangleList) triListVector.elementAt(i);

			v = (Point3D) vertVector.elementAt(t.points);

			n = (Normal3D) normVector.elementAt(t.normals);

			
			parent.normal(n.nx, n.ny, n.nz);

			
			if (m.textured && showMaterial) {

				uv = (UV) uvVector.elementAt(t.uvs);

				parent.vertex(v.x, v.y, v.z, uv.u, uv.v);

			}

			else {

				parent.vertex(v.x, v.y, v.z);

			}

		}

		parent.endShape();

		if (m.textured && showMaterial) {

			parent.textureMode(IMAGE);

		}
	}

	// -------------------------------------------------------------------------
	// ------------------------------------------------------- Draw Bounding Box
	// -------------------------------------------------------------------------

	void drawBoundingBox() {

		boundingBox.draw(parent);

	}

	// -------------------------------------------------------------------------
	// ----------------------------------------------------- Draw Vertex Normals
	// -------------------------------------------------------------------------

	void drawVertexNormals() {

		parent.stroke(255, 0, 255);

		for (int i = 0; i < triListVector.size(); i++) {

			t = (TriangleList) triListVector.elementAt(i);

			v = (Point3D) vertVector.elementAt(t.points);
			
			n = (Normal3D) normVector.elementAt(t.normals);

			arrow(v, n, normalLength);
		}
	}

	// -------------------------------------------------------------------------
	// ------------------------------------------------------- Draw Face Normals
	// -------------------------------------------------------------------------
	// maybe I should bust this out to another class. do all the pre-calc and then just draw it. hmmm
	
	void drawFaceNormals() {

		parent.stroke(0, 255, 255);

		Normal3D[] centerN = new Normal3D[3];
		
		Point3D[]  centerP = new Point3D[3];
		
		for (int i = 0; i < triListVector.size(); i += 3) {

			t = (TriangleList) triListVector.elementAt(i);

			centerN[0] = (Normal3D) normVector.elementAt(t.normals);
			
			centerP[0] = (Point3D) vertVector.elementAt(t.points);

			t = (TriangleList) triListVector.elementAt(i + 1);

			centerN[1] = (Normal3D) normVector.elementAt(t.normals);
			
			centerP[1] = (Point3D) vertVector.elementAt(t.points);

			t = (TriangleList) triListVector.elementAt(i + 2);

			centerN[2] = (Normal3D) normVector.elementAt(t.normals);
			
			centerP[2] = (Point3D) vertVector.elementAt(t.points);

			v = new Point3D(
					(centerP[0].x + centerP[1].x + centerP[2].x) / 3,
					(centerP[0].y + centerP[1].y + centerP[2].y) / 3,
					(centerP[0].z + centerP[1].z + centerP[2].z) / 3);

			n = new Normal3D(
					(centerN[0].nx + centerN[1].nx + centerN[2].nx) / 3,
					(centerN[0].ny + centerN[1].ny + centerN[2].ny) / 3,
					(centerN[0].nz + centerN[1].nz + centerN[2].nz) / 3);

			arrow(v, n, normalLength);

		}
	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------------------------- Load
	// -------------------------------------------------------------------------

	public void load(String fileName) {

		int lastFolderint = fileName.lastIndexOf('/');

		if (lastFolderint != -1) {
			
			filePath = fileName.substring(0, fileName.lastIndexOf('/')) + "/";
			
		}

		debug.println("------------------------------------ the file path");
		debug.println("file path = " + filePath);
		debug.println();

		parseXML(fileName);

	}

	// -------------------------------------------------------------------------
	// ------------------------------------------------------ Parse XML Function
	// -------------------------------------------------------------------------
	
	
	/*This parseXML needs to be broken down a LOT more. 
	 * Change to using a switch statement and then break down each NODE to it's own function.
	 * A LOT easier to deal with. Go function crazy. Less code is better code. (unless it's crap)
	 * Better to do this before adding any new features.
	*/	
	
	void parseXML(String fileName) {
		
		XMLElement xml;

		xml = new XMLElement(parent, fileName);

		String[] temp;

		// -------------------------------------------------------------------------
		// ---------------------------------- Load Triangle list into triList object
		// -------------------------------------------------------------------------

		XMLElement triangles = xml.getChild("library_geometries/geometry/mesh/triangles");

		XMLElement[] triangleArray = xml.getChildren("library_geometries/geometry/mesh/triangles");


		debug.println("------------------------------------ Loading Triangle Array");
		debug.println("Array length = " + triangleArray.length);
		debug.println();

		for(int i = 0; i < triangleArray.length; i++){

			debug.println(triangleArray[0].getChildCount() + " children under " + triangleArray[0].getName() + " at array index " + i);
			debug.println();

		}


		try {
			
			boolean hasUvs = false;

			//for(int j = 0; j < triangleArray.length; j++){

			int[] Index = new int[3];

			debug.println("------------------------------------ Loading Triangle List");
			debug.println(triangles.getChildCount() + " children under " + triangles.getName());
			debug.println();

			boolean didNormalAppear = false;

			int totalSemantics = 0;

			/*
			 * Triangles should be an array of the instances of triangles. then loop through the array. adding the triangles list to the to ModelGroup array. 
			 * 
			 */

			for (int i = 0; i < triangles.getChildCount(); i++) {

				XMLElement triChild = triangles.getChild(i);

				// Loop through to get the semantic names. this tells us what elements the model has.

				String semantic = triChild.getStringAttribute("semantic");

				if (semantic != null) {
					totalSemantics++;

					if (semantic.equals("VERTEX")) {

						Index[0] = triChild.getIntAttribute("offset");

					}

					if (semantic.equals("NORMAL")) {

						Index[1] = triChild.getIntAttribute("offset");

						didNormalAppear = true;

					}

					if (semantic.equals("TEXCOORD")) {

						Index[2] = triChild.getIntAttribute("offset");

						hasUvs = true;

					}
				}

				String childName = triChild.getName();

				if (childName.equals("p")) {

					triangles = triChild;

				}
			}

			/*
			 * Check to see what didn't appear in the input list. If it didn't
			 * appear then it has the same index as the verts.
			 */

			if (!didNormalAppear) {

				Index[1] = Index[0];

			}

			debug.println("vertex index is at " + Index[0]);
			debug.println("normal index is at " + Index[1]);

			if (hasUvs) {

				debug.println("tex index is at " + Index[2]);

			}

			debug.println();

			temp = PApplet.splitTokens(triangles.getContent());

			TriangleList tempTriList;

			for (int i = 0; i < temp.length; i += totalSemantics) {

				if (hasUvs) {
					tempTriList = new TriangleList(
							Integer.valueOf(temp[i + Index[0]]).intValue(), 
							Integer.valueOf(temp[i + Index[1]]).intValue(), 
							Integer.valueOf(temp[i + Index[2]]).intValue());
				}

				else {
					tempTriList = new TriangleList(
							Integer.valueOf(temp[i + Index[0]]).intValue(), 
							Integer.valueOf(temp[i + Index[1]]).intValue());

				}

				triListVector.addElement(tempTriList);
			}

			//}

			// -------------------------------------------------------------------------
			// ---------------------------------------------- Load Verts into vertVector
			// -------------------------------------------------------------------------

			XMLElement geometry[] = xml.getChildren("library_geometries/geometry/mesh/source/float_array");

			temp = PApplet.splitTokens(geometry[0].getContent());

			Point3D tempVert;

			for (int i = 0; i < temp.length; i += 3) {

				tempVert = new Point3D(
						Float.valueOf(temp[i]).floatValue(),
						-Float.valueOf(temp[i + 1]).floatValue(), 
						Float.valueOf(temp[i + 2]).floatValue()
				);

				vertVector.addElement(tempVert);

			}

			// -------------------------------------------------------------------------
			// -------------------------------------------- Load Normals into normVector
			// -------------------------------------------------------------------------

			temp = PApplet.splitTokens(geometry[1].getContent());

			Normal3D tempNormals;

			for (int i = 0; i < temp.length; i += 3) {

				tempNormals = new Normal3D(
						Float.valueOf(temp[i]).floatValue(),
						-Float.valueOf(temp[i + 1]).floatValue(), 
						Float.valueOf(temp[i + 2]).floatValue());

				normVector.addElement(tempNormals);

			}

			// -------------------------------------------------------------------------
			// ------------------------------------------------- Load uv's into uvVector
			// -------------------------------------------------------------------------

			if (hasUvs) {

				temp = PApplet.splitTokens(geometry[2].getContent());

				UV tempuv;

				for (int i = 0; i < temp.length; i += 2) {

					tempuv = new UV(
							Float.valueOf(temp[i]).floatValue(), 
							Float.valueOf(temp[i + 1]).floatValue());

					uvVector.addElement(tempuv);

				}
			}

			// -------------------------------------------------------------------------
			// ---------------------------- Get Material and load into Material object m
			// -------------------------------------------------------------------------

			XMLElement library_effects = xml.getChild("library_effects/effect/profile_COMMON/technique");

			debug.println("------------------------------------ Loading Materials");
			debug.println(library_effects.getChildCount() + " children under "+ library_effects.getName());
			debug.println();

			library_effects = library_effects.getChild(0);

			debug.println("------------------------------------ Loading Materials");
			debug.println(library_effects.getChildCount() + " children under " + library_effects.getName());
			debug.println();

			XMLElement[] theColor = library_effects.getChildren();

			for (int i = 0; i < theColor.length; i++) {

				String name = theColor[i].getName();

				debug.println("child " + i + ": " + name);

				XMLElement theColorChild = theColor[i].getChild(0);

				String content = theColorChild.getContent();

				debug.println(name + " = " + content);

				temp = PApplet.splitTokens(content);

				if (name.equals("emission")) {

					m.emis = parent.color(getRGBAfromColorString(temp));

				}

				else if (name.equals("diffuse")) 
				{

					m.diffuse = parent.color(getRGBAfromColorString(temp));

					if (theColorChild.getStringAttribute("texture") != null) 
					{
						m.textured = true;

						debug.println("Will search for texture instead of color");

					}
				}

				else if (name.equals("ambient")) {

					m.amb = (getRGBAfromColorString(temp));

				}

				else if (name.equals("specular")) {

					m.spec = (getRGBAfromColorString(temp));

				}

				else if (name.equals("shininess")) {

					m.shini = getShinefromFloatString(temp);

				}

				debug.println();

			}

			if (m.textured) {

				XMLElement library_images = xml.getChild("library_images/image/init_from");

				temp = PApplet.splitTokens(library_images.getContent(), "./\\");

				debug.println("------------------------------------ Loading Texture");
				debug.println("texture listed as " + library_images.getContent());
				debug.println("loading " + filePath + PApplet.join(temp, "."));
				debug.println();

				if (filePath != null) {

					m.loadTexture(filePath + PApplet.join(temp, "."));

					m.originalTexture = filePath + PApplet.join(temp, ".");

				}

				else {

					m.loadTexture(PApplet.join(temp, "."));

					m.originalTexture = PApplet.join(temp, ".");

				}
			}

			// -------------------------------------------------------------------------
			// ------------------------------------------------------ Setup Bounding Box
			// -------------------------------------------------------------------------

			setBoundingBox();

			debug.println("------------------------------------ Bounding Box");
			debug.println("min X, Y, Z = " + boundingBox.minBB.x + ", " + boundingBox.minBB.y + ", " + boundingBox.minBB.z);
			debug.println("max X, Y, Z = " + boundingBox.maxBB.x + ", " + boundingBox.maxBB.y + ", " + boundingBox.maxBB.z);
			debug.println("width, height, depth = " + boundingBox.width + ", " + boundingBox.height + ", " + boundingBox.depth);
			debug.println();

			debug.println("------------------------------------ Stats");
			debug.println("Number of triangles = " + triListVector.size());
			debug.println();

		} catch (Exception e) {
			debug.println();
			debug.println("------------------------------------------------------------------------------------------------------------");
			debug.println("--------------------------------------------- FAILED IN LOAD -----------------------------------------------");
			debug.println("------------------------------------------------------------------------------------------------------------");
			debug.println("|");
			debug.println("| Something in the dae file isn't supported by the loader. Here is the message");
			debug.println("|");
			debug.println("| Exception: " + e.fillInStackTrace());
			debug.println("|");
			debug.println("| Make sure the dae file has been triangulated before export");
			debug.println("|");
			debug.println("------------------------------------------------------------------------------------------------------------");
			debug.println("------------------------------------------------------------------------------------------------------------");
			debug.println("------------------------------------------------------------------------------------------------------------");
			debug.println();

		}

	}

	// -------------------------------------------------------------------------
	// ----------------------------------------------------------------- Helpers
	// -------------------------------------------------------------------------

	float getShinefromFloatString(String[] a) {

		float c = Float.valueOf(a[0]).floatValue();

		return c;
	}

	int getRGBAfromColorString(String[] s) {
		int c;

		/*
		 * Check to see the length of the passed in string array. an empty array
		 * indicates there is a diffuse texture instead of a diffuse color.
		 * 
		 * ToDo. Figure out the differences between different alpha settings.
		 * diffuse opaque in XSI -> 1,1,1,0 the rest of the world goes for
		 * 1,1,1,1
		 * 
		 * You suck XSI
		 */

		if (s.length == 4) {

			float r = Float.valueOf(s[0]).floatValue() * 255;
			float g = Float.valueOf(s[1]).floatValue() * 255;
			float b = Float.valueOf(s[2]).floatValue() * 255;
			// float a = Float.valueOf(s[3]).floatValue() * 255;
			float a = 255;

			c = ((int) (a) << 24) | ((int) (r) << 16) | ((int) (g) << 8) | (int) (b);

		}

		else {

			c = (255 << 24) | (255 << 16) | (255 << 8) | 255;

		}

		return c;
	}

	// -------------------------------------------------------------------------
	// -------------------------------- The arrow that appears in the drawNormal
	// -------------------------------------------------------------------------

	void arrow(Point3D p, Normal3D n, float l) {

		parent.line(p.x, p.y, p.z, p.x + (n.nx * l), p.y + (n.ny * l), p.z + (n.nz * l));

	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------- setup the Bounding Box
	// -------------------------------------------------------------------------

	void setBoundingBox() {

		Point3D[] tempPoints3D = new Point3D[vertVector.size()];

		for (int i = 0; i < vertVector.size(); i++) {

			tempPoints3D[i] = (Point3D) vertVector.elementAt(i);

		}

		boundingBox.setBoundingBox(tempPoints3D);

	}

	// -------------------------------------------------------------------------
	// -------------------------------------------------- Change the model scale
	// -------------------------------------------------------------------------
	//This isn't perfect. maybe this should change to be fitToSize(int)

	public void fitToSize(int x, int y, int z) {

		modelScale = (PApplet.dist(0, 0, 0, x, y, z)) / (PApplet.dist(boundingBox.minBB.x, boundingBox.minBB.y, boundingBox.minBB.z, boundingBox.maxBB.x, boundingBox.maxBB.y,boundingBox.maxBB.z));

		
		normalLength = 20 / modelScale;

		debug.println("------------------------------------ Model Scale");
		debug.println("Model Scale = " + modelScale);
		debug.println();

	}
	

	// -------------------------------------------------------------------------
	// --------------------------------------------------------- Boolean Toggles
	// -------------------------------------------------------------------------

	public void toggleDebug(){

		debug.enabled = (debug.enabled) ? false : true;

		PApplet.println("Debug = " + debug.enabled);

	}

	public void toggleBoundingBox(){

		showBoundingBox = (showBoundingBox) ? false : true;

		debug.println("Display Bounding Box = " + showBoundingBox);

	}

	public void toggleVertNormals(){

		showVertNormals = (showVertNormals) ? false : true;

		debug.println("Display Vertex Normals = " + showVertNormals);

	}

	public void toggleFaceNormals(){

		showFaceNormals = (showFaceNormals) ? false : true;

		debug.println("Display Face Normals = " + showVertNormals);

	}

	public void toggleWireFrame(){

		showWireFrame = (showWireFrame) ? false : true;
		
		if(showWireFrame){
			
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINES);
			
		}
		
		else{
			
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_SMOOTH);
			
		}

		debug.println("Display Wire Frame = " + showWireFrame);

	}

	public void toggleTexture() {

		m.textured = (m.textured) ? false : true;

		debug.println("Display Texture = " + m.textured);

	}


	public void toggleMaterial() {

		showMaterial = (showMaterial) ? false : true;

		debug.println("Display Material = " + showMaterial);

	}


	public void setTexture(PImage _img) {

		m.img = _img;

	}

	public void originalTexture() {

		m.img = parent.loadImage(m.originalTexture);

	}

	public void drawMode(int _mode) {

		this.mode = _mode;

		switch (mode) {

		case 16:
			debug.println("draw mode:\t\tPOINTS");
			break;

		case 32:
			debug.println("draw mode:\t\tLINES");
			break;

		case 256:
			debug.println("draw mode:\t\tPOLYGON");
			break;

		case 64:
			debug.println("draw mode:\t\tTRIANGLES");
			break;

		case 65:
			debug.println("draw mode:\t\tTRIANGLE_STRIP");
			break;

		case 128:
			debug.println("draw mode:\t\tQUADS");
			break;

		case 129:
			debug.println("draw mode:\t\tQUAD_STRIP");
			break;

		}
	}

	// -----------------------------------------------------------------------
	// --------------------------------------- Functions for library expansion
	// ------------------------------ (not doing anything with this stuff yet)
	// -----------------------------------------------------------------------

	public void dispose() {
		System.gc();
	}

}
