package dev.pixelair.launcher;
/** Normalized workspace coordinates, independent of screen density and system insets. */
public final class WidgetGeometry {
 public float x,y,width,height;
 public WidgetGeometry(float x,float y,float width,float height){this.x=x;this.y=y;this.width=width;this.height=height;clamp();}
 public WidgetGeometry copy(){return new WidgetGeometry(x,y,width,height);}
 public void clamp(){width=Math.max(.18f,Math.min(1,width));height=Math.max(.08f,Math.min(1,height));x=Math.max(0,Math.min(1-width,x));y=Math.max(0,Math.min(1-height,y));}
 public boolean overlaps(float left,float top,float right,float bottom){return x<right&&x+width>left&&y<bottom&&y+height>top;}
}
