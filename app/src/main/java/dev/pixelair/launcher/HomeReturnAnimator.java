package dev.pixelair.launcher;

import android.animation.*;
import android.view.View;
import android.view.animation.PathInterpolator;

/** Launcher-only settling feedback. Never transforms or overlays another app's window. */
final class HomeReturnAnimator {
    private ValueAnimator animator;
    private View home,icon;
    void cancel(){
        if(animator!=null){animator.cancel();animator=null;}
        if(home!=null){home.setScaleX(1);home.setScaleY(1);home.setAlpha(1);home=null;}
        if(icon!=null){icon.setScaleX(1);icon.setScaleY(1);icon=null;}
    }
    void start(View workspace,View target){
        cancel();if(!ValueAnimator.areAnimatorsEnabled())return;
        home=workspace;icon=target;
        ValueAnimator motion=ValueAnimator.ofFloat(0,1);animator=motion;
        motion.setDuration(260);motion.setInterpolator(new PathInterpolator(.2f,0f,0f,1f));
        motion.addUpdateListener(a->{float p=(float)a.getAnimatedValue();
            float scale=.985f+.015f*p;workspace.setScaleX(scale);workspace.setScaleY(scale);workspace.setAlpha(.96f+.04f*p);
            if(target!=null){float pulse=1-.09f*(1-p)+.035f*(float)Math.sin(Math.PI*p);target.setScaleX(pulse);target.setScaleY(pulse);}
        });
        workspace.setScaleX(.985f);workspace.setScaleY(.985f);workspace.setAlpha(.96f);
        if(target!=null){target.setScaleX(.91f);target.setScaleY(.91f);}
        motion.addListener(new AnimatorListenerAdapter(){@Override public void onAnimationEnd(Animator a){if(animator==motion){animator=null;cancel();}}});
        motion.start();
    }
}
