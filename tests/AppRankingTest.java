import dev.pixelair.launcher.AppRanking;
import java.text.Collator;
import java.util.Locale;
public class AppRankingTest {
 public static void main(String[] args) {
  Collator c=Collator.getInstance(Locale.ENGLISH);
  assert AppRanking.compare("Calendar","a",0,"Chrome","b",20,false,c)<0;
  assert AppRanking.compare("Calendar","a",0,"Chrome","b",20,true,c)>0;
  assert AppRanking.compare("Clock","a",3,"Chrome","b",2,true,c)<0;
  assert AppRanking.compare("Calendar","a",2,"Chrome","b",2,true,c)<0;
  assert AppRanking.compare("Same","a",2,"Same","b",2,true,c)<0;
  assert AppRanking.compare("A","a",Long.MAX_VALUE,"B","b",0,true,c)<0;
  System.out.println("PASS alphabetical sort, usage sort, stable ties and large counts");
 }
}
