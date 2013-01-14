package com.cgati.cop4331.towerdefense.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.cgati.cop4331.towerdefense.core.TowerDefense;

public class TowerDefenseJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("com/cgati/cop4331/towerdefense/resources");
    PlayN.run(new TowerDefense());
  }
}
