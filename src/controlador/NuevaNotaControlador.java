/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import modelo.Articulo;
import modelo.ArticuloJDBC;
import modelo.Libro;
import modelo.LibroJDBC;
import modelo.Nota;
import modelo.NotaJDBC;
import vista.ArticuloVista;
import vista.LibroVista;
import vista.NuevaNotaVista;

/**
 *
 * @author migue
 */
public class NuevaNotaControlador implements ActionListener {

    /**
     * instancia a nuestra interfaz de usuario
     */
    NuevaNotaVista vista;
    Nota nota = new Nota();
    NotaJDBC notaConn = new NotaJDBC();
    Libro libro = new Libro();
    LibroJDBC libroConn = new LibroJDBC();
    Articulo articulo = new Articulo();
    ArticuloJDBC articuloConn = new ArticuloJDBC();

    /**
     * instancia a nuestro modelo
     */
    public boolean fromLibro = false;
    public boolean fromArticulo = false;

    /**
     * Se declaran en un ENUM las acciones que se realizan desde la interfaz de
     * usuario VISTA y posterior ejecución desde el controlador
     */
    public enum AccionMVC {
        __INTRODUCIR_NOTA,
        __CANCELAR
    }

    /**
     * Constrcutor de clase
     *
     * @param vista Instancia de clase interfaz
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
            SwingUtilities.updateComponentTreeUI(vista);

            vista.setVisible(true);
            HomeControlador.vista.setEnabled(false);
            if (HomeControlador.mLib != null) {
            HomeControlador.mLib.vista.setEnabled(false);
            }
            if (HomeControlador.mArt != null) {
            HomeControlador.mArt.vista.setEnabled(false);
            }
            if (HomeControlador.mNot != null) {
                HomeControlador.mNot.vista.setEnabled(false);
            }

        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
        }

        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__INTRODUCIR_NOTA.setActionCommand("__INTRODUCIR_NOTA");
        this.vista.__INTRODUCIR_NOTA.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__CANCELAR.setActionCommand("__CANCELAR");
        this.vista.__CANCELAR.addActionListener(this);

    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __INTRODUCIR_NOTA:
                if (this.vista.temaBox.getText().length() == 0 || this.vista.contenidoArea.getText().length() == 0) {

                    JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");
                } else {
                    if (fromLibro) {

                        nota.setId(0);
                        nota.setTema(this.vista.temaBox.getText());
                        nota.setContenido(this.vista.contenidoArea.getText());
                        nota.setIdLibro(libroConn.getId(LibroVista.isbnBox.getText()));
                        nota.setIdArticulo(0);

                        notaConn.insert(nota);
                        fromLibro = false;
                    }
                    if (fromArticulo) {

                        nota.setId(0);
                        nota.setTema(this.vista.temaBox.getText());
                        nota.setContenido(this.vista.contenidoArea.getText());
                        nota.setIdLibro(0);
                        nota.setIdArticulo(articuloConn.getId(ArticuloVista.issnBox.getText()));

                        notaConn.insert(nota);
                        fromArticulo = false;
                    }
                    clean();
                    this.vista.dispose();
                    HomeControlador.vista.toFront();
                    HomeControlador.vista.setEnabled(true);
                    if (HomeControlador.mLib != null){
                    HomeControlador.mLib.vista.toFront();
                    HomeControlador.mLib.vista.setEnabled(true);
                    }
                    if (HomeControlador.mArt != null){
                    HomeControlador.mArt.vista.toFront();
                    HomeControlador.mArt.vista.setEnabled(true);
                    }
                    if (HomeControlador.mNot != null){
                    HomeControlador.mNot.vista.toFront();
                    HomeControlador.mNot.vista.setEnabled(true);
                    }
                    
                }

                break;
            case __CANCELAR:
                clean();
                this.vista.dispose();
                HomeControlador.vista.toFront();
                HomeControlador.vista.setEnabled(true);
                if (HomeControlador.mLib != null){
                HomeControlador.mLib.vista.toFront();
                HomeControlador.mLib.vista.setEnabled(true);
                }
                if (HomeControlador.mArt != null){
                HomeControlador.mArt.vista.toFront();
                HomeControlador.mArt.vista.setEnabled(true);
                }
                if (HomeControlador.mNot != null){
                HomeControlador.mNot.vista.toFront();
                HomeControlador.mNot.vista.setEnabled(true);
                }
        }
    }

    private void clean() {

        this.vista.temaBox.setText("");
        this.vista.contenidoArea.setText("");
    }
}
