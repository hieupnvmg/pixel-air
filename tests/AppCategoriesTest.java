import dev.pixelair.launcher.AppCategories;
public class AppCategoriesTest {
 static void check(String expected,String id,String label,int category){String actual=AppCategories.infer(id,label,category);if(!expected.equals(actual))throw new AssertionError(id+": "+actual);}
 public static void main(String[] args){check("games","com.vendor.unknown","Example",0);check("productivity","com.vendor.game","Example",7);check("media","com.google.android.youtube","YouTube",-1);check("tools","com.google.android.deskclock","Clock",-1);check("social","com.google.android.dialer","Phone",-1);check("productivity","com.google.android.apps.docs","Drive",-1);check("other","com.vendor.unknown","Example",-1);check("maps","com.vendor.map","Example",6);System.out.println("PASS metadata priority, fallback categories and unknown apps");}
}
