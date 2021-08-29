import org.junit.Test;

public class MyTest {
    @Test
    public void test(){
        ClassLoader classLoader = this.getClass().getClassLoader();
        classLoader.getResourceAsStream("UserMapper.xml");
    }
}
