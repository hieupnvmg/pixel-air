package dev.pixelair.launcher;
/** One entrance per lock/unlock cycle, never delayed until a later return from another app. */
public final class UnlockGate {
 private boolean armed,pending; private long expires;
 public void screenOff(boolean homeWasVisible){armed=homeWasVisible;pending=false;}
 public void homeBehindKeyguard(){armed=true;}
 public void leaveHome(){armed=false;pending=false;}
 public boolean isArmed(){return armed;}
 public void unlocked(long now){if(!armed)return;armed=false;pending=true;expires=now+1400;}
 public boolean hasPending(long now){if(pending&&now>expires)pending=false;return pending;}
 public boolean take(long now,boolean ready){if(!hasPending(now)||!ready)return false;pending=false;return true;}
}
