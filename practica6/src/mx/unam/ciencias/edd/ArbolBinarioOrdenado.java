package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios ordenados. */
    private class Iterador implements Iterator<T> {

        /* Pila para emular la pila de ejecución. */
        private Pila<ArbolBinario<T>.Vertice> pila;

        /* Construye un iterador con el vértice recibido. */
        public Iterador() {
	    pila = new Pila<>();
	    if(raiz != null){
		pila.mete(raiz);
		Vertice v = raiz;
		while(v.hayIzquierdo()){
		    pila.mete(v.izquierdo);
		    v = v.izquierdo;
		}
	    }
	}
	
	/* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
	    return !pila.esVacia();	    
        }

        /* Regresa el siguiente elemento del árbol en orden. */
        @Override public T next() {
          Vertice v = pila.saca();
	  T e = v.get();
	  v = v.derecho;
	  
	  while(v != null){
	      pila.mete(v);
	      v = v.izquierdo;
	  }
	  return e;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	if(elemento == null)
	    throw new IllegalArgumentException();
	if(raiz == null)
	    raiz = ultimoAgregado = nuevoVertice(elemento);  
	else
	    agrega(raiz, elemento);
	elementos ++;
    }

    private void agrega(Vertice v, T elemento){
	if(elemento.compareTo(v.get()) < 0)
	    if(!v.hayIzquierdo()){
		Vertice v2 = nuevoVertice(elemento);
		v2.padre = v;
		v.izquierdo = ultimoAgregado = v2;
	    } else
		agrega(v.izquierdo, elemento);
	else
	    if(!v.hayDerecho()){
		Vertice v1= nuevoVertice(elemento);
		v1.padre = v;
		v.derecho = ultimoAgregado = v1;
	    } else
		agrega(v.derecho, elemento);
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
	 Vertice vertice = busca(raiz, elemento);
        if (vertice == null)
            return;
        if (vertice.hayIzquierdo()) {
            Vertice aux = vertice;
            vertice = maximoEnSubarbol(vertice.izquierdo);
            aux.elemento = vertice.elemento;
        }
        
        if (vertice.izquierdo == null && vertice.derecho == null)
            if (vertice == raiz)
                raiz = ultimoAgregado = null;
            else if (vertice.padre.izquierdo == vertice)
                vertice.padre.izquierdo = vertice.padre = null;
            else
                vertice.padre.derecho = vertice.padre = null;
        else if (vertice.hayIzquierdo())
            if (vertice == raiz) {
                raiz = vertice.izquierdo;
                raiz.padre = null;
            } else {
                vertice.izquierdo.padre = vertice.padre;
                if (vertice.padre.izquierdo == vertice)
                    vertice.padre.izquierdo = vertice.izquierdo;
                else
                    vertice.padre.derecho = vertice.izquierdo;
            }
        else
            if (vertice == raiz) {
                raiz = raiz.derecho;
                raiz.padre = null;
            } else {
                vertice.derecho.padre = vertice.padre;
                if (vertice.padre.izquierdo == vertice)
                    vertice.padre.izquierdo = vertice.derecho;
                else
                    vertice.padre.derecho = vertice.derecho;
            }
        elementos--;
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
       return  busca(raiz, elemento);
    }

    /**
     * Busca recursivamente un elemento, a partir del vértice recibido.
     * @param vertice el vértice a partir del cuál comenzar la búsqueda. Puede
     *                ser <code>null</code>.
     * @param elemento el elemento a buscar a partir del vértice.
     * @return el vértice que contiene el elemento a buscar, si se encuentra en
     *         el árbol; <code>null</code> en otro caso.
     */
    @Override protected Vertice busca(Vertice vertice, T elemento) {
	if(vertice == null || elemento == null)
	    return null;
	if(elemento.compareTo(vertice.elemento) == 0)
	    return vertice;
	if(elemento.compareTo(vertice.elemento) < 0)
		return busca(vertice.izquierdo, elemento);
	return busca(vertice.derecho, elemento);
    }

    /**
     * Regresa el vértice máximo en el subárbol cuya raíz es el vértice que
     * recibe.
     * @param vertice el vértice raíz del subárbol del que queremos encontrar el
     *                máximo.
     * @return el vértice máximo el subárbol cuya raíz es el vértice que recibe.
     */
    protected Vertice maximoEnSubarbol(Vertice vertice) {
	while(vertice.hayDerecho())
	    vertice = vertice.derecho;
	return vertice;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
	return new Iterador();
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
	if(vertice == null || !vertice.hayIzquierdo())
	    return;
	Vertice v = vertice(vertice);
	Vertice v2 = v.izquierdo;
	v2.padre = v.padre;
	if(v.padre != null)
	    if(v.padre.izquierdo == v)
		v.padre.izquierdo = v.izquierdo;
	    else	
		v.padre.derecho = v.izquierdo;
	else
	    raiz = v2;
	v.izquierdo = v2.derecho;
	if(v2.hayDerecho())
	    v2.derecho.padre = v;
	v2.derecho = v;
 	v.padre = v2;
	
    }
		
    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
	if(vertice == null || !vertice.hayDerecho())
	    return;
        Vertice v = vertice(vertice);
	Vertice v2 = v.derecho;
	v2.padre = v.padre;
	    if(v.padre != null)
		if(v.padre.izquierdo == v)
		    v.padre.izquierdo = v.derecho;
		else
		    v.padre.derecho = v.derecho;
	    else
		raiz = v2;
	v.derecho = v2.izquierdo;
	if(v2.hayIzquierdo())
	    v2.izquierdo.padre = v;
	v2.izquierdo = v;
	v.padre = v2;	    
    }
}
