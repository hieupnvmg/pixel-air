import dev.pixelair.launcher.ColorHarmony;
public class ColorHarmonyTest {
 public static void main(String[] args) {
  assert ColorHarmony.withAlpha(0xFF123456,0x80)==0x80123456; assert ColorHarmony.withAlpha(0x00ABCDEF,300)==0xFFABCDEF;
  assert ColorHarmony.blend(0xFF000000,0xFFFFFFFF,0)==0xFF000000; assert ColorHarmony.blend(0xFF000000,0xFFFFFFFF,1)==0xFFFFFFFF;
  assert ColorHarmony.blend(0xFF000000,0xFFFFFFFF,.5f)==0xFF808080;
  assert Math.abs(ColorHarmony.hue(0xFFFF0000))<.01; assert Math.abs(ColorHarmony.hue(0xFF00FF00)-120)<.01; assert Math.abs(ColorHarmony.hue(0xFF0000FF)-240)<.01;
  assert ColorHarmony.hue(0xFF808080)==-1;
  assert Math.abs(ColorHarmony.hueDelta(350,10)-20)<.01; assert Math.abs(ColorHarmony.hueDelta(10,350)+20)<.01;
  // Rotation keeps alpha and a full turn returns the original color.
  assert ColorHarmony.rotateHue(0x80FF0000,360)==0x80FF0000; assert ColorHarmony.rotateHue(0xFFFF0000,120)==0xFF00FF00;
  // Harmonize moves toward the source hue by at most 15 degrees and never overshoots.
  int[] colors={0xFFD8B4FF,0xFF91E2CB,0xFFF3A5A1,0xFFA6CFFF,0xFFF2BD98,0xFF8BDADD,0xFFC5B5ED,0xFFE8CD94,0xFFBDC6D5};
  int[] sources={0xFFFFB4AB,0xFFA8C7FA,0xFFB5D18A,0xFFE6BBFF};
  for(int c:colors)for(int src:sources){
   int h=ColorHarmony.harmonize(c,src);
   assert (h>>>24)==(c>>>24);
   float before=Math.abs(ColorHarmony.hueDelta(ColorHarmony.hue(c),ColorHarmony.hue(src))),after=Math.abs(ColorHarmony.hueDelta(ColorHarmony.hue(h),ColorHarmony.hue(src)));
   assert after<=before+.5f;
   assert Math.abs(ColorHarmony.hueDelta(ColorHarmony.hue(c),ColorHarmony.hue(h)))<=ColorHarmony.MAX_SHIFT+1f;
  }
  // Achromatic colors and achromatic sources are left unchanged.
  assert ColorHarmony.harmonize(0xFF808080,0xFFA8C7FA)==0xFF808080; assert ColorHarmony.harmonize(0xFFD8B4FF,0xFF777777)==0xFFD8B4FF;
  System.out.println("PASS alpha, blending, hue rotation, bounded harmonization and achromatic passthrough");
 }
}
