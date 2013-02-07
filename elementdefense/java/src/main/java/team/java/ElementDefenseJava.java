package team.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import team.core.ElementDefense;

public class ElementDefenseJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("team/resources");
    PlayN.run(new ElementDefense());
  }
}
