package dev.pixelair.launcher;
/** One entrance per real screen-off cycle; never re-hide a Home frame already presented. */
public final class UnlockGate {
 private boolean armed,pending,completed; private long expires;
 public void screenOff(boolean homeWasVisible,boolean screenStillOff){if(!screenStillOff)return;armed=homeWasVisible;pending=false;completed=!homeWasVisible;}
 public void homeBehindKeyguard(){if(!completed&&!pending)armed=true;}
 public void leaveHome(){armed=false;pending=false;completed=true;}
 public void homeFrameShown(){leaveHome();}
 public void preview(long now){armed=false;pending=true;completed=false;expires=now+1400;}
 public boolean needsFrameDecision(){return !completed;}
 public boolean isArmed(){return armed;}
 public void unlocked(long now){if(completed||!armed)return;armed=false;pending=true;expires=now+1400;}
 public boolean hasPending(long now){if(pending&&now>expires){pending=false;completed=true;}return pending;}
 public boolean take(long now,boolean ready){if(!hasPending(now)||!ready)return false;pending=false;armed=false;completed=true;return true;}
}
