package linkedmapsimple.test;
import linkedmapsimple.structs.MyLinkedMap;
public class PointersTest extends Test
{
    MyLinkedMap<Integer,String>map;
    public PointersTest() {
        map = new MyLinkedMap();
    }
    //<editor-fold desc="relleno de datos">
    private void cargando(){
        map.clear();
        map.put(1, "Deborah");
        map.put(2, "Tommy");
        map.put(3, "Franco");
        map.put(4, "Manuela");
        map.put(5, "Miguel");
        map.put(6, "Denisse");
    }
    //</editor-fold>
    //<editor-fold desc="pruebas">
    private void probando_punteros_nulos() throws Exception {
        map.clear();
        comprobar_que(map.firstKey() == null && map.lastKey() == null);
        comprobar_que(map.firstEntry() == null && map.lastEntry() == null);
    }
    private void probando_punteros_normales() throws Exception {
        this.cargando();
        comprobar_que(map.firstKey() == 1 && map.lastKey() == 6);
        comprobar_que(map.firstEntry() != null && map.lastEntry() != null);
    }
    private void probando_punteros_borrados_facil() throws Exception {
        this.cargando();
        comprobar_que(map.firstKey() == 1 && map.lastKey() == 6);
        comprobar_que(map.firstEntry() != null && map.lastEntry() != null);
        map.remove(1);
        map.remove(6);
        comprobar_que(map.firstKey() != 1 && map.lastKey() != 6);
        comprobar_que(map.firstEntry() != null && map.lastEntry() != null);
    }
    private void probando_punteros_borrados_pares() throws Exception {
        this.cargando();
        comprobar_que(map.firstKey() == 1 && map.lastKey() == 6);
        comprobar_que(map.firstEntry() != null && map.lastEntry() != null);
        map.remove(2);
        map.remove(4);
        map.remove(6);
        comprobar_que(map.firstKey() == 1 && map.lastKey() == 5);
        comprobar_que(map.firstEntry() != null && map.lastEntry() != null);
    }
    private void probando_punteros_borrados_impares() throws Exception {
        this.cargando();
        comprobar_que(map.firstKey() == 1 && map.lastKey() == 6);
        comprobar_que(map.firstEntry() != null && map.lastEntry() != null);
        map.remove(1);
        map.remove(3);
        map.remove(5);
        comprobar_que(map.firstKey() != 1 && map.lastKey() != 5);
        comprobar_que(map.firstEntry() != null && map.lastEntry() != null);
    }
    //</editor-fold>
    @Override
    public void test() {
        try {
            probando_punteros_nulos();
            probando_punteros_normales();
            probando_punteros_borrados_facil();
            probando_punteros_borrados_pares();
            probando_punteros_borrados_impares();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }    
}