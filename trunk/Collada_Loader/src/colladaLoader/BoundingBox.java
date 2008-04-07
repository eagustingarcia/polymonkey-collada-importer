package colladaLoader;

import processing.core.*;

/**
 * @author Matt Ditton AKA polymonkey
 * @author matt@polymonkey.com
 *
 */

//-------------------------------------------------------------------------
//---------------------------------- Bounding Box Storage and Draw Function 
//-------------------------------------------------------------------------

public class BoundingBox{

  Point3D minBB = new Point3D();
  Point3D maxBB = new Point3D();
  Point3D centerBB = new Point3D();
  float width, height, depth;
//  PApplet parent;

  BoundingBox(){
//	  this.parent = _parent;
  }

  public void setBoundingBox(Point3D[] p){

    for(int i = 0; i < p.length; i++){

      if(i == 0){
    	  
    	  minBB = p[i];
    	  maxBB = p[i];
    	  
      }
      else{
    	  
	      minBB = minSize(minBB, p[i]);
	
	      maxBB = maxSize(maxBB, p[i]);
	      
      }
    }
    
    width =  Math.abs(minBB.x) + Math.abs(maxBB.x);
	height = Math.abs(minBB.y) + Math.abs(maxBB.y);
	depth =  Math.abs(minBB.z) + Math.abs(maxBB.z);
	
	centerBB.x = minBB.x + (( maxBB.x - minBB.x )/ 2);
	centerBB.y = minBB.y + (( maxBB.y - minBB.y )/ 2);
	centerBB.z = minBB.z + (( maxBB.z - minBB.z )/ 2);

  }

  Point3D minSize (Point3D a, Point3D b){
	  
	  Point3D test = new Point3D();
	  
	  test.x = PApplet.min (a.x, b.x);
	  test.y = PApplet.min (a.y, b.y);
	  test.z = PApplet.min (a.z, b.z);
	  
	  return test;
	  
  }
  
  Point3D maxSize (Point3D a, Point3D b){
	  
	  Point3D test = new Point3D();
	  
	  test.x = PApplet.max (a.x, b.x);
	  test.y = PApplet.max (a.y, b.y);
	  test.z = PApplet.max (a.z, b.z);
	  
	  return test;  
  }
  

  public void draw(PApplet _parent){

	  _parent.pushMatrix();
    
	  _parent.noFill();
    
	  _parent.stroke(255,0,255);
    
	  _parent.translate(centerBB.x, centerBB.y, centerBB.z);
	  
	  _parent.box(width, height, depth);
    
	  _parent.popMatrix();
  }
}
