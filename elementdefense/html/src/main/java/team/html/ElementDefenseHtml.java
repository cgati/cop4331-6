package team.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import team.core.ElementDefense;

public class ElementDefenseHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("elementdefense/");
    PlayN.run(new ElementDefense());
  }
}
