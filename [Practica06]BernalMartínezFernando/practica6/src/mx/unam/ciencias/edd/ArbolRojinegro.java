package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros son autobalanceados, y por lo tanto las operaciones de
 * inserción, eliminación y búsqueda pueden realizarse en <i>O</i>(log
 * <i>n</i>).
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles rojinegros. La única
     * diferencia con los vértices de árbol binario, es que tienen un campo para
     * el color del vértice.
     */
    protected class VerticeRojinegro extends ArbolBinario<T>.Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
	    super(elemento);
	    color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
	    return String.format("%s{%s}", color == Color.ROJO ? "R" : "N", elemento.toString());
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeRojinegro vertice = (VerticeRojinegro)o;
	    return raiz.get().equals(vertice.get()) && verticeRojinegro(raiz).color == vertice.color
		&& equals(verticeRojinegro(raiz.izquierdo), verticeRojinegro(vertice.izquierdo))
		&& equals(verticeRojinegro(raiz.derecho), verticeRojinegro(vertice.derecho));

	}

	private boolean equals(VerticeRojinegro v, VerticeRojinegro v1){
	    if(v == null && v1 == null)
		return true;
	    if(v == null && v1 != null || v != null && v1 == null)
		return false;
	    return v.get().equals(v1.get()) && verticeRojinegro(v).color == v1.color
		&& equals(verticeRojinegro(v.izquierdo), verticeRojinegro(v1.izquierdo))
		&& equals(verticeRojinegro(v.derecho), verticeRojinegro(v1.derecho));
	}
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * VerticeRojinegro}). Método auxililar para hacer esta audición en un único
     * lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice
     *                rojinegro.
     * @return el vértice recibido visto como vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = (VerticeRojinegro)vertice;
        return v;
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        return verticeRojinegro(vertice).color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	super.agrega(elemento);
	VerticeRojinegro v = verticeRojinegro(ultimoAgregado);
	pintaRojo(v);
	rebalanceaAgregado(v);	
    }

    private void rebalanceaAgregado(VerticeRojinegro v){
	// Caso 1
	if(!v.hayPadre()){
	    pintaNegro(v);
	    return;
	}
	VerticeRojinegro padre = verticeRojinegro(v.padre);
	// caso 2
	if(getColor(padre) == Color.NEGRO)
	    return;
	VerticeRojinegro abuelo = verticeRojinegro(padre.padre);
	VerticeRojinegro tio = getTio(v);
	// caso 3
	if(tio != null && getColor(tio) == Color.ROJO){
	    pintaNegro(padre);
	    pintaNegro(tio);
	    pintaRojo(abuelo);
	    rebalanceaAgregado(abuelo);
	    return;
	}
	//caso 4
	if(estanCruzados(v, padre)){
	    if(v.padre.izquierdo == v)
		super.giraDerecha(padre);
	    else
		super.giraIzquierda(padre);
	    VerticeRojinegro v1 = v;
	    v = padre;
	    padre = v1;
	}	
	//caso 5
	pintaNegro(padre);
	pintaRojo(abuelo);
	if(v.padre.izquierdo == v)
	    super.giraDerecha(abuelo);
	else
	    super.giraIzquierda(abuelo);	    
	return;
    }
    
    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
	VerticeRojinegro v = verticeRojinegro(super.busca(elemento));
	if(v == null)
	    return;
	elementos --;
	if(v.izquierdo != null){
	    VerticeRojinegro m = verticeRojinegro(maximoEnSubarbol(v.izquierdo));
	    VerticeRojinegro v1 = v;
	    v.elemento = m.elemento;
	    v = m;					  
	}
	if(v.izquierdo == null && v.derecho == null)
	     agregaFantasma(v);
	VerticeRojinegro hijo = getHijo(v);
	sube(hijo, v);
	if(esNegro(v) && esNegro(hijo))
	    rebalanceaElimina(hijo);
	else
	    pintaNegro(hijo);
	if(hijo.elemento == null)
	    if(hijo.padre == null)
		raiz = null;
	    else
		if(hijo.padre.izquierdo == hijo)
		    hijo.padre.izquierdo = null;
		else
		    hijo.padre.derecho = null;   
    }
    
    private void rebalanceaElimina(VerticeRojinegro v){
	//caso 1
	if(v.padre == null)
	    return;
	VerticeRojinegro padre = verticeRojinegro(v.padre);
	VerticeRojinegro hermano = getHermano(v);
	//caso 2
	if(esRojo(hermano)){
	    pintaNegro(hermano);
	    pintaRojo(padre);
	    if(v.padre.izquierdo == v)
		super.giraIzquierda(padre);
	    else
		super.giraDerecha(padre);
	    padre = verticeRojinegro(v.padre);
	    hermano = getHermano(v);
	}
	//caso 3
	VerticeRojinegro sobrinoI = verticeRojinegro(hermano.izquierdo);
	VerticeRojinegro sobrinoD = verticeRojinegro(hermano.derecho);
	if(esNegro(padre) && esNegro(hermano) && esNegro(sobrinoI) &&
	   esNegro(sobrinoD)){
	    pintaRojo(hermano);
	    rebalanceaElimina(padre);
	    return;
	}
	//caso 4
	if(esRojo(padre)  && esNegro(hermano) && esNegro(sobrinoI) &&
	   esNegro(sobrinoD)){
	    pintaNegro(padre);
	    pintaRojo(hermano);
	    return;
	}
	//caso 5
	if(v.padre.izquierdo == v && esNegro(sobrinoD) &&
	   esRojo(sobrinoI)){
	    pintaRojo(hermano);
	    pintaNegro(sobrinoI);
	    super.giraDerecha(hermano);
	}
	if(v.padre.derecho == v && esRojo(sobrinoD)  &&
	   esNegro(sobrinoI)){
	    pintaRojo(hermano);
	    pintaNegro(sobrinoD);
	    super.giraIzquierda(hermano);
	}
	hermano = getHermano(v);
	sobrinoI = verticeRojinegro(hermano.izquierdo);
	sobrinoD = verticeRojinegro(hermano.derecho);
	//caso 6
	if(esNegro(padre))
	    pintaNegro(hermano);
	else{
	    pintaRojo(hermano);
	    pintaNegro(padre);
	}
	if(v.padre.izquierdo == v){
	    pintaNegro(sobrinoD);
	    super.giraIzquierda(padre);
	}else{
	    pintaNegro(sobrinoI);
	    super.giraDerecha(padre);
	}
    }
    
    private void pintaRojo(VerticeRojinegro v){;
	v.color = Color.ROJO;
    }
    
    private void pintaNegro(VerticeRojinegro v){
	v.color = Color.NEGRO;	
    }
    private boolean esNegro(VerticeRojinegro v){
	return v == null || getColor(v) == Color.NEGRO;
    }
    private boolean esRojo(VerticeRojinegro v){
	return v != null && getColor(v) == Color.ROJO;
    }
   private VerticeRojinegro getHermano(VerticeRojinegro v){
	if(v.padre.izquierdo == v)
	    return verticeRojinegro(v.padre.derecho);
	return verticeRojinegro(v.padre.izquierdo);      
   }


   private VerticeRojinegro getTio(VerticeRojinegro v){
       if(!v.hayPadre())
	   return null;
       else
	   return getHermano(verticeRojinegro(v.padre));
       
    }
    
    private VerticeRojinegro getHijo(VerticeRojinegro v){
	return (v.hayIzquierdo())? verticeRojinegro(v.izquierdo):verticeRojinegro(v.derecho);
    }
    private boolean estanCruzados(VerticeRojinegro v, VerticeRojinegro v1){
	if(v.padre.izquierdo == v && v1.padre.derecho == v1)
 	    return true;
	if(v.padre.derecho == v && v1.padre.izquierdo == v1)
	    return true;
 	return false;
    }

    private void agregaFantasma(VerticeRojinegro v){
	if(v.izquierdo == null){
	    VerticeRojinegro ghost = verticeRojinegro(v.izquierdo);
	    ghost = verticeRojinegro(nuevoVertice(null));
	    ghost.color = Color.NEGRO;
	    ghost.padre = v;
	    v.izquierdo = ghost;
	}
	return;
    }
    private void sube(VerticeRojinegro v, VerticeRojinegro v2){
	v.padre = v2.padre;
	if(v2.padre == null)
	    raiz = v;
	else
	    if(v2.padre.derecho == v2)
		v2.padre.derecho = v;
	    else
		v2.padre.izquierdo = v;	
    }

    private void cazaFantasma(VerticeRojinegro v){
	if(v != null)
	    if(v.padre == null)
		raiz = ultimoAgregado = v = null;
	    else
		if(v.padre.izquierdo == v)
		    v.padre.izquierdo = null;
		else
		    v.padre.derecho = null;
    }
  	
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
