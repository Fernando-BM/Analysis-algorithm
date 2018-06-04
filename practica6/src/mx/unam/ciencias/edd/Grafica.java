package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase privada para iteradores de gráficas. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Grafica<T>.Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
	    iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
	    return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
	    Vertice v = iterador.next();
	    T e = v.elemento;
	    return e;
        }

        /* No lo implementamos: siempre lanza una excepción. */
        @Override public void remove() {
            throw new UnsupportedOperationException("Eliminar con el iterador " +
                                                    "no está soportado");
        }
    }

    /* Vecinos para gráficas; un vecino es un vértice y el peso de la arista que
     * los une. Implementan VerticeGrafica. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vecino del vértice. */
        public Grafica<T>.Vertice vecino;
        /* El peso de vecino conectando al vértice con el vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Grafica<T>.Vertice vecino, double peso) {
            this.vecino = vecino;
	    this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T getElemento() {
	    return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return vecino.getColor();
        }

        /* Define el color del vecino. */
        @Override public void setColor(Color color) { 
	    vecino.setColor(color);
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos;
        }
    }

    /* Vertices para gráficas; implementan la interfaz ComparableIndexable y
     * VerticeGrafica */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Lista<Grafica<T>.Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            vecinos = new Lista<>();
	    color = Color.NINGUNO;
	    this.elemento = elemento;
        }

        /* Regresa el elemento del vértice. */
        @Override public T getElemento() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Define el color del vértice. */
        @Override public void setColor(Color color) {
            this.color = color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
	    return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
	     if(this.distancia == vertice.distancia)
		return 0;
	    if(this.distancia < vertice.distancia)
		return -1;
	    else
	    return 1; 
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<>();
	aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
	return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
	if(contiene(elemento) || elemento == null)
	    throw new IllegalArgumentException();
	Vertice v = new Vertice(elemento);
	vertices.agrega(v);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        if(!contiene(a) || !contiene(b))
	    throw new NoSuchElementException();
	if(sonVecinos(a, b) || a.equals(b))
	   throw new IllegalArgumentException();
	Vertice x = (Vertice) vertice(a);
        Vertice y = (Vertice) vertice(b);
	y.vecinos.agrega(new Vecino(x, 1));
	x.vecinos.agrega(new Vecino(y, 1));
	aristas++;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
	 if(!contiene(a) || !contiene(b))
	   throw new NoSuchElementException(); 
	if(a.equals(b) || sonVecinos(a,b) || peso <= 0)
	    throw new IllegalArgumentException();
	Vertice x = (Vertice) vertice(a);
	Vertice y = (Vertice) vertice(b);
	Vecino av = new Vecino(x, peso);
	Vecino bv = new Vecino(y, peso);
	x.vecinos.agrega(bv);
	y.vecinos.agrega(av);
	aristas++; 
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
	if(!contiene(a) || !contiene(b))
	   throw new NoSuchElementException(); 
	if(!sonVecinos(a,b)|| a.equals(b))
	    throw new IllegalArgumentException();
	Vertice x = (Vertice) vertice(a);
	Vertice y = (Vertice) vertice(b);
	Vecino vx = null;
	Vecino vy = null;
	for(Vecino z: x.vecinos)
	    if(z.vecino.equals(y)){
		vx = z;
		x.vecinos.elimina(vx);
	    }
	for(Vecino w : y.vecinos)
	    if(w.equals(x)){
		vy = w;
		y.vecinos.elimina(vy);
	    }
	aristas --;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for(Vertice v : vertices)
	    if(v.elemento.equals(elemento))
		return true;
	return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
	if(!contiene(elemento))
	    throw new NoSuchElementException();
	Vertice v = (Vertice) vertice(elemento);
	for(Vertice v1 : vertices)
	    for(Vecino v2 : v1.vecinos)
		if(v2.vecino.equals(v)){
		    v1.vecinos.elimina(v2);
		    aristas --;
		}
	vertices.elimina(v);   
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        if(!contiene(a) || !contiene(b))
	    throw new NoSuchElementException();
	Vertice x = (Vertice) vertice(a);
	Vertice y = (Vertice) vertice(b);
	for(Vecino z : x.vecinos)
	    if(z.vecino.equals(y))
		return true;
	return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        if(!contiene(a) || !contiene(b))
	    throw new NoSuchElementException();
	if(!sonVecinos(a,b))
	    throw new IllegalArgumentException();
	Vertice x = (Vertice) vertice(a);
	Vertice y = (Vertice) vertice(b);
	for(Vecino v : x.vecinos)
	    if(v.vecino.equals(y))
		return v.peso;
	return -1;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        for(Vertice v : vertices)
	    if(v.elemento.equals(elemento))
		return v;
	throw new NoSuchElementException();
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice v : vertices)
	    accion.actua(v);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
	    if(!this.contiene(elemento))
	    throw new NoSuchElementException();
	Vertice r = (Vertice) vertice(elemento);
	Cola<Grafica<T>.Vertice> c = new Cola<>();
	c.mete(r);
	while(!c.esVacia()){
	    Vertice a = c.saca();
	    a.setColor(Color.ROJO);
	    accion.actua(a);
	    for(Vecino v : a.vecinos){
		if(v.vecino.color == Color.ROJO)
		    continue;
		v.vecino.color = Color.ROJO;
		c.mete(v.vecino);
	    }
	}
	for(Vertice vv : vertices)
	    vv.color = Color.NINGUNO;	
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
	if(!this.contiene(elemento))
	    throw new NoSuchElementException();
	Vertice r = (Vertice) vertice(elemento);
	Pila<Grafica<T>.Vertice> p = new Pila<>();
	p.mete(r);
	while(!p.esVacia()){
	    Vertice a = p.saca();
	    a.setColor(Color.ROJO);
	    accion.actua(a);
	    for(Vecino v : a.vecinos){
		if(v.vecino.color == Color.ROJO)
		    continue;
		v.vecino.color = Color.ROJO;
		p.mete(v.vecino);
	    }
	}
	for(Vertice vv : vertices)
	    vv.color = Color.NINGUNO;
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacio() {
        return vertices.getLongitud() == 0;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        if(!contiene(origen) || !contiene(destino))
	    throw new NoSuchElementException();
	Vertice a = (Vertice) vertice(origen);
	Vertice b = (Vertice) vertice(destino);
	hazInfinitosTm(a);
	trayectoriaMinima(a);
	Lista<VerticeGrafica<T>> tm = listaTrayectoriaMinima(a,b);
	return tm;
    }
    
    private void hazInfinitosTm(Vertice origen){
	for(Vertice v : vertices)
	    v.distancia = -1;
	origen.distancia = 0;
    }
    
    private void hazInfinitosDijkstra(Vertice vo) {
	for (Vertice v : vertices)
            v.distancia = Double.POSITIVE_INFINITY;
	vo.distancia = 0;
    }
    
    private void trayectoriaMinima(Vertice o) {
    	Cola<Vertice> c = new Cola<>();
	c.mete(o);
	while(!c.esVacia()) {
	    o = c.saca();
	    for(Vecino v : o.vecinos) {
		if(v.vecino.distancia == -1){
		    v.vecino.distancia = o.distancia + 1;
		    c.mete(v.vecino);
		}
	    }
	}
    }
    
    private Lista<VerticeGrafica<T>> listaTrayectoriaMinima(Vertice origen, Vertice destino){
	Lista<VerticeGrafica<T>> l = new Lista<>();
	if(destino.distancia != -1){
	    Vertice v = destino;
	    while(v != origen){
		for(Vecino u : v.vecinos) {
		    if(v.distancia - 1 == u.vecino.distancia){
			l.agregaInicio(v);
			v = u.vecino;
			break;
		    }
		}
		if(v == origen)
		    l.agregaInicio(origen);
	    }
	}
	return l;
    }
    
    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
     public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
	if(!contiene(origen) || !contiene(destino))
	    throw new NoSuchElementException();
	Vertice x = (Vertice) vertice(origen);
	Vertice y = (Vertice) vertice(destino);
	hazInfinitosDijkstra(x);
	trayectoriaMinimaDijkstra();
	Lista<VerticeGrafica<T>> d = listaTrayectoriaMinimaDijkstra(x, y);
	return d;
    }
    
      private void trayectoriaMinimaDijkstra(){
	MonticuloMinimo<Vertice> h = new MonticuloMinimo<>(vertices);
	while(!h.esVacio()) {
	    Vertice v = h.elimina();
	    for(Vecino u : v.vecinos) {
		if(u.vecino.distancia == Double.POSITIVE_INFINITY || v.distancia + u.peso < u.vecino.distancia){
			u.vecino.distancia = v.distancia + u.peso;
			h.reordena(u.vecino);
		}
	    }
	}
      }
   

    private Lista<VerticeGrafica<T>> listaTrayectoriaMinimaDijkstra(Vertice origen, Vertice destino){
	Lista<VerticeGrafica<T>> l = new Lista<>();
	Vertice v = destino;
	while(v != origen){
	    for(Vecino u : v.vecinos) {
		if(v.distancia - u.peso == u.vecino.distancia){
		    l.agregaInicio(v);
		    v = u.vecino;
		    break;
		}
	    }
	    if(v == origen)
		l.agregaInicio(origen);
	}
	return l;
    }
}
