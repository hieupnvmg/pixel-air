package dev.pixelair.launcher;
import android.animation.ValueAnimator;
import android.view.*;
import android.view.animation.PathInterpolator;
import java.util.*;

/** Stage while covered by keyguard, then animate the very first visible Home frame. */
final class UnlockIconAnimator {
 private ValueAnimator iphoneAnimator;
 private final IdentityHashMap<ViewGroup,boolean[]> clipping=new IdentityHashMap<>();
 private final ArrayList<View> staged=new ArrayList<>(),animated=new ArrayList<>();
 boolean isPrepared(){return !staged.isEmpty();}
 void prepare(List<View> cells){if(isPrepared())return;cancel();for(View cell:cells){staged.add(cell);cell.setAlpha(0);}}
 void cancel(){ValueAnimator old=iphoneAnimator;iphoneAnimator=null;if(old!=null)old.cancel();for(View view:new ArrayList<>(animated)){view.animate().cancel();reset(view);}for(View view:staged)reset(view);animated.clear();staged.clear();restoreClipping();}
 private void restoreClipping(){for(Map.Entry<ViewGroup,boolean[]> entry:clipping.entrySet()){entry.getKey().setClipChildren(entry.getValue()[0]);entry.getKey().setClipToPadding(entry.getValue()[1]);}clipping.clear();}
 private void allowFlight(View cell,View root){for(ViewParent p=cell.getParent();p instanceof ViewGroup&&p!=root;p=p.getParent()){ViewGroup group=(ViewGroup)p;if(group instanceof WorkspacePager)continue;if(!clipping.containsKey(group))clipping.put(group,new boolean[]{group.getClipChildren(),group.getClipToPadding()});group.setClipChildren(false);group.setClipToPadding(false);}}
 private void reset(View view){view.setTranslationX(0);view.setTranslationY(0);view.setScaleX(1);view.setScaleY(1);view.setAlpha(1);}
 void startPrepared(View root,int style){if(!isPrepared())return;if(!ValueAnimator.areAnimatorsEnabled()||root.getWidth()==0){cancel();return;}int[] xy=new int[2];root.getLocationOnScreen(xy);float cx=xy[0]+root.getWidth()/2f,cy=xy[1]+root.getHeight()*.5f;
  ArrayList<View> cells=new ArrayList<>(staged);staged.clear();if(style==1){startIphone(root,cells);return;}
  for(View cell:cells){if(!cell.isAttachedToWindow()||cell.getWidth()==0){reset(cell);continue;}cell.getLocationOnScreen(xy);float dx=xy[0]+cell.getWidth()/2f-cx,dy=xy[1]+cell.getHeight()/2f-cy;allowFlight(cell,root);cell.setTranslationX(-dx*.26f);cell.setTranslationY(-dy*.26f);cell.setScaleX(.72f);cell.setScaleY(.72f);cell.setAlpha(0);animated.add(cell);long delay=0;cell.animate().translationX(0).translationY(0).scaleX(1).scaleY(1).alpha(1).setStartDelay(delay).setDuration(500).setInterpolator(new PathInterpolator(.18f,.78f,.2f,1f)).withLayer().withEndAction(()->{reset(cell);animated.remove(cell);if(animated.isEmpty())restoreClipping();}).start();}
  if(animated.isEmpty())restoreClipping();
 }
 private void startIphone(View root,ArrayList<View> cells){
  int[] xy=new int[2];float minX=Float.MAX_VALUE,maxX=-Float.MAX_VALUE,minY=Float.MAX_VALUE,maxY=-Float.MAX_VALUE;
  float dockMin=Float.MAX_VALUE,dockMax=-Float.MAX_VALUE;
  ArrayList<View> visible=new ArrayList<>();
  for(View cell:cells){if(!cell.isAttachedToWindow()||cell.getWidth()==0){reset(cell);continue;}visible.add(cell);cell.getLocationOnScreen(xy);
   float x=xy[0]+cell.getWidth()/2f,y=xy[1]+cell.getHeight()/2f;
   if(isDock(cell)){dockMin=Math.min(dockMin,x);dockMax=Math.max(dockMax,x);}else{minX=Math.min(minX,x);maxX=Math.max(maxX,x);minY=Math.min(minY,y);maxY=Math.max(maxY,y);}
  }
  float centerX=minX==Float.MAX_VALUE?0:(minX+maxX)/2,centerY=minY==Float.MAX_VALUE?0:(minY+maxY)/2;
  float dockCenter=dockMin==Float.MAX_VALUE?0:(dockMin+dockMax)/2;
  float[] dx=new float[visible.size()],dy=new float[visible.size()];boolean[] docks=new boolean[visible.size()];
  for(int n=0;n<visible.size();n++){View cell=visible.get(n);cell.getLocationOnScreen(xy);docks[n]=isDock(cell);dx[n]=xy[0]+cell.getWidth()/2f-(docks[n]?dockCenter:centerX);dy[n]=xy[1]+cell.getHeight()/2f-centerY;allowFlight(cell,root);animated.add(cell);}
  if(visible.isEmpty()){restoreClipping();return;}
  float density=root.getResources().getDisplayMetrics().density;
  ValueAnimator motion=ValueAnimator.ofFloat(0,1);iphoneAnimator=motion;motion.setDuration(UnlockMotion.DURATION_MS);motion.setInterpolator(new android.view.animation.LinearInterpolator());
  motion.addUpdateListener(a->{float t=(float)a.getAnimatedValue();float grid=UnlockMotion.gridScale(t),dock=UnlockMotion.dockScale(t),spread=UnlockMotion.dockSpread(t),rise=UnlockMotion.dockRise(t)*density;
   for(int n=0;n<visible.size();n++){View cell=visible.get(n);float scale=docks[n]?dock:grid;cell.setScaleX(scale);cell.setScaleY(scale);cell.setTranslationX(dx[n]*((docks[n]?spread:grid)-1));cell.setTranslationY(docks[n]?rise:dy[n]*(grid-1));cell.setAlpha(docks[n]?UnlockMotion.dockAlpha(t):UnlockMotion.gridAlpha(t));}
  });
  motion.addListener(new android.animation.AnimatorListenerAdapter(){@Override public void onAnimationEnd(android.animation.Animator a){if(iphoneAnimator==motion){iphoneAnimator=null;for(View cell:visible)reset(cell);animated.clear();restoreClipping();}}});motion.start();
 }
 private boolean isDock(View cell){Object address=cell.getTag(R.id.workspace_address);return address instanceof Integer&&(Integer)address<0;}

}
