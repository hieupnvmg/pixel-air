package dev.pixelair.launcher;

/** Tracks an app launched here; ignores dialogs, cold starts and stale return hints. */
public final class HomeReturnGate {
    private String app;
    private boolean departed;
    private long expires;
    public void launch(String id,long now){app=id;departed=false;expires=now+180_000;}
    public void stopped(){if(app!=null)departed=true;}
    public boolean hasDeparted(){return departed;}
    public void clear(){app=null;departed=false;}
    public String take(long now,boolean ready){
        if(app!=null&&now>expires)clear();
        if(!ready||!departed)return null;
        String result=app;clear();return result;
    }
}
