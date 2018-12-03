package linkedmapsimple.structs;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/**
 * @author detectivejd
 * @param <K>
 * @param <V> 
 */
public class MyMap<K,V> implements Map<K,V>
{
    transient Entry<K,V>[] table;
    private static final float LOAD_FACTOR = 0.75f;
    private static int threshold;
    transient int size;
    /**
     * Construye un nuevo HashMap con una cantidad a almacenar por 
     * defecto
     */
    public MyMap() {
        this(4);
    }
    /**
     * Construye un nuevo HashMap según la cantidad de elementos que
     * deseamos almacenar
     * 
     * @param xcap -> capacidad de elementos a almacenar 
     */
    public MyMap(int xcap) {
        if(xcap <= 0){
            throw new IllegalArgumentException("Capacidad no permitida: " + xcap);
        }
        threshold = (int) (xcap * LOAD_FACTOR);
        table = new Entry[xcap];
        size = 0;
        init();
    }
    /**
     * Construye un nuevo HashMap que utilizamos para almacenar toda una 
     * estructura de datos tipo map a nuestra estructura de datos
     * 
     * @param m 
     */
    public MyMap(Map<? extends K, ? extends V> m) {
        this(m.size());
        this.putAll(m);
    }
    /**
     * Inicializa la cabeza la estructura de datos a un estado por defecto
     */
    void init() { }
    /**
     * Devuelve la cantidad de elementos almacenados en nuestra
     * estructura de datos
     * 
     * @return int -> entero 
     */
    @Override
    public int size() {
        return size;
    }
    /**
     * Verifica si nuestra estructura de datos esta vacía o no
     * 
     * @return boolean -> verdadero o falso 
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    /**
     * Limpia todas las entradas almacenadas de nuestro HashMap
     */
    @Override
    public void clear() {
        for(int i = 0; i < table.length; i++){
            table[i] = null;
        }
        size = 0;
    }
    /*----------------------------------------------------------*/
    /**
     * Verifica si existe una entrada por medio de un valor pasado 
     * por parámetro 
     * 
     * @param value -> valor
     * @return boolean -> verdadero o falso 
     */
    @Override
    public boolean containsValue(Object value) {
        for (Entry<K,V> entry : table) {
            for (Entry e = entry; e != null; e = e.next) {
                if (value != null && value.equals(e.getValue())){
                    return true;
                }
            }
        }
        return false;
    }
    /*----------------------------------------------------------*/
    /**
     * Encuentra un valor mediante una clave pasada por parámetro
     * 
     * @param key -> clave
     * @return V -> valor 
     */
    @Override
    public V get(Object key) {
        return (this.getEntry(key) == null) ? null : this.getEntry(key).getValue();
    }
    /**
     * Verifica si existe una entrada por medio de una clave 
     * pasada por parámetro
     * 
     * @param key -> clave
     * @return boolean -> verdadero o falso
     */
    @Override
    public boolean containsKey(Object key) {
        return this.getEntry(key) != null;
    }
    /**
     * Obtenemos una entrada mediante una clave pasada por parámetro
     * 
     * @param key -> clave
     * @return Entry<K, V> -> entrada clave/valor
     */
    private Entry<K, V> getEntry(Object key) {
        int hash = hash(key,table.length);
        for (Entry e = table[hash]; e != null; e = e.next) {
            if(e.getKey().equals(key)){
                return e;
            }
        }
        return null;
    }
    /*----------------------------------------------------------*/
    /**
     * Almacenamos una nueva entrada clave/valor a nuestro Map, si 
     * nuestro map contiene una entrada con la key existente, el 
     * valor viejo será reemplazado
     * 
     * @param key -> clave
     * @param value -> valor
     * @return V -> valor
     */
    @Override
    public V put(K key, V value) {
        if(key != null){
            int hash = hash(key,table.length);   
            for(Entry<K,V> e = table[hash]; e != null ; e = e.next){
                if(e.getKey().equals(key)){
                    V oldValue = e.getValue();
                    e.setValue(value);
                    e.afterNodeAccess(this);
                    return oldValue;
                } else if(e.next == null) {
                    return e.chain(key, value);
                }                
            }
            addEntry(key, value);
            return value;                     
        } else {
            return null;
        }
    }
    /**
     * Almacenamos la clave/valor a nuestra estructura de datos
     * 
     * @param key
     * @param value
     * @param hash 
     */
    private void addEntry(K key, V value){
        if(size >= threshold){
            Entry newTable[] = new Entry[table.length * 2];
            transfer(newTable);
            table = newTable;
            threshold = (int)(newTable.length * LOAD_FACTOR);
        }
        createEntry(key, value);
    }
    /**
     * Transfiere todo el contenido de los datos al array temporal
     * 
     * @param newTable 
     */
    private void transfer(Entry newTable[]){
        for (Entry<K, V> entry : table) {
            if (entry != null) {
                Entry<K,V> current = entry;
                while(current != null){
                    Entry<K,V> et = current;
                    current = current.next;
                    int index = hash(et.key,newTable.length);
                    et.next = newTable[index];
                    newTable[index] = et;
                }
            }
        }
    }
    /**
     * Creamos una entrada nueva
     * 
     * @param key
     * @param value 
     * @param hash 
     */
    protected void createEntry(K key, V value){
        int hash = hash(key,table.length);
        table[hash] = new Entry(key, value);
        size++;
    }
    /**
     * Transferiere toda una estructura de tipo Map a 
     * nuestro HashMap "casero"
     * 
     * @param m -> mapa de clave/valor
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if(m.size() > 0){
            m.entrySet().forEach((e) -> {
                this.put(e.getKey(), e.getValue());
            });
        }
    }
    /*----------------------------------------------------------*/
    /**
     * Elimina una entrada existente por medio de una clave 
     * pasada por parámetro
     * 
     * @param key
     * @return V -> valor 
     */
    @Override
    public V remove(Object key) {
        int hash = hash(key,table.length);
        Entry last = null;
        for (Entry e = table[hash]; e != null; e = e.next) {
            if (key != null && key.equals(e.getKey())) {
                if (last == null) {
                    table[hash] = e.next;
                } else {
                    last.next = e.next;
                }
                e.afterNodeRemoval(this);
                size--;
                return (V) e.value;
            }
            last = e;
        }
        return null;
    }
    /*----------------------------------------------------------*/
    /**
     * Genera un índice de una entrada existente, en base a la 
     * clave y al largo (array) pasados por parámetro 
     * 
     * @param key -> clave
     * @param length
     * @return int -> índice para obtener una entrada existente
     */
    protected int hash(Object key, int length) {
        return (key == null) ? 0 : (key.hashCode() & 0x7fffffff) % length;
    }
    /*----------------------------------------------------------*/
    /**
     * Devuelve un conjunto de todas las claves almacenadas en 
     * nuestra estructura de datos
     * 
     * @return Set<K> -> tupla de claves
     */
    @Override
    public Set<K> keySet() {
        return new KeySet();
    }   
    /*-----------------------------------------------------------*/
    /**
     * KeySet es una clase interna que utilizamos para las iteraciones
     * (recorridos que hacemos con foreach) de las claves
     */
    private class KeySet extends AbstractSet<K> {
        /**
         * Personaliza el recorrido de las claves
         * 
         * @return Iterator<K> -> recorrido de claves
         */
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }
        /**
         * Misma idea de la función size de nuestra estructura
         * 
         * @return int -> entero 
         */
        @Override
        public int size() {
            return size;
        }        
    }
    /**
     * Clase interna para dar estilo al recorrido de las claves
     */
    private final class KeyIterator extends HashIterator<K> {
        /**
         * Obtiene la siguiente clave del recorrido
         * 
         * @return K -> clave
         */
        @Override
        public K next() {
            return nextEntry().getKey();
        }        
    }
    /*-----------------------------------------------------------*/
    /**
     * Devuelve una colección de los valores almacenados de 
     * nuestra estructura de datos
     * 
     * @return Collection<V> -> colección de valores
     */
    @Override
    public Collection<V> values() {
        return new Values();
    }
    /**
     * Values es una clase interna que utilizamos para las iteraciones
     * (recorridos que hacemos con foreach) de los valores
     */
    private class Values extends AbstractCollection<V>{
        /**
         * Personaliza el recorrido de los valores
         * 
         * @return Iterator<V> -> recorrido de valores
         */
        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }
        /**
         * Misma idea de la función size de nuestra estructura
         * 
         * @return int -> entero 
         */
        @Override
        public int size() {
            return size;
        }        
    }
    /**
     * Clase interna para dar estilo al recorrido de los valores
     */
    private final class ValueIterator extends HashIterator<V>{
        /**
         * Devuelve el siguiente valor del recorrido
         * 
         * @return V -> valor 
         */
        @Override
        public V next() {
            return nextEntry().getValue();
        }        
    }
    /*-----------------------------------------------------------*/
    /**
     * Devuelve un conjunto de las entradas almacenadas en 
     * nuestra estructura de datos
     * 
     * @return Set<Map.Entry<K, V>> -> conjunto de entradas
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }
    /**
     * EntrySet es una clase interna que utilizamos para las iteraciones
     * (recorridos que hacemos con foreach) de las entradas
     */
    private class EntrySet extends AbstractSet<Map.Entry<K,V>> {
        /**
         * Personaliza el recorrido de las entradas
         * 
         * @return Map.Entry<K, V> -> recorrido de entradas
         */
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }
        /**
         * Misma idea de la función size de nuestra estructura
         * 
         * @return int -> entero 
         */
        @Override
        public int size() {
            return size;
        }        
    }
    /**
     * Clase interna para dar estilo al recorrido de las entradas
     */
    private final class EntryIterator extends HashIterator<Map.Entry<K,V>> {
        /**
         * Devuelve la siguiente entrada del recorrido
         * 
         * @return V -> valor 
         */
        @Override
        public Entry<K, V> next() {
            return nextEntry();
        }        
    }
    /*-----------------------------------------------------------*/
    /**
     * Clase abstracta que usamos para los distintos tipos de 
     * recorridos empleados en nuestra estructura de datos
     * 
     * @param <E> 
     */
    private abstract class HashIterator<E> implements Iterator<E> {
        private int index;
        private Entry<K,V> curr;
        private Entry<K,V> next;
        /**
         * Construye una nueva iteración hash
         */
        @SuppressWarnings("empty-statement")
        HashIterator() {
            curr = null;
            next = null;
            findEntry(0);
        }
        /**
         * Verifica si hay una siguiente entrada
         * 
         * @return boolean -> verdadero o falso 
         */
        @Override
        public boolean hasNext() {
            return next != null;
        }
        /**
         * Obtiene la entrada próxima, y también es una función 
         * sobreexplotada para los recorridos ;)
         * 
         * @return Entry<K,V> -> entrada clave/valor
         */
        @SuppressWarnings("empty-statement")
        public Entry<K,V> nextEntry() {
            curr = next;
            next = next.next;
            if (next == null) {
                findEntry(index + 1);
            }
            return curr;
        }
        /**
         * Encuentra la siguiente posición no vacía de un array con
         * una entrada disponible
         * 
         * @param n -> int 
         */
        private void findEntry(int n) {
            for (int i = n; i < table.length; i++) {
                Entry entry = table[i];
                if (entry != null) {
                    next = entry;
                    index = i;
                    break;
                }
            }
        }
    }
    /*-----------------------------------------------------------*/
    /**
     * Clase interna para definir las entradas clave/valor que 
     * almacenaremos en nuestra estructura de datos
     * 
     * @param <K>
     * @param <V> 
     */
    class Entry<K,V> implements Map.Entry<K,V>{
        final K key;
        V value;
        Entry<K,V> next;
        public Entry(K xkey, V xvalue) {
            this.key = xkey;
            this.value = xvalue;
            this.next = null;
        }        
        @Override
        public K getKey() {
            return key;
        }
        @Override
        public V getValue() {
            return value;
        }
        @Override
        public V setValue(V v) {
            V val = value;
            value = v;
            return val;
        }
        /**
         * Reemplaza la entrada con clave existente de la lista por la 
         * existente (Método para sobreescribir en MyLinkedMap).
         * 
         * @param m 
         */
        void afterNodeAccess(MyMap<K,V> m) { }
        /**
         * Remueve la entrada existente de la lista (Método para sobreescribir 
         * en MyLinkedMap).
         * 
         * @param m 
         */
        void afterNodeRemoval(MyMap<K,V> m) { }
        /**
         * Sobreescrita: función útil para las colisiones de los hashing
         * y mantener el órden de la lista
         * 
         * @param key
         * @param value
         * @return V -> valor
         */
        V chain(K key, V value){
            this.next = new Entry(key,value);
            size++;
            return value;
        }
        @Override
        public String toString() {
            return "["+ this.getKey() + " -> " + this.getValue() + "]";
        }
    } 
}