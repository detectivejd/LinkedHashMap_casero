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
     * La cabeza (más vieja) de la lista doble enlazada
     */
    private Entry<K,V>head;
    /**
     * La cola (más nueva) de la lista doble enlazada
     */
    private Entry<K,V>tail;
    /**
     * Construye un nuevo HashMap con una cantidad a almacenar por 
     * defecto
     */
    public MyLinkedMap() {
        super();
    }
    /**
     * Construye un nuevo HashMap según la cantidad de elementos que
     * deseamos almacenar
     * 
     * @param xcap -> capacidad de elementos a almacenar 
     */
    public MyLinkedMap(int xcap) {
        super(xcap);
    }
    /**
     * Construye un nuevo HashMap que utilizamos para almacenar toda una 
     * estructura de datos tipo map a nuestra estructura de datos
     * 
     * @param m 
     */
    public MyLinkedMap(Map<? extends K, ? extends V> m) {
        super(m);
    }
    /**
     * Sobreescrita: Limpieza de las entradas de nuestra estructura
     */
    @Override
    public void clear() {
        super.clear();
        head = tail = null;
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
        maintainOrderAfterInsert(e);
        size++;
    }
    /*--------------------------------------------------------------------*/
    /**
     * Mantiene el orden de la estructura luego de ser insertada
     * una nueva entrada
     * 
     * @param newEntry 
     */
    private void maintainOrderAfterInsert(Entry<K, V> newEntry) {           
        if(head==null){
           head=newEntry;
           tail=newEntry;
           return;
        }      
        if(head.key.equals(newEntry.key)){
           deleteFirst();
           insertFirst(newEntry);
           return;
        }      
        if(tail.key.equals(newEntry.key)){
           deleteLast();
           insertLast(newEntry);
           return;
        }      
        Entry<K, V> beforeDeleteEntry= deleteSpecificEntry(newEntry);
        if(beforeDeleteEntry==null){
           insertLast(newEntry);
        }
        else{
           insertAfter(beforeDeleteEntry,newEntry);
        }            
    }
    /*--------------------------------------------------------------------*/
    /**
     * Inserte la entrada en el lugar dónde será reemplazada la entrada
     * a ser borrada
     * 
     * @param beforeDeleteEntry
     * @param newEntry 
     */
    private void insertAfter(Entry<K, V> beforeDeleteEntry, Entry<K, V> newEntry) {
        Entry<K, V> current=head;
        while(current!=beforeDeleteEntry){
               current=current.after;  //move to next node.
        }           
        newEntry.after=beforeDeleteEntry.after;
        beforeDeleteEntry.after.before=newEntry;
        newEntry.before=beforeDeleteEntry;
        beforeDeleteEntry.after=newEntry;           
    }    
    /**
     * Inserta la entrada al principio de los punteros
     * 
     * @param newEntry 
     */
    private void insertFirst(Entry<K, V> newEntry){                 
        if(head==null){ //no entry found
            head=newEntry;
            tail=newEntry;
        } else {           
            newEntry.after=head;
            head.before=newEntry;
            head=newEntry;
        }           
    }    
    /**
     * Inserta la entrada al final de los punteros
     * 
     * @param newEntry 
     */
    private void insertLast(Entry<K, V> newEntry){           
        if(head==null){
            head=newEntry;
            tail=newEntry;
        } else {
           tail.after=newEntry;
           newEntry.before=tail;
           tail=newEntry;
        }
    }
    /*--------------------------------------------------------------------*/
    /**
     * Elimina la entrada que encabeza los punteros
     */
    private void deleteFirst(){ 
        if(head==tail){ //only one entry found.
            head=tail=null;
        } else {
           head=head.after;
           head.before=null; 
        }           
    }
    /**
     * Elimina la entrada del final de los punteros
     */
    private void deleteLast(){           
        if(head==tail){ //only one entry found.
            head=tail=null;
        } else {           
           tail=tail.before;
           tail.after=null; 
        }
    }
    /**
     * Encuentra una entrada específica para luego ser borrada
     * 
     * @param newEntry
     * @return Entry<K, V>
     */
    private Entry<K, V> deleteSpecificEntry(Entry<K, V> newEntry){                        
        Entry<K, V> current=head;
        while(!current.key.equals(newEntry.key)){
            if(current.after==null){   //entry not found
                  return null;
            }
            current=current.after;  //move to next node.
        }           
        Entry<K, V> beforeDeleteEntry=current.before;
        current.before.after=current.after;
        current.after.before=current.before;  //entry deleted
        return beforeDeleteEntry;
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
        Entry<K,V> before = null;
        Entry<K,V> after = null;
        public Entry(K xkey, V xvalue) {
            super(xkey, xvalue);
        }
    }
}