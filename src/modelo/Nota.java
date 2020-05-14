/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Comparator;

/**
 *
 * @author migue
 */
public class Nota {
    
    private String tema, contenido;
    private int id, idArticulo, idLibro, idUser;

    public Nota() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    

    public Nota(String tema, String contenido) {
        this.tema = tema;
        this.contenido = contenido;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Nota{" + "tema=" + tema + ", contenido=" + contenido + ", id=" + id + ", idArticulo=" + idArticulo + ", idLibro=" + idLibro + ", idUser=" + idUser + '}';
    }
    
    public static Comparator<Nota> temaComparator = (Nota n1, Nota n2) -> {
        String titulo1 = n1.getTema().toUpperCase();
        String titulo2 = n2.getTema().toUpperCase();
        
        return titulo1.compareTo(titulo2);
    };
}
