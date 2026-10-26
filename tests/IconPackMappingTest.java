import dev.pixelair.launcher.IconPackMapping;
public class IconPackMappingTest {
 public static void main(String[] args) {
  assert "com.demo/com.demo.Main".equals(IconPackMapping.normalize("ComponentInfo{com.demo/.Main}"));
  assert "com.demo/com.other.Main".equals(IconPackMapping.normalize("com.demo/com.other.Main"));
  assert "com.demo/com.demo.Main".equals(IconPackMapping.normalize(" com.demo/.Main "));
  assert IconPackMapping.normalize(null)==null;
  assert IconPackMapping.normalize("broken")==null;
  assert IconPackMapping.normalize("/MissingPackage")==null;
  assert IconPackMapping.normalize("com.demo/")==null;
  assert IconPackMapping.normalize("com.demo/Bad Class")==null;
  System.out.println("PASS canonical components, relative names, invalid and missing mappings");
 }
}
