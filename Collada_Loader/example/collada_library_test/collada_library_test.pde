import colladaLoader.*;

ColladaModel testModel = new ColladaModel(this);
PImage img = createImage(128,128,RGB);

void setup(){

  size(500, 500, P3D);

  hint(ENABLE_ACCURATE_TEXTURES);
  hint(ENABLE_OPENGL_4X_SMOOTH);

  testModel.load("duck_triangulate.dae");
  //testModel.load("Seymour_triangulate.dae");

  testModel.fitToSize(400, 400, 400);

  for (int i = 0; i < img.pixels.length; i++){

    img.pixels[i] = i;

  }
}


void draw(){
  background(16);

  lights();

  translate(width/2 , height , -height/2);

  rotateY(radians(frameCount));

  fill(255,0,255);

  stroke(64);

  testModel.draw();
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

