package controlador;

import static controlador.InicioControlador.icon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import modelo.Articulo;
import modelo.ArticuloConexion;
import modelo.Libro;
import modelo.LibroConexion;
import modelo.Nota;
import modelo.NotaConexion;
import vista.NuevaNotaVista;

/**
 * NuevaNotaControlador.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class NuevaNotaControlador implements ActionListener {

    //Declaración de objetos necesarios
    NuevaNotaVista vista;
    Nota nota = new Nota();

    NotaConexion notaConn = new NotaConexion();
    Libro libro = new Libro();
    LibroConexion libroConn = new LibroConexion();
    Articulo articulo = new Articulo();
    ArticuloConexion articuloConn = new ArticuloConexion();

    public boolean fromLibro = false;
    public boolean fromArticulo = false;

    public enum AccionMVC {
        __INTRODUCIR_NOTA,
        __CANCELAR
    }

    /**
     * Constructor de la clase NuevaNotaControlador
     *
     * @param vista Instancia de clase interfaz
     *
     */
    public NuevaNotaControlador(NuevaNotaVista vista) {
        this.vista = vista;
    }

    /**
     * Inicia el skin y las diferentes variables que se utilizan
     */
    public void iniciar() {
        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this.vista);
            this.vista.setIconImage(icon.getImage());
            HomeControlador.vista.setEnabled(false);
            this.vista.setVisible(true);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
        }

        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__INTRODUCIR_NOTA.setActionCommand("__INTRODUCIR_NOTA");
        this.vista.__INTRODUCIR_NOTA.addActionListener(this);

        this.vista.__CANCELAR.setActionCommand("__CANCELAR");
        this.vista.__CANCELAR.addActionListener(this);

    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __INTRODUCIR_NOTA:

                //Si un campo obligatorio esta vacio salta un mensaje
                if (this.vista.temaBox.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");
                } else {

                    //Si viene del panel Libros coloca al atributo idLibro la id del objeto libro relacionado
                    if (fromLibro) {

                        nota.setId(0);
                        nota.setTema(this.vista.temaBox.getText());

                        if (this.vista.contenidoArea.getText().isEmpty()) {
                            nota.setContenido(null);
                        } else {
                            nota.setContenido(this.vista.contenidoArea.getText());
                        }

                        nota.setIdLibro(libroConn.getId(LibroControlador.isbn));
                        nota.setIdArticulo(0);

                        //Si el metodo insert retorna true muestra un mensaje de exito, si no muestra error
                        if (notaConn.insert(nota)) {
                            JOptionPane.showMessageDialog(null, "Nota introducida con exito.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Ha habido un error.");
                        }
                        fromLibro = false;
                    }

                    //Si viene del panel Articulos coloca al atributo idArticulo la id del objeto articulo relacionado
                    if (fromArticulo) {

                        nota.setId(0);
                        nota.setTema(this.vista.temaBox.getText());
                        if (this.vista.contenidoArea.getText().isEmpty()) {
                            nota.setContenido(null);
                        } else {
                            nota.setContenido(this.vista.contenidoArea.getText());
                        }

                        nota.setIdLibro(0);
                        nota.setIdArticulo(articuloConn.getId(ArticuloControlador.issn));

                        if (notaConn.insert(nota)) {
                            JOptionPane.showMessageDialog(null, "Nota introducida con exito.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Ha habido un error.");
                        }
                        fromArticulo = false;
                    }
                    //Limpia las cajas de texto y cierra la ventana
                    clean();
                    this.vista.dispose();
                    HomeControlador.vista.toFront();
                    HomeControlador.vista.setEnabled(true);

                }

                break;
            case __CANCELAR:

                //Limpia las cajas de texto y cierra la ventana
                clean();

                fromLibro = false;
                fromArticulo = false;

                this.vista.dispose();
                HomeControlador.vista.toFront();
                HomeControlador.vista.setEnabled(true);
        }
    }

    /**
     * Pone en blanco todas las cajas de texto
     */
    private void clean() {
        this.vista.temaBox.setText("");
        this.vista.contenidoArea.setText("");
    }
}
