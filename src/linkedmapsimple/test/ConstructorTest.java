package linkedmapsimple.test;

import linkedmapsimple.structs.MyLinkedMap;

public class ConstructorTest extends Test
{
    private void creando_map_vacio() throws Exception{
        MyLinkedMap<Integer,String>m= new MyLinkedMap();
        this.comprobar_que(m.isEmpty());
    }
    private void creando_map_normal() throws Exception {
        MyLinkedMap<Integer,String>m1= new MyLinkedMap();
        m1.put(1, "Deborah");
        m1.put(2, "Tommy");
        m1.put(3, "Franco");
        m1.put(4, "Manuela");
        this.comprobar_que(m1.size() == 4);
    }
    private void creando_map_con_capacidad_de_dos_elementos() throws Exception{
        MyLinkedMap<Integer,String>m2= new MyLinkedMap(2);
        m2.put(1, "Paula");
        m2.put(2, "Pedro");
        m2.put(3, "Fabio");
        m2.put(4, "John");
        m2.put(5, "Manuela");
        m2.put(6, "Andrea");
        m2.put(7, "Luisa");
        this.comprobar_que(m2.size() == 7);
    }
    private void pasar_datos_de_hashmap_a_nuestro_map() throws Exception{
        java.util.HashMap<Integer, String> m = new java.util.HashMap();
        m.put(1, "Agustin");
        m.put(2, "Amanda");
        m.put(3, "Olivia");
        m.put(4, "Maite");
        /*---------------------------------------*/
        MyLinkedMap<Integer,String> m3 = new MyLinkedMap();
        m3.putAll(m);
        /*---------------------------------------*/
        this.comprobar_que(m3.size() == 4);
    }
    private void creando_map_con_capacidad_de_un_elemento() throws Exception{
        MyLinkedMap<Integer,String> m4 = new MyLinkedMap(1);
        m4.put(1, "Luis");
        m4.put(2, "Edelma");
        m4.put(3, "Arnoldno");
        m4.put(4, "Edinson");
        this.comprobar_que(m4.size() == 4);
    }
    @Override
    public void test() {
        try {
            creando_map_vacio();
            creando_map_normal();
            creando_map_con_capacidad_de_dos_elementos();
            pasar_datos_de_hashmap_a_nuestro_map();
            creando_map_con_capacidad_de_un_elemento(); 
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}