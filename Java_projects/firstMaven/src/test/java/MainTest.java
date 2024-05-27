import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class MainTest {
    @Test
    public void test(){
        System.out.println("测试");
        Assertions.assertArrayEquals(new int[]{1,2,3},new int[]{1,2,3});
    }
}
