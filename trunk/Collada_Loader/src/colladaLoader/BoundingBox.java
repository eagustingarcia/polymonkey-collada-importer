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

  BoundingBox(){
	  
  }

  public void setBoundingBox(Point3D[] points){

    for(int i = 0; i < points.length; i++){

      if(i == 0){
    	  
    	  minBB = points[i];
    	  maxBB = points[i];
    	  
      }
      else{
	      minBB = minSize(minBB, points[i]);
	
	      maxBB = maxSize(maxBB, points[i]);
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
	  
	  a.x = minSize (a.x, b.x);
	  a.y = minSize (a.y, b.y);
	  a.z = minSize (a.z, b.z);
	  
	  return a;
	  
  }
  
  Point3D maxSize (Point3D a, Point3D b){
	  
	  a.x = maxSize (a.x, b.x);
	  a.y = maxSize (a.y, b.y);
	  a.z = maxSize (a.z, b.z);
	  
	  return a;  
  }
  
  float maxSize(float a, float b){
	  
	  return a = (a < b) ? b : a;
  }
  
 float minSize(float a, float b){
	  
	  return a = (a > b) ? b : a;
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
