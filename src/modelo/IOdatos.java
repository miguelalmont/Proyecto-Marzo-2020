package modelo;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * IOdatos.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class IOdatos {

    /**
     * Guarda el contenido de un String en un fichero
     *
     * @param txt El String a guardar
     * @param ruta La ruta del fichero donde se va a guardar
     */
    public void exportarTxt(String txt, String ruta) {

        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write(txt);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(IOdatos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Lee un fichero json y devuelve el contenido en forma de coleccion
     *
     * @param ruta La ruta del fichero
     * @return Retorna un ArrayList de Libro
     */
    public List<Libro> lecturaLibro(String ruta) {

        String json = "";
        List<Libro> lista = new ArrayList<>();
        Gson gson = new Gson();

        //Lee el contenido del fichero por su ruta
        try ( BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;

            //Mientras haya lineas en el fichero, las añade al String
            while ((linea = br.readLine()) != null) {
                json += linea;
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        //Convierte el String json a objetos Libro y los inserta en un array
        Libro[] al = gson.fromJson(json, Libro[].class);

        //Añade el contenido del array a la coleccion
        lista.addAll(Arrays.asList(al));

        return lista;
    }

    /**
     * Transforma una coleccion de Libro a un String json y lo guarda en un
     * fichero
     *
     * @param lista La List de Libro a guardar
     * @param ruta La ruta del fichero donde se va a guardar
     */
    public void escrituraLibro(List<Libro> lista, String ruta) {

        Gson gson = new Gson();
        String json = "[";

        //Por cada libro en la coleccion, añade una cadena al String con su contenido en formato json
        for (Libro libro : lista) {
            json += gson.toJson(libro);
            //Si no es el ultimo libro, añade una coma
            if (lista.indexOf(libro) < lista.size() - 1) {
                json += ",";
            }
        }

        //Cierra el String
        json += "]";

        //Guarda el String en el fichero
        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write(json);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(IOdatos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Lee un fichero json y devuelve el contenido en forma de coleccion
     *
     * @param ruta La ruta del fichero
     * @return Retorna un ArrayList de Articulo
     */
    public List<Articulo> lecturaArticulo(String ruta) {

        String json = "";
        List<Articulo> lista = new ArrayList<>();
        Gson gson = new Gson();

        //Lee el contenido del fichero por su ruta
        try ( BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            //Mientras haya lineas en el fichero, las añade al String
            while ((linea = br.readLine()) != null) {
                json += linea;
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        //Convierte el String json a objetos Articulo y los inserta en un array
        Articulo[] a = gson.fromJson(json, Articulo[].class);

        //Añade el contenido del array a la coleccion
        lista.addAll(Arrays.asList(a));

        return lista;
    }

    /**
     * Transforma una coleccion de Articulo a un String json y lo guarda en un
     * fichero
     *
     * @param lista La List de Articulo a guardar
     * @param ruta La ruta del fichero donde se va a guardar
     */
    public void escrituraArticulo(List<Articulo> lista, String ruta) {

        Gson gson = new Gson();
        String json = "[";

        //Por cada articulo en la coleccion, añade una cadena al String con su contenido en formato json
        for (Articulo articulo : lista) {
            json += gson.toJson(articulo);
            //Si no es el ultimo articulo, añade una coma
            if (lista.indexOf(articulo) < lista.size() - 1) {
                json += ",";
            }
        }

        //Cierra el String
        json += "]";

        //Guarda el String en el fichero
        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write(json);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(IOdatos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Lee un fichero json y devuelve el contenido en forma de coleccion
     *
     * @param ruta La ruta del fichero
     * @return Retorna un ArrayList de Nota
     */
    public List<Nota> lecturaNota(String ruta) {

        String json = "";
        List<Nota> lista = new ArrayList<>();
        Gson gson = new Gson();

        //Lee el contenido del fichero por su ruta
        try ( BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            //Mientras haya lineas en el fichero, las añade al String
            while ((linea = br.readLine()) != null) {
                json += linea;
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        //Convierte el String json a objetos Articulo y los inserta en un array
        Nota[] n = gson.fromJson(json, Nota[].class);

        //Añade el contenido del array a la coleccion
        lista.addAll(Arrays.asList(n));

        return lista;
    }

    /**
     * Transforma una coleccion de Nota a un String json y lo guarda en un
     * fichero
     *
     * @param lista La List de Nota a guardar
     * @param ruta La ruta del fichero donde se va a guardar
     */
    public void escrituraNota(List<Nota> lista, String ruta) {

        Gson gson = new Gson();

        String json = "[";

        //Por cada nota en la coleccion, añade una cadena al String con su contenido en formato json
        for (Nota nota : lista) {
            json += gson.toJson(nota);
            //Si no es la ultima nota, añade una coma
            if (lista.indexOf(nota) < lista.size() - 1) {
                json += ",";
            }
        }

        //Cierra el String
        json += "]";

        //Guarda el String en el fichero
        try ( BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write(json);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(IOdatos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
