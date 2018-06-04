package mx.unam.ciencias.edd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Práctica 5: Bosque generador.
 */
public class practica5 {

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

    public static   Lista<String>  BFSaristas(Grafica<String> g){

              Lista<String> aristas=new Lista<String>();
              g.paraCadaVertice((v) -> v.setColor(Color.ROJO));
              Cola<VerticeGrafica<String>> c = new Cola<VerticeGrafica<String>>();
              Lista<VerticeGrafica<String>> ELista = new Lista<VerticeGrafica<String>>();
              VerticeGrafica<String> vertice = g.vertice(v);
              vertice.setColor(Color.NEGRO);
              c.mete(vertice);
              while (!c.esVacia()) {
                  vertice = c.saca();
                  ELista.agrega(vertice);
                  String va = vertice.getElemento();
                  for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                      if (vecino.getColor() == Color.ROJO) {
                          vecino.setColor(Color.NEGRO);
                          String ve = vecino.getElemento();
                          String union=ve+va;
                          aristas.agrega(union);
                          //System.out.println(va +","+ ve);

                          c.mete(vecino);

                      }
                  }
              }
              return aristas;
    }

    public static   Lista<String>  BFSvertices(Grafica<String> g){

              g.paraCadaVertice((v) -> v.setColor(Color.ROJO));
              Cola<VerticeGrafica<String>> c = new Cola<VerticeGrafica<String>>();
              Lista<String> ELista = new Lista<String>();
              VerticeGrafica<String> vertice = g.vertice(v);
              vertice.setColor(Color.NEGRO);
              c.mete(vertice);
              while (!c.esVacia()) {
                  vertice = c.saca();
                  ELista.agrega(vertice.getElemento());
                  for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                      if (vecino.getColor() == Color.ROJO) {
                          vecino.setColor(Color.NEGRO);
                          c.mete(vecino);

                      }
                  }
              }
              return  ELista;
    }

    public static   Lista<String>  DFSaristas(Grafica<String> g){

              Lista<String> aristas=new Lista<String>();
              g.paraCadaVertice((v) -> v.setColor(Color.ROJO));
              Pila<VerticeGrafica<String>> p = new Pila<VerticeGrafica<String>>();
              Lista<VerticeGrafica<String>> ELista = new Lista<VerticeGrafica<String>>();
              VerticeGrafica<String> vertice = g.vertice(v);
              vertice.setColor(Color.NEGRO);
              p.mete(vertice);
              while (!p.esVacia()) {
                  vertice = p.saca();
                  ELista.agrega(vertice);
                  String va = vertice.getElemento();
                  for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                      if (vecino.getColor() == Color.ROJO) {
                          vecino.setColor(Color.NEGRO);
                          String ve = vecino.getElemento();
                          String union=ve+va;
                          aristas.agrega(union);
                          //System.out.println(va +","+ ve);

                          p.mete(vecino);

                      }
                  }
              }
              return aristas;
    }


    public static   Lista<String>  DFSvertices(Grafica<String> g){

              g.paraCadaVertice((v) -> v.setColor(Color.ROJO));
              Pila<VerticeGrafica<String>> p = new Pila<VerticeGrafica<String>>();
              Lista<String> ELista = new Lista<String>();
              VerticeGrafica<String> vertice = g.vertice(v);
              vertice.setColor(Color.NEGRO);
              p.mete(vertice);
              while (!p.esVacia()) {
                  vertice = p.saca();
                  ELista.agrega(vertice.getElemento());
                  for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                      if (vecino.getColor() == Color.ROJO) {
                          vecino.setColor(Color.NEGRO);
                          p.mete(vecino);

                      }
                  }
              }
              return  ELista;
    }




    public static   Lista<String>  eliminaVertices(Lista<String> original, Lista<String> sub){
      for(String s:sub){
        original.elimina(s);
      }
      return original;
    }





    public static void main(String[] args) {


      Grafica<String> r = new Grafica<String>();

      if (args.length != 2)
            uso();

        String nombreArchivo = args[0];
        Grafica<String> g = new Grafica<String>();
        g = CreaGrafica(nombreArchivo);


        /*Crear una lista de vertices la caul tendra todos los vertices de G original
        y se llena*/
        Lista<String> vertices_Original= new Lista<String>(); //Lita con todos los vertices de G
        for(String s: g){
          vertices_Original.agrega(s);
        }

        System.out.println("\n Estos son les vertices originales de G1: "+vertices_Original.toString());




        /*Lista vertices tendra los vertice de cada bosque generado por DFS o BFS*
        Lista aristas tendra las aristas de cada bosque generado por DFS o BFS*/
        Lista<Lista<String>> vertices_bosque= new Lista<Lista<String>>();
        Lista<Lista<String>> aristas_bosque= new  Lista<Lista<String>> ();

        /*Ejecutar DFS o BFS*/
        if(args[1].equals("BFS")){

          System.out.println(  " \n Con BFS: "+args[1]);

          /*Lista vertices tendra los vertice de cada arbol generado por DFS o BFS*
          Lista aristas tendra las aristas de cada arbol generado por DFS o BFS*/
          Lista<String> vertices= new Lista<String>();
          Lista<String> aristas= new Lista<String>();

          int b=1;

          while(g.getElementos()!=0){
            v=vertices_Original.get(0);
            vertices=BFSvertices(g);
            aristas =BFSaristas(g);
            vertices_bosque.agrega(vertices);
            aristas_bosque.agrega(aristas);
            vertices_Original=eliminaVertices(vertices_Original,vertices);
          //  System.out.println(v);
            for(String s:vertices){
              g.elimina(s);
            }

          }

          System.out.println("\n Los vertices del bosque son "+vertices_bosque.toString());
          System.out.println("Los aristas del bosque son "+aristas_bosque.toString());

          int i=1;
          for (Lista<String> v : vertices_bosque) {

            System.out.println("\nEl arbol "+i + " tiene "+ v.toString() + " vertices");
            System.out.println("y tiene "+aristas_bosque.get(i-1)+" aristas");

            i++;

          }
        }




        else if(args[1].equals("DFS")){

          System.out.println("Ested ejecuta: "+args[1]);

          /*Lista vertices tendra los vertice de cada arbol generado por DFS o BFS*
          Lista aristas tendra las aristas de cada arbol generado por DFS o BFS*/
          Lista<String> vertices= new Lista<String>();
          Lista<String> aristas= new Lista<String>();

          int b=1;

          while(g.getElementos()!=0){
            v=vertices_Original.get(0);
            vertices=DFSvertices(g);
            aristas =DFSaristas(g);
            vertices_bosque.agrega(vertices);
            aristas_bosque.agrega(aristas);
            vertices_Original=eliminaVertices(vertices_Original,vertices);
          //  System.out.println(v);
            for(String s:vertices){
              g.elimina(s);
            }

          }

          System.out.println("\n Los vertices del bosque son "+vertices_bosque.toString());
          System.out.println("Los aristas del bosque son "+aristas_bosque.toString());

          int i=1;
          for (Lista<String> v : vertices_bosque) {

            System.out.println("\nEl arbol "+i + " tiene "+ v.toString() + " vertices");
            System.out.println("y tiene "+aristas_bosque.get(i-1)+" aristas");

            i++;

          }



        }

    }
}
