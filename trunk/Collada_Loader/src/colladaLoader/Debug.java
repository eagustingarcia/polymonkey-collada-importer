package colladaLoader;

import processing.core.*;

/**
 * @author Matt Ditton AKA polymonkey
 * @author matt@polymonkey.com
 *
 */


//-------------------------------------------------------------------------
//---------------------------------------------------------- Debug printing 
//-------------------------------------------------------------------------;


public class Debug {

  public boolean enabled = true;

  Debug(){
  }

  public void println(int i){
    if(enabled)
      PApplet.println(PApplet.str(i));
  }


  public void println(float f){
    if(enabled)
      PApplet.println(PApplet.str(f));
  }


  public void println(String s){
    if(enabled)
      PApplet.println(s);
  }
  

  public void println(String[] s){
    if(enabled)
      for(int i = 0; i < s.length; i ++){
        PApplet.println(s[i]);
      }
      PApplet.println();
  }


  public void println(){
    if(enabled)
      PApplet.println();
  }


  public void print(String s){
    if(enabled)
      PApplet.print(s);
  }
}
