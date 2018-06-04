package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>). Podemos crear un montículo
 * mínimo con <em>n</em> elementos en tiempo <em>O</em>(<em>n</em>), y podemos
 * agregar y actualizar elementos en tiempo <em>O</em>(log <em>n</em>). Eliminar
 * el elemento mínimo también nos toma tiempo <em>O</em>(log <em>n</em>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T> {

    /* Clase privada para iteradores de montículos. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return indice < siguiente && arbol[indice] != null;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
	    if(this.hasNext())
		return arbol[indice ++];
	    throw new NoSuchElementException();
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* El siguiente índice dónde agregar un elemento. */
    private int siguiente;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] creaArregloGenerico(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)}, pero se ofrece este constructor por
     * completez.
     */
    public MonticuloMinimo() {
	arbol = creaArregloGenerico(1);
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *              montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
	siguiente = coleccion.getElementos();
	arbol = creaArregloGenerico(coleccion.getElementos());
	int n = 0;
	for(T e : coleccion){
	    arbol[n] = e;
	    arbol[n].setIndice(n);
	    ++n;
	}
	for(int i = (siguiente - 1)/2; i >= 0; i--)
	    minimizaMonticulo(i);	    
    }

    private void minimizaMonticulo(int i){
	int izq = 2 * i + 1;
	int der = 2 * i + 2;
	int min = i;
	if(izq >= siguiente && der >= siguiente)
            return;	
	if(izq < siguiente && arbol[izq].compareTo(arbol[min]) < 0){
	    min = izq;
	}
	if(der < siguiente && arbol[der].compareTo(arbol[min]) < 0){
	    min = der;
	}
	if(min != i){
	    T a = arbol[i];
	    arbol[i] = arbol[min];
	    arbol[i].setIndice(i);
	    arbol[min] = a;
	    arbol[min].setIndice(min);
	    minimizaMonticulo(min);
	}
    }

    
    private void update(int i){
	int padre = (i - 1) / 2;
	int min = i;
	if(padre >= 0 && arbol[padre].compareTo(arbol[min]) > 0)
	    min = padre;
	if(min != i){
	    T a = arbol[i];
	    arbol[i] = arbol[padre];
	    arbol[i].setIndice(i);
	    arbol[padre] = a;
	    arbol[padre].setIndice(padre);
	    update(min);
	}	    
    }
    
    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
	if(siguiente <= arbol.length){
	    T[] arbol2 = creaArregloGenerico(arbol.length + 200);
	    for(int i = 0; i < arbol.length; i ++)
		arbol2[i] = arbol[i]; 
	    arbol = arbol2;
	}
	arbol[siguiente] = elemento;
	elemento.setIndice(siguiente);
	update(siguiente++);
	
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    public T elimina() {
	if(esVacio())
	    throw new IllegalStateException();
	T a = arbol[0];
	intercambia(arbol, 0, --siguiente);
	arbol[siguiente].setIndice(-1);
	arbol[siguiente] = null;
	minimizaMonticulo(0);    
	return a;
    }

      private void intercambia(T[] a, int i, int j){
	if( i == j) 
	    return;
	T b = a[j];
	a[j] = a[i];
	a[i] = b;
	a[j].setIndice(j);
	a[i].setIndice(i);
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
	if(elemento == null)
	    return;
	if(!contiene(elemento))
	    return;
	int i = elemento.getIndice();
	intercambia(arbol, i, --siguiente);
	arbol[siguiente].setIndice(-1);
	arbol[siguiente] = null;
	reordena(arbol[i]);
	
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
	for(T e : arbol)
	    if (!e.equals(elemento))
		continue;
	    else
		return true;
	return false;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacio() {
	return siguiente == 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    public void reordena(T elemento) {
	if(elemento == null)
	    return;
	int i = elemento.getIndice();
	update(i);
	minimizaMonticulo(i); 
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
	return siguiente;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    public T get(int i) {
        if(i < 0 || siguiente <= i)
	    throw new NoSuchElementException();
	return arbol[i];
	    
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si el montículo mínimo es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof MonticuloMinimo))
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> mm =
            (MonticuloMinimo<T>)o;
	if(this.getElementos() != mm.getElementos())
	    return false;
	for(T a : mm){
	    int i = 0;
	    if(this.get(i).equals(mm.get(i)))
		continue;
	    else
		return false;
	}
	return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
