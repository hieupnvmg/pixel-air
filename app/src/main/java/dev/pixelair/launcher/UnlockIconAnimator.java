package dev.pixelair.launcher;
import android.animation.ValueAnimator;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.*;
import android.view.animation.PathInterpolator;
import java.util.*;

/** Stage while covered by keyguard, then animate the very first visible Home frame. */
final class UnlockIconAnimator {
 private int preparedStyle;
 private final IdentityHashMap<View,Integer> layers=new IdentityHashMap<>();
 private final ArrayList<Animator> fades=new ArrayList<>();
 private final IdentityHashMap<ViewGroup,boolean[]> clipping=new IdentityHashMap<>();
 private final ArrayList<View> staged=new ArrayList<>(),animated=new ArrayList<>();
 boolean isPrepared(){return !staged.isEmpty();}
 void prepare(View root,List<View> cells,int style){
  if(isPrepared()&&preparedStyle==style)return;cancel();preparedStyle=style;
  Collection<View> targets=cells;
  if(style==1){LinkedHashSet<View> groups=new LinkedHashSet<>();for(View cell:cells){View target=cell;
   for(ViewParent p=cell.getParent();p instanceof View;p=p.getParent()){View parent=(View)p;if("unlock-grid".equals(parent.getTag())||"unlock-dock".equals(parent.getTag())){target=parent;break;}}
   groups.add(target);
  }targets=groups;}
  for(View target:targets){staged.add(target);if(style==1){allowFlight(target,root);warmLayer(target);}target.setAlpha(0);}
 }
 private void warmLayer(View view){
  if(layers.containsKey(view)||!view.isAttachedToWindow()||!view.isHardwareAccelerated()||view.getWidth()==0||view.getHeight()==0||view.getLayerType()==View.LAYER_TYPE_SOFTWARE)return;
  layers.put(view,view.getLayerType());view.setLayerType(View.LAYER_TYPE_HARDWARE,null);view.buildLayer();
 }
 void warmPrepared(){
  if(preparedStyle!=1)return;
  for(View target:staged){if(!target.isAttachedToWindow()||!target.isHardwareAccelerated()||target.getWidth()==0||target.getHeight()==0)continue;if(layers.containsKey(target))target.buildLayer();else warmLayer(target);}
 }
 private void finishLayers(){for(Map.Entry<View,Integer> entry:layers.entrySet())entry.getKey().setLayerType(entry.getValue(),null);layers.clear();fades.clear();}

 void cancel(){for(Animator fade:new ArrayList<>(fades))fade.cancel();fades.clear();for(View view:new ArrayList<>(animated)){view.animate().cancel();reset(view);}for(View view:staged)reset(view);animated.clear();staged.clear();restoreClipping();finishLayers();}
 private void restoreClipping(){for(Map.Entry<ViewGroup,boolean[]> entry:clipping.entrySet()){entry.getKey().setClipChildren(entry.getValue()[0]);entry.getKey().setClipToPadding(entry.getValue()[1]);}clipping.clear();}
 private void allowFlight(View cell,View root){for(ViewParent p=cell.getParent();p instanceof ViewGroup&&p!=root;p=p.getParent()){ViewGroup group=(ViewGroup)p;if(group instanceof WorkspacePager)continue;if(!clipping.containsKey(group))clipping.put(group,new boolean[]{group.getClipChildren(),group.getClipToPadding()});group.setClipChildren(false);group.setClipToPadding(false);}}
 private void reset(View view){view.setTranslationX(0);view.setTranslationY(0);view.setScaleX(1);view.setScaleY(1);view.setAlpha(1);}
 void startPrepared(View root,int style){if(!isPrepared())return;if(!ValueAnimator.areAnimatorsEnabled()||root.getWidth()==0){cancel();return;}int[] xy=new int[2];root.getLocationOnScreen(xy);float cx=xy[0]+root.getWidth()/2f,cy=xy[1]+root.getHeight()*.5f;
  ArrayList<View> cells=new ArrayList<>(staged);staged.clear();if(style==1){startIphone(root,cells);return;}
  for(View cell:cells){if(!cell.isAttachedToWindow()||cell.getWidth()==0){reset(cell);continue;}cell.getLocationOnScreen(xy);float dx=xy[0]+cell.getWidth()/2f-cx,dy=xy[1]+cell.getHeight()/2f-cy;allowFlight(cell,root);cell.setTranslationX(-dx*.26f);cell.setTranslationY(-dy*.26f);cell.setScaleX(.72f);cell.setScaleY(.72f);cell.setAlpha(0);animated.add(cell);long delay=0;cell.animate().translationX(0).translationY(0).scaleX(1).scaleY(1).alpha(1).setStartDelay(delay).setDuration(500).setInterpolator(new PathInterpolator(.18f,.78f,.2f,1f)).withLayer().withEndAction(()->{reset(cell);animated.remove(cell);if(animated.isEmpty())restoreClipping();}).start();}
  if(animated.isEmpty())restoreClipping();
 }
 private void startIphone(View root,ArrayList<View> targets){
  final long duration=600;
  float density=root.getResources().getDisplayMetrics().density;
  // Animate two cached containers instead of applying six properties to every icon in Java.
  for(View target:targets){
   if(!target.isAttachedToWindow()||target.getWidth()==0){reset(target);continue;}
   boolean dock="unlock-dock".equals(target.getTag());allowFlight(target,root);warmLayer(target);animated.add(target);
   float scale=dock?.62f:2.1f;
   target.setScaleX(scale);target.setScaleY(scale);target.setTranslationY(dock?48*density:0);target.setAlpha(0);
   ObjectAnimator fade=ObjectAnimator.ofFloat(target,View.ALPHA,0,1);fade.setDuration(90);fade.setInterpolator(new android.view.animation.LinearInterpolator());fades.add(fade);fade.start();
   target.animate().setUpdateListener(null).setListener(null).scaleX(1).scaleY(1).translationX(0).translationY(0).setStartDelay(0).setDuration(duration).setInterpolator(new PathInterpolator(.18f,.78f,.2f,1f)).withEndAction(()->{
    reset(target);animated.remove(target);if(animated.isEmpty()){restoreClipping();finishLayers();}
   }).start();
  }
  if(animated.isEmpty()){restoreClipping();finishLayers();}
 }
}
