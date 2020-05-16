package modelo;

import java.util.Comparator;

/**
 * Nota.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class Nota {

    private String tema, contenido;
    private int id, idArticulo, idLibro, idUser;

    /**
     * Constructor vacio
     */
    public Nota() {
    }

    /**
     * Contructor por parametros
     *
     * @param tema
     * @param contenido
     * @param id
     * @param idArticulo
     * @param idLibro
     * @param idUser
     */
    public Nota(String tema, String contenido, int id, int idArticulo, int idLibro, int idUser) {
        this.tema = tema;
        this.contenido = contenido;
        this.id = id;
        this.idArticulo = idArticulo;
        this.idLibro = idLibro;
        this.idUser = idUser;
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

    /**
     * Metodo que compara los temas de una nota
     */
    public static Comparator<Nota> temaComparator = (Nota n1, Nota n2) -> {
        String titulo1 = n1.getTema().toUpperCase();
        String titulo2 = n2.getTema().toUpperCase();

        return titulo1.compareTo(titulo2);
    };
}
