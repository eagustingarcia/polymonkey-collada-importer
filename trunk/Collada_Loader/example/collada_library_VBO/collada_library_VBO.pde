import processing.opengl.*;

import colladaLoader.*;

ColladaModel testModel = new ColladaModel(this);
PImage img = createImage(128,128,RGB);

boolean test;

PFont font;

void setup(){

  size(500, 500, OPENGL);

  font = loadFont("AGaramondPro-Regular-30.vlw");

  hint(ENABLE_ACCURATE_TEXTURES);
  hint(ENABLE_OPENGL_4X_SMOOTH);

  testModel.load("duck_triangulate.dae");
  //testModel.load("Seymour_triangulate.dae");

  testModel.setUpVBO();

  testModel.fitToSize(400, 400, 400);

  for (int i = 0; i < img.pixels.length; i++){

    img.pixels[i] = i;

  }
  frameRate(60);
}


void draw(){
  background(16);

  lights();

  pushMatrix();

  translate(width/2 , height , -height/2);

  rotateY(radians(frameCount));

  fill(255,255,255);

  stroke(64);

  if(test)
  {
    testModel.drawOPENGL();
  }
  else
  {
    testModel.draw(); 
  }

  popMatrix();

  textFont(font);

  text("fps:" + nf(frameRate, 3, 2), 10, height - 10);

}



void keyPressed(){

  switch(key){

  case ' ' :
    testModel.toggleDebug();
    break;

  case '1' :
    testModel.toggleBoundingBox();
    break;

  case '2' :
    testModel.toggleVertNormals();
    break;

  case '3' :
    testModel.toggleFaceNormals();
    break;

  case '4' :
    testModel.toggleWireFrame();
    break;

  case '5' :
    testModel.toggleTexture();
    break;

  case '6' :
    testModel.toggleMaterial();
    break;

  case '7' :
    testModel.setTexture(img);
    break;

  case '8' :
    testModel.originalTexture();
    break;

  } 
}

void mousePressed(){

  test = (test) ? false : true; 

}
