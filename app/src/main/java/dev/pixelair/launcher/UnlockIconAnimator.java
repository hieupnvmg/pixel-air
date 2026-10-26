package dev.pixelair.launcher;
import android.animation.ValueAnimator;
import android.view.*;
import android.view.animation.PathInterpolator;
import java.util.*;

/** Stage while covered by keyguard, then animate the very first visible Home frame. */
final class UnlockIconAnimator {
 private final IdentityHashMap<ViewGroup,boolean[]> clipping=new IdentityHashMap<>();
 private final ArrayList<View> staged=new ArrayList<>(),animated=new ArrayList<>();
 boolean isPrepared(){return !staged.isEmpty();}
 void prepare(List<View> cells){if(isPrepared())return;cancel();for(View cell:cells){staged.add(cell);cell.setAlpha(0);}}
 void cancel(){for(View view:new ArrayList<>(animated)){view.animate().cancel();reset(view);}for(View view:staged)reset(view);animated.clear();staged.clear();restoreClipping();}
 private void restoreClipping(){for(Map.Entry<ViewGroup,boolean[]> entry:clipping.entrySet()){entry.getKey().setClipChildren(entry.getValue()[0]);entry.getKey().setClipToPadding(entry.getValue()[1]);}clipping.clear();}
 private void allowFlight(View cell,View root){for(ViewParent p=cell.getParent();p instanceof ViewGroup&&p!=root;p=p.getParent()){ViewGroup group=(ViewGroup)p;if(group instanceof WorkspacePager)continue;if(!clipping.containsKey(group))clipping.put(group,new boolean[]{group.getClipChildren(),group.getClipToPadding()});group.setClipChildren(false);group.setClipToPadding(false);}}
 private void reset(View view){view.setTranslationX(0);view.setTranslationY(0);view.setScaleX(1);view.setScaleY(1);view.setAlpha(1);}
 void startPrepared(View root){if(!isPrepared())return;if(!ValueAnimator.areAnimatorsEnabled()||root.getWidth()==0){cancel();return;}int[] xy=new int[2];root.getLocationOnScreen(xy);float cx=xy[0]+root.getWidth()/2f,cy=xy[1]+root.getHeight()*.5f;
  ArrayList<View> cells=new ArrayList<>(staged);staged.clear();
  for(View cell:cells){if(!cell.isAttachedToWindow()||cell.getWidth()==0){reset(cell);continue;}cell.getLocationOnScreen(xy);float dx=xy[0]+cell.getWidth()/2f-cx,dy=xy[1]+cell.getHeight()/2f-cy;allowFlight(cell,root);cell.setTranslationX(-dx*.26f);cell.setTranslationY(-dy*.26f);cell.setScaleX(.72f);cell.setScaleY(.72f);cell.setAlpha(0);animated.add(cell);long delay=0;cell.animate().translationX(0).translationY(0).scaleX(1).scaleY(1).alpha(1).setStartDelay(delay).setDuration(500).setInterpolator(new PathInterpolator(.18f,.78f,.2f,1f)).withLayer().withEndAction(()->{reset(cell);animated.remove(cell);if(animated.isEmpty())restoreClipping();}).start();}
  if(animated.isEmpty())restoreClipping();
 }
}
