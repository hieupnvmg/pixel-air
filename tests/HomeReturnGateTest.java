import dev.pixelair.launcher.HomeReturnGate;
public class HomeReturnGateTest {
 static void check(boolean ok){if(!ok)throw new AssertionError();}
 public static void main(String[] args){
  HomeReturnGate gate=new HomeReturnGate();check(gate.take(0,true)==null);
  gate.launch("clock",100);check(gate.take(200,true)==null); // no stop: dialog or launch failure
  gate.stopped();check(gate.take(300,false)==null);check("clock".equals(gate.take(400,true)));check(gate.take(500,true)==null);
  gate.launch("camera",1000);gate.stopped();gate.clear();check(gate.take(1200,true)==null); // lock / cancellation
  gate.launch("old",2000);gate.stopped();check(gate.take(182001,true)==null);
  gate.launch("old",200000);gate.stopped();gate.launch("new",200100);check(gate.take(200200,true)==null);gate.stopped();check("new".equals(gate.take(200300,true)));
  System.out.println("PASS return feedback: actual departure, readiness, once-only, expiry, lock cancellation and replacement");
 }
}
