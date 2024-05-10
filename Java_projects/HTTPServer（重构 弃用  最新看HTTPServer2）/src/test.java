import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        String foo = null;
        String qpa = "abc";
        HashMap<String,String> testMap = new HashMap<>();
        testMap.put("a","1");
        testMap.put("b","2");
        testMap.put("c","3");

        System.out.println(testMap.keySet());
        for(String a : testMap.keySet()){
            System.out.println(a);
        }
        System.out.printf("%s",qpa);
    }
}
