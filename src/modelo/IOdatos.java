/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import com.google.gson.Gson;
import conexion.Conexion;

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
 *
 * @author migue
 */
public class IOdatos {

    public List<Libro> lecturaLibro(String ruta) {

        String json = "";
        List<Libro> lista = new ArrayList<>();
        Gson gson = new Gson();

        try ( BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                json += linea;
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        Libro[] al = gson.fromJson(json, Libro[].class);

        lista.addAll(Arrays.asList(al));

        return lista;
    }

    public void escrituraLibro(List<Libro> lista, String ruta) {

        Gson gson = new Gson();
        String json = "[";

        for (Libro libro : lista) {
            json += gson.toJson(libro);
            if (lista.indexOf(libro) < lista.size() - 1) {
                json += ",";
            }
        }

        json += "]";

        FileWriter fw = null;

        try {
            fw = new FileWriter(ruta);
        } catch (IOException ex) {
            Logger.getLogger(IOdatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        try ( BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(json);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(IOdatos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public List<Articulo> lecturaArticulo(String ruta) {

        String json = "";
        List<Articulo> lista = new ArrayList<>();
        Gson gson = new Gson();

        try ( BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                json += linea;
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        Articulo[] a = gson.fromJson(json, Articulo[].class);

        lista.addAll(Arrays.asList(a));

        return lista;
    }

    public void escrituraArticulo(List<Articulo> lista, String ruta) {

        Gson gson = new Gson();
        String json = "[";

        for (Articulo articulo : lista) {
            json += gson.toJson(articulo);
            if (lista.indexOf(articulo) < lista.size() - 1) {
                json += ",";
            }
        }

        json += "]";

        FileWriter fw = null;

        try {
            fw = new FileWriter(ruta);
        } catch (IOException ex) {
            Logger.getLogger(IOdatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        try ( BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(json);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(IOdatos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public List<Nota> lecturaNota(String ruta) {

        String json = "";
        List<Nota> lista = new ArrayList<>();
        Gson gson = new Gson();

        try ( BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                json += linea;
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        Nota[] n = gson.fromJson(json, Nota[].class);

        lista.addAll(Arrays.asList(n));
        
        return lista;
    }

    public void escrituraNota(List<Nota> lista, String ruta) {

        Gson gson = new Gson();

        String json = "[";

        for (Nota nota : lista) {
            json += gson.toJson(nota);
            if (lista.indexOf(nota) < lista.size() - 1) {
                json += ",";
            }
        }

        json += "]";

        FileWriter fw = null;

        try {
            fw = new FileWriter(ruta);
        } catch (IOException ex) {
            Logger.getLogger(IOdatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        try ( BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(json);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(IOdatos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
