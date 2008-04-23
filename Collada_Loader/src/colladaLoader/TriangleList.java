package colladaLoader;

/**
 * @author Matt Ditton AKA polymonkey
 * @author matt@polymonkey.com
 *
 */

//-------------------------------------------------------------------------
//--------------------------------------------------- Triangle List Storage
//-------------------------------------------------------------------------

public class TriangleList{

  int points, normals, uvs;

  public TriangleList(int _points, int _normals){

    this.points = _points;
    this.normals = _normals;

  }

  public TriangleList(int _points, int _normals, int _uvs){

	  this.points = _points;
	  this.normals = _normals;
	  this.uvs = _uvs;

  }
}