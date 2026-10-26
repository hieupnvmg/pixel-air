import dev.pixelair.launcher.SwipePhysics;
public class SwipePhysicsTest {
 public static void main(String[] args) {
  assert SwipePhysics.target(108, -108, 0, 0, 2, 1080, 1000, 24)==0;
  assert SwipePhysics.target(648, -648, 0, 0, 2, 1080, 1000, 24)==1;
  assert SwipePhysics.target(120, -120, -2400, 0, 2, 1080, 1000, 24)==1;
  assert SwipePhysics.target(800, -800, 2400, 0, 2, 1080, 1000, 24)==0;
  assert SwipePhysics.target(2050, -970, -2400, 0, 2, 1080, 1000, 24)==2;
  assert SwipePhysics.target(2150, -1070, -2400, 0, 1, 1080, 1000, 24)==1;
  assert SwipePhysics.target(1080, 0, 2000, 0, 2, 1080, 1000, 24)==1;
  assert SwipePhysics.target(1080, -1080, -2400, 0, 3, 1080, 1000, 24)==1;
  assert SwipePhysics.target(1080, 1080, 2400, 0, 3, 1080, 1000, 24)==1;
  assert SwipePhysics.duration(800, 5000, 1080)<SwipePhysics.duration(800, 1200, 1080);
  assert SwipePhysics.duration(1, 0, 1080)>=110;
  System.out.println("PASS full-width targets, short drag, fling, reversal, boundaries, velocity duration");
 }
}
