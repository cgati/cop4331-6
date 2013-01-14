package com.cgati.cop4331.towerdefense.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.cgati.cop4331.towerdefense.core.TowerDefense;

public class TowerDefenseActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("com/cgati/cop4331/towerdefense/resources");
    PlayN.run(new TowerDefense());
  }
}
