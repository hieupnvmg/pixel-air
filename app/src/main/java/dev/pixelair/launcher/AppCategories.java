package dev.pixelair.launcher;
import java.util.Locale;
/** Provider metadata first, conservative name fallback, user overrides live in preferences. */
public final class AppCategories {
 public static final String[] KEYS={"games","tools","productivity","media","social","maps","news","shopping","other"};
 public static String infer(String id,String label,int category){
  switch(category){case 0:return "games";case 1:case 2:case 3:return "media";case 4:return "social";case 5:return "news";case 6:return "maps";case 7:return "productivity";case 8:return "tools";default:break;}
  String s=(id+" "+label).toLowerCase(Locale.ROOT);
  if(matches(s,"game","minecraft","pubg","genshin","roblox","asphalt","candycrush"))return "games";
  if(matches(s,"camera","photo","gallery","youtube","music","spotify","netflix","video","vlc"))return "media";
  if(matches(s,"whatsapp","messenger","telegram","instagram","facebook","zalo","contacts","dialer","messaging","phone","messages"))return "social";
  if(matches(s,"maps","navigation","uber","grab","waze"))return "maps";
  if(matches(s,"gmail","calendar","docs","sheets","slides","office","drive","notes","notion","outlook","classroom"))return "productivity";
  if(matches(s,"news","books","kindle","reader"))return "news";
  if(matches(s,"shopping","shopee","lazada","amazon","bank","wallet","paypal"))return "shopping";
  if(matches(s,"settings","calculator","clock","deskclock","files","filemanager","chrome","browser","vending","recorder","compass","weather"))return "tools";
  return "other";
 }
 private static boolean matches(String s,String... words){for(String w:words)if(s.contains(w))return true;return false;}
}
