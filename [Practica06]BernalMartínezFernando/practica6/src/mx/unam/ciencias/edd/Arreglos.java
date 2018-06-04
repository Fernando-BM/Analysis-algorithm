package mx.unam.ciencias.edd;

/**
 * Clase para manipular arreglos genéricos de elementos comparables.
 */
public class Arreglos {

    /**
     * Ordena el arreglo recibido usando QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void quickSort(T[] a) {
	quickSort(a, 0, a.length - 1);  
	
    }
    private static <T extends Comparable<T>> void quickSort(T[] a, int ini,  int fin){
	if(fin<=ini)
	    return;
	int i = ini + 1;
	int j = fin;
	while(i < j)
	    if(a[ini].compareTo(a[i])<0 && a[j].compareTo(a[ini])<=0)
		intercambia(a, i++, j--);
	    else if(a[i].compareTo(a[ini])<=0)
		i++;
	    else
		j--;
	if(a[ini].compareTo(a[i])<0)
	    i--;
	intercambia(a, i, ini);
	quickSort(a, ini, i-1);
	quickSort(a, i+1, fin);
    }
    
    private static <T extends Comparable<T>> void intercambia(T[] a, int i, int j){
	if( i == j) 
	    return;
	T b = a[j];
	a[j] = a[i];
	a[i] = b;
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] a) {
        for(int i = 0; i < a.length; i++){
	    int p = i;
	    for(int j = i + 1; j < a.length; j++)
		if(a[j].compareTo(a[p])<0)
		    p = j;
	    intercambia(a, i, p);
	}
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param a el arreglo dónde buscar.
     * @param e el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int busquedaBinaria(T[] a, T e) {
        return busquedaBinaria(a, 0, a.length - 1, e);
    }
    
    private static <T extends Comparable<T>>  int busquedaBinaria(T[] a, int ini, int fin, T p){
	if(fin < ini)
	    return -1;
	int m = (ini + fin)/2;
	if(a[m] == p)
	    return m;
	return (p.compareTo(a[m])<0)? busquedaBinaria(a, ini, m-1, p): busquedaBinaria(a, m+1, fin, p); 
    }

}
