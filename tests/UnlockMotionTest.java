import dev.pixelair.launcher.UnlockMotion;
public class UnlockMotionTest {
 static void check(boolean ok){if(!ok)throw new AssertionError();}
 static boolean near(float a,float b){return Math.abs(a-b)<.0001;}
 public static void main(String[] args){
  check(UnlockMotion.gridScale(0)>2);check(UnlockMotion.gridAlpha(0)==0);
  check(UnlockMotion.dockScale(0)<1 && UnlockMotion.dockRise(0)>0 && UnlockMotion.dockAlpha(0)==0);
  float previous=UnlockMotion.gridScale(0),previousDock=UnlockMotion.dockScale(0),previousRise=UnlockMotion.dockRise(0);
  for(int n=1;n<=120;n++){float t=n/120f;float grid=UnlockMotion.gridScale(t),dock=UnlockMotion.dockScale(t),rise=UnlockMotion.dockRise(t);
   check(grid<=previous && grid>=1);check(dock>=previousDock && dock<=1);check(rise<=previousRise && rise>=0);
   previous=grid;previousDock=dock;previousRise=rise;
  }
  for(float t:new float[]{1,2}){check(near(UnlockMotion.gridScale(t),1));check(near(UnlockMotion.dockScale(t),1));check(near(UnlockMotion.dockSpread(t),1));check(near(UnlockMotion.dockRise(t),0));check(UnlockMotion.gridAlpha(t)==1 && UnlockMotion.dockAlpha(t)==1);}
  check(UnlockMotion.dockAlpha(.02f)==0);check(near(UnlockMotion.gridScale(-1),UnlockMotion.gridScale(0)));
  check(100*(UnlockMotion.gridScale(.25f)-1)>0 && -100*(UnlockMotion.gridScale(.25f)-1)<0); // outward grid starts, converges inward
  System.out.println("PASS unlock geometry: enlarged grid settles inward, dock rises/grows, bounded alpha and final reset");
 }
}
