package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios completos. */
    private class Iterador implements Iterator<T> {

        private Cola<ArbolBinario<T>.Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador() {
            cola = new Cola<>();
	    if(raiz != null)
		cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            if(!cola.esVacia())
		return true;
	    return false;
        }

        /* Regresa el elemento siguiente. */
        @Override public T next() {
            Vertice v = cola.saca();
	    if(v.hayIzquierdo())
		cola.mete(v.izquierdo);
	    if(v.hayDerecho())
		cola.mete(v.derecho);
	    return v.get();
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
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
	if(elemento == null)
	    throw new IllegalArgumentException();
	Vertice v = nuevoVertice(elemento);
	if(raiz == null)
	    raiz = ultimoAgregado = v;
	else{
	    Vertice v2 = raiz;
	    Cola<ArbolBinario<T>.Vertice> c = new Cola<>();
	    c.mete(v2);
	    while(!c.esVacia()){
		v2 = c.saca();
		if(!v2.hayIzquierdo() || !v2.hayDerecho()){
		    v.padre = v2;
		    if(!v2.hayIzquierdo())
			ultimoAgregado = v2.izquierdo = v;
		    else if(!v2.hayDerecho())
			ultimoAgregado = v2.derecho = v;
		    break;
		}
		    c.mete(v2.izquierdo);
		    c.mete(v2.derecho);
	    }
	}
	elementos ++;
    }
    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        if(raiz == null)
	    return;
	Vertice v = raiz;
	Vertice nv = null;
	Cola<ArbolBinario<T>.Vertice> cola = new Cola<>();
	cola.mete(v);
	while(!cola.esVacia()){
	    v = cola.saca();
	    if(v.get().equals(elemento)){
		v.elemento = ultimoAgregado.elemento;
		elementos --;
	    }
	    if(v != ultimoAgregado)
		nv = v;
	    if(v == ultimoAgregado){
		if(!v.hayPadre())
		    raiz =ultimoAgregado = null;
		else{
		    ultimoAgregado = nv;
		    if(v.padre.izquierdo == v)
			v.padre.izquierdo = null;
		    else
			v.padre.derecho = null;
		}
	    }
	    if(v.izquierdo!= null)
		cola.mete(v.izquierdo);
	    if(v.derecho != null)
		cola.mete(v.derecho);    
	}	
    }
    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
