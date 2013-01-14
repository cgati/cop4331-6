package com.cgati.cop4331.towerdefense.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.cgati.cop4331.towerdefense.core.TowerDefense;

public class TowerDefenseHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("towerdefense/");
    PlayN.run(new TowerDefense());
  }
}
