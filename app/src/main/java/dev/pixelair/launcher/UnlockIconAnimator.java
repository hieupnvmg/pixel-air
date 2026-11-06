package dev.pixelair.launcher;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.*;
import android.view.animation.DecelerateInterpolator;
import java.util.*;

/** Stage while covered by keyguard, then animate the very first visible Home frame. Shape lives in UnlockMotion. */
final class UnlockIconAnimator {
 private final IdentityHashMap<ViewGroup,boolean[]> clipping=new IdentityHashMap<>();
 private final ArrayList<View> staged=new ArrayList<>(),animated=new ArrayList<>();
 private final IdentityHashMap<View,Animator> running=new IdentityHashMap<>();
 private final IdentityHashMap<View,Integer> layerTypes=new IdentityHashMap<>();
 boolean isPrepared(){return !staged.isEmpty();}
 void prepare(List<View> cells){if(isPrepared())return;cancel();for(View cell:cells){staged.add(cell);cell.setAlpha(0);}}
 void cancel(){
  ArrayList<Animator> active=new ArrayList<>(running.values());running.clear();
  for(Animator animator:active)animator.cancel();
  for(View view:new ArrayList<>(animated))reset(view);
  for(View view:staged)reset(view);
  animated.clear();staged.clear();restoreClipping();
 }
 private void restoreClipping(){for(Map.Entry<ViewGroup,boolean[]> entry:clipping.entrySet()){entry.getKey().setClipChildren(entry.getValue()[0]);entry.getKey().setClipToPadding(entry.getValue()[1]);}clipping.clear();}
 private void allowFlight(View cell,View root){for(ViewParent p=cell.getParent();p instanceof ViewGroup&&p!=root;p=p.getParent()){ViewGroup group=(ViewGroup)p;if(group instanceof WorkspacePager)continue;if(!clipping.containsKey(group))clipping.put(group,new boolean[]{group.getClipChildren(),group.getClipToPadding()});group.setClipChildren(false);group.setClipToPadding(false);}}
 private void reset(View view){
  view.setTranslationX(0);view.setTranslationY(0);view.setScaleX(1);view.setScaleY(1);view.setAlpha(1);
  Integer layer=layerTypes.remove(view);if(layer!=null)view.setLayerType(layer,null);
 }
 private static boolean isDock(View cell){Object address=cell.getTag(R.id.workspace_address);return address instanceof Integer&&(Integer)address<0;}

 /** Starts the entrance. {@code clock} (optional) settles from slightly larger alongside the icons. */
 void startPrepared(View root,View clock){
  if(!isPrepared())return;
  if(!ValueAnimator.areAnimatorsEnabled()||root.getWidth()==0){cancel();return;}
  int[] xy=new int[2];root.getLocationOnScreen(xy);float cx=xy[0]+root.getWidth()/2f,cy=xy[1]+root.getHeight()*.5f;
  ArrayList<View> cells=new ArrayList<>(staged);staged.clear();
  ArrayList<float[]> offsets=new ArrayList<>();float farthest=0;
  for(View cell:cells){
   if(!cell.isAttachedToWindow()||cell.getWidth()==0){offsets.add(null);continue;}
   cell.getLocationOnScreen(xy);float dx=xy[0]+cell.getWidth()/2f-cx,dy=xy[1]+cell.getHeight()/2f-cy;
   offsets.add(new float[]{dx,dy});farthest=Math.max(farthest,(float)Math.hypot(dx,dy));
  }
  for(int n=0;n<cells.size();n++){
   View cell=cells.get(n);float[] offset=offsets.get(n);
   if(offset==null){reset(cell);continue;}
   boolean dock=isDock(cell);float pull=UnlockMotion.pull(dock),scale=UnlockMotion.startScale(dock);
   long delay=UnlockMotion.rippleDelay((float)Math.hypot(offset[0],offset[1]),farthest);
   allowFlight(cell,root);
   cell.setTranslationX(-offset[0]*pull);cell.setTranslationY(-offset[1]*pull);cell.setScaleX(scale);cell.setScaleY(scale);cell.setAlpha(0);
   ObjectAnimator move=ObjectAnimator.ofPropertyValuesHolder(cell,
     PropertyValuesHolder.ofFloat(View.TRANSLATION_X,0),PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,0),
     PropertyValuesHolder.ofFloat(View.SCALE_X,1),PropertyValuesHolder.ofFloat(View.SCALE_Y,1));
   move.setDuration(UnlockMotion.DURATION);move.setInterpolator(UnlockMotion::spring);
   ObjectAnimator fade=ObjectAnimator.ofFloat(cell,View.ALPHA,1);
   fade.setDuration(UnlockMotion.FADE);fade.setInterpolator(UnlockMotion::fade);
   track(cell,move,fade,delay);
  }
  if(clock!=null&&clock.isAttachedToWindow()&&clock.getWidth()>0){
   clock.setScaleX(UnlockMotion.CLOCK_SCALE);clock.setScaleY(UnlockMotion.CLOCK_SCALE);
   ObjectAnimator settle=ObjectAnimator.ofPropertyValuesHolder(clock,PropertyValuesHolder.ofFloat(View.SCALE_X,1),PropertyValuesHolder.ofFloat(View.SCALE_Y,1));
   settle.setDuration(UnlockMotion.totalDuration());settle.setInterpolator(new DecelerateInterpolator(2f));
   track(clock,settle,null,0);
  }
  if(animated.isEmpty())restoreClipping();
 }
 private void track(View view,Animator first,Animator second,long delay){
  AnimatorSet set=new AnimatorSet();if(second==null)set.play(first);else set.playTogether(first,second);set.setStartDelay(delay);
  if(!layerTypes.containsKey(view)){layerTypes.put(view,view.getLayerType());view.setLayerType(View.LAYER_TYPE_HARDWARE,null);}
  animated.add(view);running.put(view,set);
  set.addListener(new AnimatorListenerAdapter(){@Override public void onAnimationEnd(Animator animation){
   if(running.get(view)!=animation)return; // cancel() already restored this view
   running.remove(view);reset(view);animated.remove(view);if(animated.isEmpty())restoreClipping();
  }});
  set.start();
 }
}
