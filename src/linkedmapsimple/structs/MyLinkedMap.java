package linkedmapsimple.structs;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
/**
 * @author detectivejd
 * @param <K>
 * @param <V> 
 */
public class MyLinkedMap<K,V> extends MyMap<K,V>
{
    /**
     * La cabeza de la lista enlazada
     */
    private Entry<K,V>head;
    /**
     * La cola de la lista enlazada
     */
    private Entry<K,V>tail;
    /**
      * El método de ordenación de iteración para este hashmap enlazado: 
      * {@code true} para el orden de acceso, {@code false} para el orden 
      * de la inserción.
      *
      * @serial
    */
    private boolean accessOrder;
    /**
     * Construye un nuevo HashMap con una cantidad a almacenar por 
     * defecto
     */
    public MyLinkedMap() {
        super();
        accessOrder = false;
    }
    /**
     * Construye un nuevo HashMap según la cantidad de elementos que
     * deseamos almacenar
     * 
     * @param xcap -> capacidad de elementos a almacenar 
     */
    public MyLinkedMap(int xcap) {
        super(xcap);
        accessOrder = false;
    }
    /**
     * Construye un nuevo HashMap que utilizamos para almacenar toda una 
     * estructura de datos tipo map a nuestra estructura de datos
     * 
     * @param m 
     */
    public MyLinkedMap(Map<? extends K, ? extends V> m) {
        super(m);
        accessOrder = false;
    }
    /**
     * Sobreescrita: inicializa la cabeza y cola de la estructura de datos
     * a un estado por defecto
     */
    @Override
    void init() {
        head = tail = null;
    }
    /**
     * Sobreescrita: Limpieza de las entradas de nuestra estructura
     */
    @Override
    public void clear() {
        super.clear();
        head = tail = null;
        //head.before = head.after = head;
    } 
    /**
     * Sobreescrita: Verifica si existe o no la entrada pasada por 
     * parámetro
     * 
     * @param value
     * @return boolean 
     */    
    @Override
    public boolean containsValue(Object value) {
        for (Entry<K,V> e = head; e != null; e = e.after) {
            if(value != null && value.equals(e.getValue())){
                return true;
            }
        }
        return false;
    }    
    /**
     * Sobreescrita: Creamos una nueva entrada, la cual mantenemos
     * el orden de la misma
     * 
     * @param key
     * @param value 
     */
    @Override
    protected void createEntry(K key, V value) {
        int hash = hash(key,table.length);
        Entry<K,V> e = new Entry(key, value);
        table[hash] = e;        
        linkNodeLast(e);
        size++;
    }
    /**
     * Enlaza la entrada clave/valor hacia el final de la lista
     * 
     * @param p -> entrada con clave y valor
     */
    private void linkNodeLast(Entry<K,V> p) {
        Entry<K,V> last = tail;
        tail = p;
        if (last == null)
            head = p;
        else {
            p.before = last;
            last.after = p;
        }
    }
    /**
     * Obtiene la clave del inicio de la lista
     * 
     * @return K -> clave 
     */
    public K firstKey(){
        return (head == null) ? null : (K) head.getKey();
    }
    /**
     * Obtiene la entrada clave/valor del inicio de la lista
     * 
     * @return Entry<K,V> -> entrada clave/valor
     */
    public Entry<K,V> firstEntry(){
        return head;
    }
    /**
     * Obtiene la clave del fin de la lista
     * 
     * @return K -> clave 
     */
    public K lastKey(){
        return (tail == null) ? null : (K) tail.getKey();
    }
    /**
     * Obtiene la entrada clave/valor del fin de la lista
     * 
     * @return Entry<K,V> -> entrada clave/valor
     */
    public Entry<K,V> lastEntry(){
        return tail;
    }    
    /*--------------------------------------------------------------------*/
    /**
     * Devuelve un conjunto de las entradas almacenadas en 
     * nuestra estructura de datos
     * 
     * @return Set<Map.Entry<K, V>> -> conjunto de entradas
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new LinkedEntrySet();
    }
    /**
     * EntrySet es una clase interna que utilizamos para las iteraciones
     * (recorridos que hacemos con foreach) de las entradas ordenadas
     * por inserción
     */
    private class LinkedEntrySet extends AbstractSet<Map.Entry<K,V>> {
        /**
         * Personaliza el recorrido de las entradas
         * 
         * @return Map.Entry<K, V> -> recorrido de entradas
         */
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new LinkedEntryIterator();
        }
        /**
         * Misma idea de la función size
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
     * ordenadas según la inserción
     */    
    private class LinkedEntryIterator extends LinkedHashIterator {
        /**
         * Devuelve la siguiente entrada del recorrido
         * 
         * @return V -> valor 
         */
        @Override
        public final Map.Entry<K,V> next() { 
            return nextNode(); 
        }
    }
    /*--------------------------------------------------------------------*/
    /**
     * Devuelve un conjunto de todas las claves almacenadas en 
     * nuestra estructura de datos
     * 
     * @return Set<K> -> tupla de claves
     */
    @Override
    public Set<K> keySet() {
        return new LinkedKeySet();
    }
    /**
     * KeySet es una clase interna que utilizamos para las iteraciones
     * (recorridos que hacemos con foreach) de las claves ordenadas
     * por inserción
     */
    private class LinkedKeySet extends AbstractSet<K> {
        /**
         * Personaliza el recorrido de las claves
         * 
         * @return Iterator<K> -> recorrido de claves
         */
        @Override
        public Iterator<K> iterator() {
            return new LinkedKeyIterator();
        }
        /**
         * Misma idea de la función size
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
     * ordenadas según la inserción
     */
    private class LinkedKeyIterator extends LinkedHashIterator<K>{
        /**
         * Obtiene la siguiente clave del recorrido
         * 
         * @return K -> clave
         */
        @Override
        public K next() {
            return (K) nextNode().getKey();
        }                
    }
    /*--------------------------------------------------------------------*/
    /**
     * Devuelve una colección de los valores almacenados de 
     * nuestra estructura de datos
     * 
     * @return Collection<V> -> colección de valores
     */
    @Override
    public Collection<V> values() {
        return new LinkedValues();
    }
    /**
     * Values es una clase interna que utilizamos para las iteraciones
     * (recorridos que hacemos con foreach) de los valores ordenados
     * por inserción
     */
    private class LinkedValues extends AbstractCollection<V> {
        /**
         * Personaliza el recorrido de los valores
         * 
         * @return Iterator<V> -> recorrido de valores
         */
        @Override
        public Iterator<V> iterator() {
            return new LinkedValueIterator();
        }
        /**
         * Misma idea de la función size
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
     * ordenados según la inserción
     */
    private class LinkedValueIterator extends LinkedHashIterator<V> {
        @Override
        public V next() {
            return (V) nextNode().getValue();
        }        
    }
    /*--------------------------------------------------------------------*/
    /**
     * Clase abstracta que usamos para los distintos tipos de 
     * recorridos ordenados por inserción 
     * 
     * @param <E> 
     */
    private abstract class LinkedHashIterator<E> implements Iterator<E> {
        Entry<K,V> next;
        Entry<K,V> current;
        /**
         * Construye una nueva iteración linked-hash
         */        
        LinkedHashIterator() {
            next = head;
            current = null;
        }
        /**
         * Verifica si hay una siguiente entrada
         * 
         * @return boolean 
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
        public Entry<K, V> nextNode() {
            Entry<K,V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            current = e;
            next = e.after;
            return e;
        }        
    }
    /**
     * Clase interna que hereda de Map de la estructura
     * casera MyMap
     * 
     * @param <K>
     * @param <V> 
     */
    class Entry<K,V> extends MyMap.Entry {
        Entry<K,V> before, after;
        public Entry(K xkey, V xvalue) {
            super(xkey, xvalue);
        }    
        /**
         * Sobreescrita: Reemplaza la entrada con clave existente de la lista 
         * por la existente
         * 
         * @param m 
         */
        @Override
        void afterNodeAccess(MyMap m) {
            Entry<K,V> last;
            if (accessOrder && (last = ((MyLinkedMap)m).tail) != this) {
                Entry<K,V> b = this.before; 
                Entry<K,V> a = this.after;
                this.after = null;
                if (b == null)
                    ((MyLinkedMap)m).head = a;
                else
                    b.after = a;
                if (a != null)
                    a.before = b;
                else
                    last = b;
                if (last == null)
                    ((MyLinkedMap)m).head = this;
                else {
                    this.before = last;
                    last.after = this;
                }
                ((MyLinkedMap)m).tail = this;
            }
        }
        /**
         * Sobreescrita: Remueve la entrada existente de la lista
         * 
         * @param m 
         */
        @Override
        void afterNodeRemoval(MyMap m) {
            Entry<K,V> b = this.before, a = this.after;
            this.before = this.after = null;
            if (b == null)
                ((MyLinkedMap)m).head = a;
            else
                b.after = a;
            if (a == null)
                ((MyLinkedMap)m).tail = b;
            else
                a.before = b;
        }   
        /**
         * Sobreescrita: función útil para las colisiones de los hashing
         * y mantener el órden de la lista
         * 
         * @param key
         * @param value
         * @return Object -> Objeto
         */
        @Override
        Object chain(Object key, Object value) {
            this.next = new Entry(key,value);
            linkNodeLast((MyLinkedMap.Entry)this.next);
            size++;
            return value;
        }                      
    }
}