package colladaLoader;

import java.util.Vector;

import processing.core.*;

//-------------------------------------------------------------------------
//----------------------------------------------------- Model Group Storage
//-------------------------------------------------------------------------

public class ModelGroup {

	Vector triListVector;

	Material m;

	BoundingBox boundingBox;

	ModelGroup(PApplet _parent) {

		triListVector = new Vector();

		m = new Material(_parent);

		boundingBox = new BoundingBox();

	}
}
