/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author migue
 */
public class NuevaNotaControlador implements ActionListener {

    /**
     * instancia a nuestra interfaz de usuario
     */
    NuevaNotaVista vista;
    Nota nota = new Nota();
    NotaConexion notaConn = new NotaConexion();
    Libro libro = new Libro();
    LibroConexion libroConn = new LibroConexion();
    Articulo articulo = new Articulo();
    ArticuloConexion articuloConn = new ArticuloConexion();

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
            SwingUtilities.updateComponentTreeUI(this.vista);
            this.vista.setIconImage(icon.getImage());
            HomeControlador.vista.setEnabled(false);
            this.vista.setVisible(true);
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
                if (this.vista.temaBox.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");
                } else {
                    if (fromLibro) {

                        nota.setId(0);
                        nota.setTema(this.vista.temaBox.getText());
                        
                        if (this.vista.contenidoArea.getText().isEmpty())
                            nota.setContenido(null);
                        else   
                            nota.setContenido(this.vista.contenidoArea.getText());
                        
                        nota.setIdLibro(libroConn.getId(LibroControlador.isbn));
                        nota.setIdArticulo(0);

                        if (notaConn.insert(nota)) {
                            JOptionPane.showMessageDialog(null, "Nota introducida con exito.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Ha habido un error.");
                        }
                        fromLibro = false;
                    }
                    if (fromArticulo) {

                        nota.setId(0);
                        nota.setTema(this.vista.temaBox.getText());
                        if (this.vista.contenidoArea.getText().isEmpty())
                            nota.setContenido(null);
                        else
                            nota.setContenido(this.vista.contenidoArea.getText());
                        
                        nota.setIdLibro(0);
                        nota.setIdArticulo(articuloConn.getId(ArticuloControlador.issn));

                        if(notaConn.insert(nota)){
                            JOptionPane.showMessageDialog(null, "Nota introducida con exito.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Ha habido un error.");
                        }
                        fromArticulo = false;
                    }
                    clean();
                    this.vista.dispose();
                    HomeControlador.vista.toFront();
                    HomeControlador.vista.setEnabled(true);

                }

                break;
            case __CANCELAR:
                clean();
                
                fromLibro = false;
                fromArticulo = false;
                
                this.vista.dispose();
                HomeControlador.vista.toFront();
                HomeControlador.vista.setEnabled(true);
        }
    }

    private void clean() {
        this.vista.temaBox.setText("");
        this.vista.contenidoArea.setText("");
    }
}
