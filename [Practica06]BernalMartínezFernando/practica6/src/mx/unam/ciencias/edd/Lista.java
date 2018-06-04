package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas implementan la interfaz {@link Iterable}, y por lo tanto se
 * pueden recorrer usando la estructura de control <em>for-each</em>. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo {
        public T elemento;
        public Nodo anterior;
        public Nodo siguiente;

        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        public Lista<T>.Nodo anterior;
        public Lista<T>.Nodo siguiente;

        public Iterador() {
            start();
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if (siguiente == null)
                throw new NoSuchElementException("No hay siguiente.");
            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (anterior == null)
                throw new NoSuchElementException("No hay anterior.");
            siguiente = anterior;
            anterior = anterior.anterior;
            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
        }

        /* No implementamos este método. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    public int getElementos() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacio() {
        return longitud == 0;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agrega(T elemento) {
        agregaFinal(elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("Elemento nulo.");
        Nodo nodo = new Nodo(elemento);
        if (rabo == null) {
            cabeza = rabo = nodo;
        } else {
            rabo.siguiente = nodo;
            nodo.anterior = rabo;
            rabo = nodo;
        }
        longitud++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException("Elemento nulo.");
        Nodo nodo = new Nodo(elemento);
        if (cabeza == null) {
            cabeza = rabo = nodo;
        } else {
            cabeza.anterior = nodo;
            nodo.siguiente = cabeza;
            cabeza = nodo;
        }
        longitud++;
    }

    /* Busca un nodo en la lista. */
    private Nodo buscaNodo(Nodo nodo, T elemento) {
        if (nodo == null)
            return null;
        if (nodo.elemento.equals(elemento))
            return nodo;
        return buscaNodo(nodo.siguiente, elemento);
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) {
        Nodo nodo = buscaNodo(cabeza, elemento);
        if (nodo == null)
            return;
        if (cabeza == rabo) {
            cabeza = rabo = null;
        } else if (nodo == cabeza) {
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        } else if (nodo == rabo) {
            rabo = rabo.anterior;
            rabo.siguiente = null;
        } else {
            nodo.anterior.siguiente = nodo.siguiente;
            nodo.siguiente.anterior = nodo.anterior;
        }
        longitud--;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (cabeza == null)
            throw new NoSuchElementException("Lista vacía.");
        T p = cabeza.elemento;
        cabeza = cabeza.siguiente;
        if (cabeza != null)
            cabeza.anterior = null;
        else
            rabo = null;
        longitud--;
        return p;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if (rabo == null)
            throw new NoSuchElementException("Lista vacía.");
        T u = rabo.elemento;
        rabo = rabo.anterior;
        if (rabo != null)
            rabo.siguiente = null;
        else
            cabeza = null;
        longitud--;
        return u;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(T elemento) {
        return buscaNodo(cabeza, elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> r = new Lista<T>();
        return reversa(r, cabeza);
    }

    /* Método auxiliar recursivo para reversa. */
    private Lista<T> reversa(Lista<T> r, Nodo n) {
        if (n == null)
            return r;
        r.agregaInicio(n.elemento);
        return reversa(r, n.siguiente);
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> c = new Lista<T>();
        return copia(c, cabeza);
    }

    /* Método auxiliar recursivo para copia. */
    private Lista<T> copia(Lista<T> c, Nodo nodo) {
        if (nodo == null)
            return c;
        c.agregaFinal(nodo.elemento);
        return copia(c, nodo.siguiente);
    }

    /**
     * Limpia la lista de elementos. El llamar este método es equivalente a
     * eliminar todos los elementos de la lista.
     */
    public void limpia() {
        cabeza = rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (cabeza == null)
            throw new NoSuchElementException("Lista vacía.");
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (rabo == null)
            throw new NoSuchElementException("Lista vacía.");
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (i < 0 || i >= longitud)
            throw new ExcepcionIndiceInvalido();
        return get(cabeza, i, 0);
    }

    /* Método auxiliar recursivo para get. */
    private T get(Nodo nodo, int i, int j) {
        if (i == j)
            return nodo.elemento;
        return get(nodo.siguiente, i, ++j);
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        return indiceDe(elemento, cabeza, 0);
    }

    /* Método auxiliar recursivo para indiceDe. */
    private int indiceDe(T elemento, Nodo nodo, int i) {
        if (nodo == null)
            return -1;
        if (nodo.elemento.equals(elemento))
            return i;
        return indiceDe(elemento, nodo.siguiente, i+1);
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (cabeza == null)
            return "[]";
        return "[" + cabeza.elemento.toString() + toString(cabeza.siguiente);
    }

    /* Método auxiliar recursivo para toString. */
    private String toString(Nodo nodo) {
        if (nodo == null)
            return "]";
        return ", " + nodo.elemento.toString() + toString(nodo.siguiente);
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof Lista))
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
        Nodo t1 = cabeza;
        Nodo t2 = lista.cabeza;
        while (t1 != null && t2 != null) {
            if (!t1.elemento.equals(t2.elemento))
                return false;
            t1 = t1.siguiente;
            t2 = t2.siguiente;
        }
        if (t1 != null || t2 != null)
            return false;
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

      private static<T extends Comparable <T>> Lista<T> mezcla(Lista<T> l, Lista<T> j){
	Lista<T> c = new Lista<T>();
	Lista<T>.Nodo n = l.cabeza;
	Lista<T>.Nodo m = j.cabeza;
	while(n != null){
	    if(n.elemento.compareTo(m.elemento)<0){
		c.agregaFinal(n.elemento);
		n = n.siguiente;
	    }else{
		c.agregaFinal(m.elemento);
		m = m.siguiente;
	    }
	    if(m == null){
		break;
	    }
	}
	while(n != null){
	    c.agregaFinal(n.elemento);
	    n = n.siguiente;
	}
	while(m != null){
	    c.agregaFinal(m.elemento);
	    m = m.siguiente;
	}
	return c.copia();
      }
    
    public static <T extends Comparable<T>>
	Lista<T> mergeSort(Lista<T> l) {
	if(l.longitud < 2)
	    return l.copia();
	Lista<T> ll = new Lista<T>();
	Lista<T> lr = new Lista<T>();
	Lista<T>.Nodo n =l.cabeza;
	for(int i = 0; i < l.longitud/2; i++){
	    ll.agregaFinal(n.elemento);
	    n = n.siguiente;
	}
	for(int i = l.longitud/2; i < l.longitud; i++){
	    lr.agregaFinal(n.elemento);
	    n = n.siguiente;
	}
	return mezcla(mergeSort(ll), mergeSort(lr));	
    }
    
    public static <T extends Comparable<T>>
	boolean busquedaLineal(Lista<T> l, T e) {
	Lista<T>.Nodo n = l.cabeza;
	Lista<T>.Nodo m = l.rabo;
        if(0 < e.compareTo(m.elemento))
	    return false;
	if(e.compareTo(n.elemento) < 0)
	    return false;
	while(n != null){
	    if(e.compareTo(n.elemento) < 0)
		return false;
	    if(e.compareTo(n.elemento) == 0)
		return true;
	    n= n.siguiente;   
	}
	return false;
    }
}
