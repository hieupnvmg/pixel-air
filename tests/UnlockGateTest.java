import dev.pixelair.launcher.UnlockGate;
public class UnlockGateTest {
 static void check(boolean b){if(!b)throw new AssertionError();}
 public static void main(String[] args){UnlockGate gate=new UnlockGate();check(!gate.take(0,true));gate.unlocked(0);check(!gate.take(1,true));gate.screenOff(true);gate.unlocked(100);check(gate.hasPending(105));check(!gate.take(110,false));check(gate.hasPending(115));check(gate.take(250,true));check(!gate.take(260,true));gate.screenOff(false);gate.unlocked(500);check(!gate.take(600,true));gate.homeBehindKeyguard();gate.unlocked(1000);check(!gate.take(2501,true));gate.screenOff(true);gate.unlocked(3000);gate.screenOff(true);check(!gate.take(3100,true));gate.unlocked(3500);check(gate.take(3501,true));gate.screenOff(true);gate.leaveHome();gate.unlocked(4000);check(!gate.take(4001,true));System.out.println("PASS one animation per unlock, focus race, expiry, background app and repeated screen-off");}
}
