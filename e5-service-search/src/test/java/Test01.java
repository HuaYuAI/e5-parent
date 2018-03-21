import org.junit.Test;

/**
 * Created by HuaYu on 2018/3/6.
 */
public class Test01 {
    @Test
    public void test1(){
        int a=3;

        a+=a-=a*a;
        System.out.println(a);

        int i=1,j=10;
        do{
            if (i++ > --j) {
                continue;
            }
        }while(i<5);
        System.out.print(i+"\t"+j);
    }
}
