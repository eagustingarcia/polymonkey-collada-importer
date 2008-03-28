package colladaLoader;

import processing.core.*;

/**
 * @author Matt Ditton AKA polymonkey
 * @author matt@polymonkey.com
 *
 */

//-------------------------------------------------------------------------
//-------------------------------------- Material Storage and Draw Function 
//-------------------------------------------------------------------------

public class Material {

	int amb, emis, spec, diffuse;

	float shini;

	boolean textured;
	
	String originalTexture = "";

	PImage img;

	PApplet parent;

	Material(PApplet _parent) {

		parent = _parent;
		
		
		
	}

	public void draw() {

		parent.emissive(emis);
		
		parent.ambient(amb);

		parent.specular(spec);

		parent.shininess(shini);

		parent.fill(diffuse);

		parent.noStroke();

	}

	public void loadTexture(String s) {

		textured = false;

		img = parent.loadImage(s);

		textured = true;

	}
}