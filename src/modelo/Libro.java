package modelo;

import java.util.Comparator;

/**
 * Libro.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class Libro {

    private long ISBN;
    private String autor, titulo, editorial;
    private int anio, nPaginas, idUser;

    /**
     * Constructor vacio
     */
    public Libro() {
    }

    /**
     * Contructor por parametros
     *
     * @param ISBN
     * @param autor
     * @param titulo
     */
    public Libro(long ISBN, String autor, String titulo) {
        this.ISBN = ISBN;
        this.autor = autor;
        this.titulo = titulo;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getnPaginas() {
        return nPaginas;
    }

    public void setnPaginas(int nPaginas) {
        this.nPaginas = nPaginas;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Libro{" + "ISBN=" + ISBN + ", autor=" + autor + ", titulo=" + titulo + ", editorial=" + editorial + ", anio=" + anio + ", nPaginas=" + nPaginas + ", idUser=" + idUser + '}';
    }

    /**
     * Metodo que compara los titulos de un libro
     */
    public static Comparator<Libro> tituloComparator = (Libro l1, Libro l2) -> {
        String titulo1 = l1.getTitulo().toUpperCase();
        String titulo2 = l2.getTitulo().toUpperCase();

        return titulo1.compareTo(titulo2);
    };
}
