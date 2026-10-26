import dev.pixelair.launcher.WidgetGeometry;
public class WidgetGeometryTest {
 static void check(boolean b){if(!b)throw new AssertionError();}
 public static void main(String[] args){WidgetGeometry g=new WidgetGeometry(-.4f,1.2f,2,.01f);check(g.x==0&&g.width==1&&g.height==.08f&&g.y<=.92f);g=new WidgetGeometry(.1f,.2f,.3f,.2f);check(g.overlaps(.2f,.3f,.5f,.5f));check(!g.overlaps(.4f,.4f,.6f,.6f));check(!g.overlaps(.5f,.2f,.6f,.4f));WidgetGeometry copy=g.copy();copy.x=.8f;copy.clamp();check(copy.x+copy.width<=1&&g.x==.1f);check(g.overlaps(.1f,.2f,.4f,.4f));System.out.println("PASS normalized drag/resize bounds, overlap edges and independent placement copy");}
}
