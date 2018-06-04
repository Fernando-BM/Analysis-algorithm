package mx.unam.ciencias.edd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Práctica 9: Trayectoria mínima y algoritmo de Dijkstra.
 */
public class practica1 {

    private static String cadena;

    private static String v;
  /*  private static Lista<VerticeGrafica<String>> ELista2 = new Lista<VerticeGrafica<String>>();*/





    private static void uso() {
        System.err.println("El uso es: java -jar practica8.jar <Nombre Archivo>");
        System.exit(1);
    }

    public static Grafica<String> CreaGrafica(String archivo){
      Grafica<String> g = new Grafica<String>();
      String cadena;

    try {

      FileReader f = new FileReader(archivo);
      BufferedReader b = new BufferedReader(f);

      String primeraLinea=b.readLine();
      String[] partsAux = primeraLinea.split(", |,");
      v = partsAux[0];

      /*ZONE TRIKI*/


      for(String s: partsAux){
        g.agrega(s);
      }

      /*for(String ss: partsAux){
        VerticeGrafica<String>  verticeLista = g.vertice(ss);
        ELista2.agrega(verticeLista);

      }Habilitar par inconexas*/


      while((cadena = b.readLine())!=null) {
        String[] parts = cadena.split(", |,");
        if(cadena.length()<=5){
            String part1 = parts[0];
            String part2 = parts[1];
            g.conecta(part1,part2);

        }

      }
      b.close();
    }catch (IOException ioe) {
      System.out.printf("No pude cargar del archivo \"%s\".\n",
      archivo);
      System.exit(1);
    }
      return g;
  }


    @SuppressWarnings("unchecked")
    public static Lista<VerticeGrafica<String>> MinRecursivo(Lista<VerticeGrafica<String>> original, VerticeGrafica<String> vertice, Grafica f){

      Lista<VerticeGrafica<String>> copiat= new Lista<VerticeGrafica<String>>();

      for(VerticeGrafica<String> elem: original){

        if (f.sonVecinos( vertice.getElemento(), elem.getElemento()) == false){
          copiat.agrega(elem);
        }
      }

      return copiat;

    }





    public static void main(String[] args) {



      if (args.length != 1)
            uso();

        String nombreArchivo = args[0];

        Grafica<String> g = new Grafica<String>();
        g = CreaGrafica(nombreArchivo);



        /* BFS */
        g.paraCadaVertice((v) -> v.setColor(Color.ROJO));

        Cola<VerticeGrafica<String>> c = new Cola<VerticeGrafica<String>>();

        Lista<VerticeGrafica<String>> ELista = new Lista<VerticeGrafica<String>>();


        VerticeGrafica<String> vertice = g.vertice(v);


        vertice.setColor(Color.NEGRO);
        c.mete(vertice);
        cadena = "BFS 1: ";
        while (!c.esVacia()) {
            vertice = c.saca();
            ELista.agrega(vertice);
            for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                if (vecino.getColor() == Color.ROJO) {
                    vecino.setColor(Color.NEGRO);
                    c.mete(vecino);
                }
            }
        }


        vertice = g.vertice(v);


        /* Conjunto minimo*/
        Lista<VerticeGrafica<String> > ConMinimo = new Lista<VerticeGrafica<String> >();
        ConMinimo=MinRecursivo(ELista, vertice,g);






      Lista<VerticeGrafica<String> > ConMinimoFinal = new Lista<VerticeGrafica<String> >();


      for(VerticeGrafica<String> eleme: ConMinimo){
                  ConMinimoFinal=MinRecursivo(ConMinimo, eleme,g);
      }



      String s="El Conjunto minimo son los vertices: ";
      for(VerticeGrafica<String> elem: ConMinimoFinal){
          s+= elem.getElemento() + " ";
      }
      System.out.println(s);













    }
}
