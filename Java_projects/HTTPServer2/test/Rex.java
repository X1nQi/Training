import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rex {
    public static void main(String[] args) {
        String input = "------WebKitFormBoundaryxs22mQGA5AWAfp9s\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"linkList\"\n" +
                "Content-Type: application/octet-stream\n" +
                "\n" +
                "https://222.173.132.130:4433\n" +
                "\n" +
                "------WebKitFormBoundaryxs22mQGA5AWAfp9s--";

        Pattern filename = Pattern.compile("filename=\"(.+)\"");
        Pattern filetype = Pattern.compile("Content-Type: (.+)");


        Matcher matcherName = filename.matcher(input);
        Matcher matcherType = filetype.matcher(input);



        if(matcherName.find()){
            System.out.println("已匹配到文件名");
            System.out.println("文件名：" + matcherName.group(1));
        }else {
            System.out.println("未匹配到");
        }

        if(matcherType.find()){
            System.out.println("已匹配到类型名");
            System.out.println("类型：" + matcherType.group(1));
        }else {
            System.out.println("未匹配到类型名");
        }

        // 注意：如果有多行可能包含Content-Type:，则需要使用while循环来查找所有匹配项
    }
}