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

	PVector minBB = new PVector();
	PVector maxBB = new PVector();
	PVector centerBB = new PVector();
  float width, height, depth;


  BoundingBox(){

  }

  public void setBoundingBox(PVector[] p){

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

  PVector minSize (PVector a, PVector b){
	  
	  PVector test = new PVector();
	  
	  test.x = Math.min (a.x, b.x);
	  test.y = Math.min (a.y, b.y);
	  test.z = Math.min (a.z, b.z);
	  
	  return test;
	  
  }
  
  PVector maxSize (PVector a, PVector b){
	  
	  PVector test = new PVector();
	  
	  test.x = Math.max (a.x, b.x);
	  test.y = Math.max (a.y, b.y);
	  test.z = Math.max (a.z, b.z);
	  
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
