package linkedmapsimple.interfaz;
import linkedmapsimple.test.*;
public class Cuerpo {
    public static void main(String[] args) {
        Test t1 = null;
        t1 = new ConstructorTest();
        t1.test();
        t1 = new UpTest();
        t1.test();
        t1 = new GetTest();
        t1.test();
        t1 = new ContainsKeyTest();
        t1.test();
        t1 = new ContainsValueTest();
        t1.test();
        t1 = new DownTest();
        t1.test();
        t1 = new PointersTest();
        t1.test();
    }    
}