import dev.pixelair.launcher.UnlockGate;
public class UnlockGateTest {
 static void check(boolean b){if(!b)throw new AssertionError();}
 public static void main(String[] args){
  UnlockGate gate=new UnlockGate();check(!gate.take(0,true));gate.homeFrameShown();check(!gate.needsFrameDecision());gate.screenOff(true,false);check(!gate.needsFrameDecision());
  gate.homeBehindKeyguard();gate.unlocked(10);check(!gate.hasPending(20)); // no late animation after visible Home
  gate.screenOff(true,true);gate.homeBehindKeyguard();gate.unlocked(100);check(gate.hasPending(105));
  check(!gate.take(110,false));check(gate.hasPending(115));check(gate.take(250,true));check(!gate.take(260,true));
  gate.homeBehindKeyguard();gate.unlocked(300);check(!gate.take(310,true)); // duplicate lifecycle / user-present cannot replay
  gate.screenOff(false,true);gate.homeBehindKeyguard();gate.unlocked(500);check(!gate.take(600,true));
  gate.screenOff(true,true);gate.unlocked(1000);check(!gate.take(2501,true));gate.homeBehindKeyguard();gate.unlocked(2600);check(!gate.take(2601,true));
  gate.screenOff(true,true);gate.unlocked(3000);gate.screenOff(true,true);check(!gate.take(3100,true));gate.unlocked(3500);check(gate.take(3501,true));
  gate.screenOff(true,true);gate.leaveHome();gate.unlocked(4000);check(!gate.take(4001,true));
  gate.preview(5000);check(gate.take(5001,true));gate.homeBehindKeyguard();gate.unlocked(5100);check(!gate.take(5101,true));
  gate.screenOff(true,true);gate.unlocked(6000);gate.homeFrameShown();check(!gate.take(6001,true));
  System.out.println("PASS no late replay after visible Home, duplicate events, readiness, expiry, new lock cycles and explicit preview");
 }
}
