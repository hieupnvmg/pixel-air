import dev.pixelair.launcher.SpringMotion;
public class SpringMotionTest {
 public static void main(String[] args) {
  SpringMotion s=new SpringMotion(300,1080,2500,24);
  assert s.position(0)==300; assert s.velocity(0)==2500;
  double h=.000001;assert Math.abs((s.position(h)-s.position(0))/h-2500)<.2;
  assert Math.abs(s.position(1)-1080)<.001;assert Math.abs(s.velocity(1))<.01;
  double previous=300;for(int n=1;n<=100;n++){double p=s.position(n/100.0);assert p>=previous-.0001;assert p<=1080.001;previous=p;}
  double time=0;for(int n=0;n<60;n++)time+=1.0/120;assert Math.abs(s.position(time)-s.position(.5))<1e-8;
  assert SpringMotion.rubberBand(500,0,1080,120)==500;
  for(float raw:new float[]{-500,-100,0,500,1080,1200,1600}){float p=SpringMotion.rubberBand(raw,0,1080,120);assert p>-120&&p<1200;assert Math.abs(SpringMotion.inverseRubberBand(p,0,1080,120)-raw)<.1;}
  float raw=-100,edge=SpringMotion.rubberBand(raw,0,1080,120);float numerical=(SpringMotion.rubberBand(raw+.1f,0,1080,120)-SpringMotion.rubberBand(raw-.1f,0,1080,120))/.2f;assert Math.abs(numerical-SpringMotion.velocityFactor(edge,0,1080,120))<.001;
  SpringMotion reverse=new SpringMotion(700,0,-2200,24);assert reverse.position(.1)<700;assert Math.abs(reverse.position(1))<.01;
  System.out.println("PASS release continuity, convergence, cadence independence, edge resistance and reversal");
 }
}
