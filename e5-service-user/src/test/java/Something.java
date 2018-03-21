/**
 * Created by HuaYu on 2018/3/14.
 */
public class Something {
    public static void main(String[]args){
        Other o=new Other();
        new Something().addOne(o);
    }
    public void addOne(final Other o){
        o.i++;
    }
}
class Other{
    public int i;
}
