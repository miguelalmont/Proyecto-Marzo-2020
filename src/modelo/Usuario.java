package modelo;

/**
 * Usuario.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class Usuario {

    private int id;
    private String usuario, password, nombre, mail, lastSession;

    /**
     * Constructor vacio
     */
    public Usuario() {
    }

    /**
     * Constructor por parametros
     *
     * @param id
     * @param usuario
     * @param password
     * @param nombre
     * @param mail
     * @param lastSession
     */
    public Usuario(int id, String usuario, String password, String nombre, String mail, String lastSession) {
        this.id = id;
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.mail = mail;
        this.lastSession = lastSession;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLastSession() {
        return lastSession;
    }

    public void setLastSession(String last_session) {
        this.lastSession = last_session;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", usuario=" + usuario + ", password=" + password + ", nombre=" + nombre + ", mail=" + mail + ", lastSession=" + lastSession + '}';
    }

}
