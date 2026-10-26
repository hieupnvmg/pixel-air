import dev.pixelair.launcher.DrawerPages;
import dev.pixelair.launcher.DrawerSections;
import java.util.*;

public class DrawerPagesTest {
    static void check(boolean value){if(!value)throw new AssertionError();}
    record App(String id,String category){}
    public static void main(String[] args){
        List<App> input=new ArrayList<>();
        for(int n=0;n<8;n++)input.add(new App("frequent"+n,"tools"));
        for(int n=0;n<37;n++)input.add(new App("game"+n,"games"));
        input.add(new App("media","media"));
        var sections=DrawerSections.build(input,8,true,App::id,App::category);
        var pages=DrawerPages.build(sections,16);
        check(pages.size()==5 && pages.get(0).apps.size()==8);
        check(pages.get(1).parts==3 && pages.get(3).part==2 && pages.get(3).apps.size()==5);
        check(pages.get(1).total==37 && DrawerPages.firstPage(pages,"games")==1);
        check(DrawerPages.firstPage(pages,"media")==4 && DrawerPages.firstPage(pages,"missing")==-1);
        List<String> actual=new ArrayList<>();for(var p:pages)for(var app:p.apps)actual.add(app.id());
        check(actual.size()==input.size() && new HashSet<>(actual).size()==input.size());
        check(DrawerPages.build(sections,4).size()==13); // smaller viewport keeps every app
        check(DrawerPages.build(List.of(),16).isEmpty());
        var flat=DrawerSections.build(input,0,false,App::id,App::category);
        check(DrawerPages.build(flat,16).size()==3);
        boolean rejected=false;try{DrawerPages.build(sections,0);}catch(IllegalArgumentException e){rejected=true;}check(rejected);
        System.out.println("PASS horizontal pagination, category boundaries, overflow, full coverage, viewport resize and flat sorting");
    }
}
