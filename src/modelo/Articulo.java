package modelo;

import java.util.Comparator;

/**
 * Articulo.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class Articulo {

    private int ISSN;
    private String autor, titulo, revista;
    private int anio, mes, pagInicio, pagFin, idUser;

    /**
     * Constructor vacio
     */
    public Articulo() {
    }

    /**
     * Contructor por parametros
     *
     * @param ISSN
     * @param autor
     * @param titulo
     * @param revista
     */
    public Articulo(int ISSN, String autor, String titulo, String revista) {
        this.ISSN = ISSN;
        this.autor = autor;
        this.titulo = titulo;
        this.revista = revista;
    }

    public int getISSN() {
        return ISSN;
    }

    public void setISSN(int ISSN) {
        this.ISSN = ISSN;
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

    public String getRevista() {
        return revista;
    }

    public void setRevista(String revista) {
        this.revista = revista;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getPagInicio() {
        return pagInicio;
    }

    public void setPagInicio(int pagInicio) {
        this.pagInicio = pagInicio;
    }

    public int getPagFin() {
        return pagFin;
    }

    public void setPagFin(int pagFin) {
        this.pagFin = pagFin;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Articulo{" + "ISSN=" + ISSN + ", autor=" + autor + ", titulo=" + titulo + ", revista=" + revista + ", anio=" + anio + ", mes=" + mes + ", pagInicio=" + pagInicio + ", pagFin=" + pagFin + ", idUser=" + idUser + '}';
    }

    /**
     * Metodo que compara los titulos de un articulo
     */
    public static Comparator<Articulo> tituloComparator = (Articulo a1, Articulo a2) -> {
        String titulo1 = a1.getTitulo().toUpperCase();
        String titulo2 = a2.getTitulo().toUpperCase();

        return titulo1.compareTo(titulo2);
    };

}
