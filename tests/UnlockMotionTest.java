import dev.pixelair.launcher.UnlockMotion;
public class UnlockMotionTest {
 public static void main(String[] args) {
  // Spring starts at rest, ends exactly in place and stays continuous.
  assert UnlockMotion.spring(0)==0; assert UnlockMotion.spring(1)==1; assert UnlockMotion.spring(-1)==0; assert UnlockMotion.spring(2)==1;
  float peak=0,previous=0;int crossing=-1;
  for(int n=1;n<=1000;n++){float v=UnlockMotion.spring(n/1000f);assert Math.abs(v-previous)<.02f;if(crossing<0&&v>=1)crossing=n;peak=Math.max(peak,v);previous=v;}
  // Overshoot is visible but small, arrival happens in the first half, and the tail has settled before the end.
  assert peak>1.05f&&peak<1.12f; assert crossing>0&&crossing<500;
  for(int n=900;n<1000;n++)assert Math.abs(UnlockMotion.spring(n/1000f)-1)<.01f;
  // Opacity rises monotonically and never exceeds fully opaque.
  float last=-1;for(int n=0;n<=100;n++){float a=UnlockMotion.fade(n/100f);assert a>=last&&a>=0&&a<=1;last=a;}
  assert UnlockMotion.fade(0)==0&&UnlockMotion.fade(1)==1;
  // Ripple: center first, farthest last, bounded, and safe for degenerate layouts.
  assert UnlockMotion.rippleDelay(0,500)==0; assert UnlockMotion.rippleDelay(500,500)==UnlockMotion.STAGGER;
  assert UnlockMotion.rippleDelay(250,500)==UnlockMotion.STAGGER/2; assert UnlockMotion.rippleDelay(900,500)==UnlockMotion.STAGGER;
  assert UnlockMotion.rippleDelay(10,0)==0; assert UnlockMotion.rippleDelay(Float.NaN,500)==0;
  // Dock moves half as much as the grid.
  assert Math.abs((1-UnlockMotion.startScale(true))*2-(1-UnlockMotion.startScale(false)))<1e-6; assert Math.abs(UnlockMotion.pull(true)*2-UnlockMotion.pull(false))<1e-6;
  // Whole entrance stays shorter than the previous 500 ms per-icon tween plus a short tail.
  assert UnlockMotion.totalDuration()<=520;
  System.out.println("PASS spring overshoot and settle, opacity bounds, ripple order, dock damping and total duration");
 }
}
