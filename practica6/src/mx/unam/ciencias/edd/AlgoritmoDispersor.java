package mx.unam.ciencias.edd;

/**
 * Enumeración para los distintos algoritmos disponibles para dispersores.
 */
public enum AlgoritmoDispersor {
    /** Algoritmo de Bob Jenkins para cadenas. */
    BJ_STRING,
    /** Algoritmo de GLib para cadenas. */
    GLIB_STRING,
    /** Algoritmo de XOR para cadenas. */
    XOR_STRING;
}
