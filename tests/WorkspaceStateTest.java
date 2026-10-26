import dev.pixelair.launcher.WorkspaceState;
public class WorkspaceStateTest {
 public static void main(String[] args) {
  WorkspaceState s=new WorkspaceState();
  for(int n=0;n<8;n++)s.put(n,"app"+n);
  s.put(-1,"phone");
  int second=s.addApp("extra");assert second==1;assert s.get(20).equals("extra");assert s.get(-1).equals("phone");
  s.swap(0,21);assert s.get(0).isEmpty();assert s.get(21).equals("app0");
  s.merge(21,20);assert s.get(20).equals("extra|app0");assert s.get(21).isEmpty();
  s.currentPage=1;assert !s.removeEmptyCurrentPage();s.put(20,"");assert s.removeEmptyCurrentPage();assert s.currentPage==0;
  s.currentPage=s.addPage();for(int n=20;n<40;n++)s.put(n,"x"+n);assert s.addApp("overflow")==0;
  s.put(0,"restored");assert s.addApp("next")==2;assert s.get(40).equals("next");assert s.get(-1).equals("phone");
  System.out.println("PASS page capacity, auto pages, dock stability, cross-page move, folders, page deletion");
 }
}
