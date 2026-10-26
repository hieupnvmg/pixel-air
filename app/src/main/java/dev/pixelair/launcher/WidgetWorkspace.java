package dev.pixelair.launcher;
import android.appwidget.AppWidgetHostView;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;

/** Places a live widget over the workspace, with a dedicated drag/resize surface during placement. */
final class WidgetWorkspace extends FrameLayout {
 final HomeActivity activity;
 final View base,legacyAnchor;
 final AppWidgetHostView host;
 final WidgetGeometry geometry;
 final boolean editing,legacy;
 View placementSurface;
 final Paint paint=new Paint(3);
 final ArrayList<RectF> occupied=new ArrayList<>();
 float downX,downY,startX,startY,startWidth,startHeight; boolean resizing;
 final int grip;
 WidgetWorkspace(HomeActivity a,View base,View anchor,AppWidgetHostView host,WidgetGeometry geometry,boolean editing,boolean legacy){
  super(a);activity=a;this.base=base;legacyAnchor=anchor;this.host=host;this.geometry=geometry;this.editing=editing;this.legacy=legacy;grip=a.dp(36);setWillNotDraw(false);
  addView(base,new LayoutParams(-1,-1));addView(host,new LayoutParams(1,1));setClipChildren(true);
  if(editing){setContentDescription(a.getString(R.string.widget_placement_surface));setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);host.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);placementSurface=new View(a);placementSurface.setContentDescription(a.getString(R.string.ui_055));placementSurface.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);placementSurface.setFocusable(true);placementSurface.setOnClickListener(v->Toast.makeText(a,a.getString(R.string.ui_062),Toast.LENGTH_SHORT).show());addView(placementSurface,new LayoutParams(1,1));}
 }
 @Override protected void onLayout(boolean changed,int l,int t,int r,int b){
  base.layout(0,0,getWidth(),getHeight());
  if(legacy&&legacyAnchor!=null&&!editing){Rect bounds=new Rect();legacyAnchor.getDrawingRect(bounds);offsetDescendantRectToMyCoords(legacyAnchor,bounds);geometry.x=bounds.left/(float)getWidth();geometry.y=bounds.top/(float)getHeight();geometry.width=bounds.width()/(float)getWidth();geometry.height=bounds.height()/(float)getHeight();geometry.clamp();}
  place();
 }
 void place(){if(getWidth()==0||getHeight()==0)return;android.appwidget.AppWidgetProviderInfo info=host.getAppWidgetInfo();int minW=info.minResizeWidth>0?info.minResizeWidth:info.minWidth,minH=info.minResizeHeight>0?info.minResizeHeight:info.minHeight;geometry.width=Math.max(Math.min(1,minW/(float)getWidth()),geometry.width);geometry.height=Math.max(Math.min(1,minH/(float)getHeight()),geometry.height);if(info.maxResizeWidth>0)geometry.width=Math.min(geometry.width,Math.min(1,Math.max(minW,info.maxResizeWidth)/(float)getWidth()));if(info.maxResizeHeight>0)geometry.height=Math.min(geometry.height,Math.min(1,Math.max(minH,info.maxResizeHeight)/(float)getHeight()));geometry.clamp();int x=Math.round(geometry.x*getWidth()),y=Math.round(geometry.y*getHeight()),w=Math.round(geometry.width*getWidth()),h=Math.round(geometry.height*getHeight());host.measure(MeasureSpec.makeMeasureSpec(w,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(h,MeasureSpec.EXACTLY));host.layout(x,y,x+w,y+h);if(placementSurface!=null)placementSurface.layout(x,y,x+w,y+h);if(editing)activity.placementValidity(valid());else syncReserved();invalidate();}
 void blocked(View view){if("occupied".equals(view.getTag())){Rect r=new Rect();view.getDrawingRect(r);offsetDescendantRectToMyCoords(view,r);occupied.add(new RectF(r.left/(float)getWidth(),r.top/(float)getHeight(),r.right/(float)getWidth(),r.bottom/(float)getHeight()));}else if(view instanceof ViewGroup){ViewGroup group=(ViewGroup)view;for(int n=0;n<group.getChildCount();n++)blocked(group.getChildAt(n));}}
 boolean valid(){if(getWidth()==0||getHeight()==0)return false;occupied.clear();blocked(base);for(RectF r:occupied)if(geometry.overlaps(r.left+.004f,r.top+.004f,r.right-.004f,r.bottom-.004f))return false;return true;}
 void reserved(View view,StringBuilder addresses){Object address=view.getTag(R.id.workspace_address);if(address instanceof Integer){Rect r=new Rect();view.getDrawingRect(r);offsetDescendantRectToMyCoords(view,r);if(Rect.intersects(r,new Rect(host.getLeft(),host.getTop(),host.getRight(),host.getBottom())))addresses.append(",").append(address).append(",");}if(view instanceof ViewGroup){ViewGroup group=(ViewGroup)view;for(int n=0;n<group.getChildCount();n++)reserved(group.getChildAt(n),addresses);}}
 void syncReserved(){if(activity.prefs.getInt("widget",-1)!=host.getAppWidgetId())return;StringBuilder addresses=new StringBuilder();reserved(base,addresses);String value=addresses.toString();if(!value.equals(activity.prefs.getString("widgetReserved","")))activity.prefs.edit().putString("widgetReserved",value).apply();}
 boolean containsWidget(float x,float y){Rect r=new Rect();return isAttachedToWindow()&&host.getGlobalVisibleRect(r)&&r.contains((int)x,(int)y);}
 boolean findSpace(){if(valid())return true;for(float y=0;y<=1-geometry.height+.001f;y+=.025f)for(float x=0;x<=1-geometry.width+.001f;x+=.025f){geometry.x=x;geometry.y=y;if(valid()){place();return true;}}geometry.x=.04f;geometry.y=.1f;geometry.clamp();place();return false;}
 void updateSize(){int w=Math.round(host.getWidth()/getResources().getDisplayMetrics().density),h=Math.round(host.getHeight()/getResources().getDisplayMetrics().density);if(w>0&&h>0)host.updateAppWidgetSize(null,w,h,w,h);}
 @Override public boolean onInterceptTouchEvent(MotionEvent e){return editing;}
 @Override public boolean onTouchEvent(MotionEvent e){if(!editing)return super.onTouchEvent(e);switch(e.getActionMasked()){
 case MotionEvent.ACTION_DOWN:downX=e.getX();downY=e.getY();startX=geometry.x;startY=geometry.y;startWidth=geometry.width;startHeight=geometry.height;resizing=downX>=host.getRight()-grip&&downX<=host.getRight()&&downY>=host.getBottom()-grip&&downY<=host.getBottom();if(!resizing&&(downX<host.getLeft()||downX>host.getRight()||downY<host.getTop()||downY>host.getBottom())){geometry.x=downX/getWidth()-geometry.width/2;geometry.y=downY/getHeight()-geometry.height/2;geometry.clamp();startX=geometry.x;startY=geometry.y;place();}getParent().requestDisallowInterceptTouchEvent(true);return true;
 case MotionEvent.ACTION_MOVE:float dx=(e.getX()-downX)/getWidth(),dy=(e.getY()-downY)/getHeight();if(resizing){geometry.width=startWidth+dx;geometry.height=startHeight+dy;}else{geometry.x=startX+dx;geometry.y=startY+dy;}place();return true;
 case MotionEvent.ACTION_UP:case MotionEvent.ACTION_CANCEL:updateSize();performClick();return true;default:return true;}}
 @Override public boolean performClick(){super.performClick();return true;}
 @Override protected void dispatchDraw(Canvas canvas){super.dispatchDraw(canvas);if(!editing)return;paint.setStyle(Paint.Style.STROKE);paint.setStrokeWidth(activity.dp(2));paint.setColor(valid()?0xFFB9D4FF:0xFFFF8B8B);RectF box=new RectF(host.getLeft()+2,host.getTop()+2,host.getRight()-2,host.getBottom()-2);canvas.drawRoundRect(box,activity.dp(12),activity.dp(12),paint);paint.setStyle(Paint.Style.FILL);canvas.drawCircle(host.getRight()-grip/2f,host.getBottom()-grip/2f,grip/2.5f,paint);paint.setColor(0xFF172033);paint.setTextSize(activity.dp(18));paint.setTextAlign(Paint.Align.CENTER);canvas.drawText("↘",host.getRight()-grip/2f,host.getBottom()-grip/2f+activity.dp(6),paint);}
}
