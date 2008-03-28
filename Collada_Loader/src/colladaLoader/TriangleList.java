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

    points = _points;
    normals = _normals;

  }

  public TriangleList(int _points, int _normals, int _uvs){

    points = _points;
    normals = _normals;
    uvs = _uvs;

  }
}