package team.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import team.core.ElementDefense;

public class ElementDefenseActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("team/resources");
    PlayN.run(new ElementDefense());
  }
}
