/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import modelo.IOdatos;
import modelo.Libro;
import modelo.LibrosJDBC;
import vista.LibroVista;
import vista.NuevaNotaVista;

/**
 *
 * @author migue
 */
public class LibroControlador implements ActionListener, MouseListener {

    /**
     * instancia a nuestra interfaz de usuario
     */
    public LibroVista vista;
    public static NuevaNotaControlador nuevaNota;
    /**
     * instancia a nuestro modelo
     */
    LibrosJDBC librosConn = new LibrosJDBC();
    IOdatos io = new IOdatos();
    public static FileChooserControlador fcc = null;

    /**
     * Se declaran en un ENUM las acciones que se realizan desde la interfaz de
     * usuario VISTA y posterior ejecución desde el controlador
     */
    public enum AccionMVC {
        __NUEVO_LIBRO,
        __MODIFICAR_LIBRO,
        __ELIMINAR_LIBRO,
        __ANIADIR_NOTA,
        __GUARDAR_TABLA,
        __CARGAR_TABLA,
        __VOLVER,
        __BUSCAR
    }

    /**
     * Constrcutor de clase
     *
     * @param vista Instancia de clase interfaz
     */
    public LibroControlador(LibroVista vista) {
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

        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
        }

        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__NUEVO_LIBRO.setActionCommand("__NUEVO_LIBRO");
        this.vista.__NUEVO_LIBRO.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__MODIFICAR_LIBRO.setActionCommand("__MODIFICAR_LIBRO");
        this.vista.__MODIFICAR_LIBRO.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__ELIMINAR_LIBRO.setActionCommand("__ELIMINAR_LIBRO");
        this.vista.__ELIMINAR_LIBRO.addActionListener(this);

        this.vista.__ANIADIR_NOTA.setActionCommand("__ANIADIR_NOTA");
        this.vista.__ANIADIR_NOTA.addActionListener(this);

        this.vista.__GUARDAR_TABLA.setActionCommand("__GUARDAR_TABLA");
        this.vista.__GUARDAR_TABLA.addActionListener(this);

        this.vista.__CARGAR_TABLA.setActionCommand("__CARGAR_TABLA");
        this.vista.__CARGAR_TABLA.addActionListener(this);

        this.vista.__BUSCAR.setActionCommand("__BUSCAR");
        this.vista.__BUSCAR.addActionListener(this);

        this.vista.__VOLVER.setActionCommand("__VOLVER");
        this.vista.__VOLVER.addActionListener(this);

        //añade e inicia el jtable
        LibroVista.__tabla_libros.addMouseListener(this);
        LibroVista.__tabla_libros.setModel(crearTabla(librosConn.select()));

        LibroVista.isbnCheckBox.addActionListener((ActionEvent event) -> {
            JCheckBox cb = (JCheckBox) event.getSource();
            if (cb.isSelected()) {
                LibroVista.isbnBox.setEnabled(true);
            } else {
                LibroVista.isbnBox.setEnabled(false);
            }
        });
    }

    //Eventos que suceden por el mouse
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1)//boton izquierdo
        {
            int fila = LibroVista.__tabla_libros.rowAtPoint(e.getPoint());
            if (fila > -1) {
                LibroVista.isbnBox.setText(String.valueOf(LibroVista.__tabla_libros.getValueAt(fila, 0)));
                this.vista.tituloBox.setText(String.valueOf(LibroVista.__tabla_libros.getValueAt(fila, 1)));
                this.vista.autorBox.setText(String.valueOf(LibroVista.__tabla_libros.getValueAt(fila, 2)));
                this.vista.editorialBox.setText(String.valueOf(LibroVista.__tabla_libros.getValueAt(fila, 3)));
                this.vista.anioFormatedBox.setText(String.valueOf(LibroVista.__tabla_libros.getValueAt(fila, 4)));
                this.vista.nPaginasFormatedBox.setText(String.valueOf(LibroVista.__tabla_libros.getValueAt(fila, 5)));
                if (LibroVista.isbnCheckBox.isSelected()) {
                    LibroVista.isbnBox.setEnabled(false);
                    LibroVista.isbnCheckBox.setSelected(false);
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __NUEVO_LIBRO:

                if (LibroVista.isbnBox.getText().length() == 0 || this.vista.tituloBox.getText().length() == 0 || this.vista.autorBox.getText().length() == 0 || this.vista.editorialBox.getText().length() == 0 || this.vista.anioFormatedBox.getText().length() == 0 || this.vista.nPaginasFormatedBox.getText().length() == 0) {

                    JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");
                } else {

                    if (librosConn.existeISBN(LibroVista.isbnBox.getText()) == 0) {

                        Libro libro = new Libro();

                        libro.setISBN(LibroVista.isbnBox.getText());
                        libro.setTitulo(this.vista.tituloBox.getText());
                        libro.setAutor(this.vista.autorBox.getText());
                        libro.setEditorial(this.vista.editorialBox.getText());
                        libro.setAnio(Integer.parseInt(this.vista.anioFormatedBox.getText()));
                        libro.setnPaginas(Integer.parseInt(this.vista.nPaginasFormatedBox.getText()));

                        librosConn.insert(libro);
                        LibroVista.__tabla_libros.setModel(crearTabla(librosConn.select()));
                        clean();
                    } else {
                        JOptionPane.showMessageDialog(null, "El ISBN ya existe.");
                    }
                }
                break;

            case __MODIFICAR_LIBRO:

                if (librosConn.existeISBN(LibroVista.isbnBox.getText()) > 0) {

                    Libro libro = new Libro();

                    libro.setISBN(LibroVista.isbnBox.getText());
                    libro.setTitulo(this.vista.tituloBox.getText());
                    libro.setAutor(this.vista.autorBox.getText());
                    libro.setEditorial(this.vista.editorialBox.getText());

                    int anio = Integer.parseInt(this.vista.anioFormatedBox.getText());
                    libro.setAnio(anio);

                    int pags = Integer.parseInt(this.vista.nPaginasFormatedBox.getText());
                    libro.setnPaginas(pags);

                    librosConn.update(libro, LibroVista.isbnBox.getText());

                    clean();
                    LibroVista.__tabla_libros.setModel(crearTabla(librosConn.select()));
                } else {
                    JOptionPane.showMessageDialog(null, "El ISBN introducido no esta registrado.");
                }
                break;

            case __ELIMINAR_LIBRO:

                if (librosConn.existeISBN(LibroVista.isbnBox.getText()) > 0) {
                    librosConn.delete(LibroVista.isbnBox.getText());

                    LibroVista.__tabla_libros.setModel(crearTabla(librosConn.select()));
                } else {
                    JOptionPane.showMessageDialog(null, "El ISBN introducido no esta registrado.");
                }
                break;

            case __ANIADIR_NOTA:

                if (librosConn.existeISBN(LibroVista.isbnBox.getText()) > 0) {

                    nuevaNota = new NuevaNotaControlador(new NuevaNotaVista());
                    nuevaNota.fromLibro = true;
                    nuevaNota.iniciar();

                } else {

                    JOptionPane.showMessageDialog(null, "Debes seleccionar un ISBN valido.");

                }

                break;

            case __BUSCAR:

                if (librosConn.coincidencias(LibroVista.busquedaBox.getText()) > 0) {

                    LibroVista.__tabla_libros.setModel(crearTabla(librosConn.buscar(LibroVista.busquedaBox.getText())));

                } else {

                    JOptionPane.showMessageDialog(null, "La busqueda no ha sido satisfactoria.");
                    LibroVista.__tabla_libros.setModel(crearTabla(librosConn.buscar(LibroVista.busquedaBox.getText())));
                }

                break;
            case __GUARDAR_TABLA:

                JFileChooser fileChooser = new JFileChooser();
                int seleccion = fileChooser.showSaveDialog(this.vista);
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        io.escrituraArchivoLibros(getContenidoTabla(), fichero.getAbsolutePath());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ha habido un error.");
                    }
                }

                break;
            case __CARGAR_TABLA:

                fileChooser = new JFileChooser();
                seleccion = fileChooser.showOpenDialog(this.vista);
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        LibroVista.__tabla_libros.setModel(crearTabla(io.lecturaArchivoLibros(fichero.getAbsolutePath())));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar un archivo valido.");
                    }

                }

                break;
            case __VOLVER:

                this.vista.dispose();
                HomeControlador.mLib = null;
                break;

        }

    }

    public void clean() {
        LibroVista.isbnBox.setText("");
        this.vista.tituloBox.setText("");
        this.vista.autorBox.setText("");
        this.vista.editorialBox.setText("");
        this.vista.anioFormatedBox.setText("");
        this.vista.nPaginasFormatedBox.setText("");
    }

    public DefaultTableModel crearTabla(List<Libro> lista) {

        LibrosJDBC librosConn = new LibrosJDBC();

        LibroVista.__tabla_libros.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ISBN", "Titulo", "Autor", "Editorial", "Año", "Nº Pags"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        DefaultTableModel modelo = (DefaultTableModel) LibroVista.__tabla_libros.getModel();

        String[] columNames = {"ISBN", "Titulo", "Autor", "Editorial", "Año", "Nº Pags"};

        if (librosConn.cuentaLibros() < 1) {
            //JOptionPane.showMessageDialog(null, "La lista de libros esta vacia.");  
        } else {

            Object[][] fila = new Object[lista.size()][6];
            int i = 0;

            for (Libro libro : lista) {
                fila[i][0] = libro.getISBN();
                fila[i][1] = libro.getTitulo();
                fila[i][2] = libro.getAutor();
                fila[i][3] = libro.getEditorial();
                fila[i][4] = libro.getAnio();
                fila[i][5] = libro.getnPaginas();
                i++;

            }

            modelo.setDataVector(fila, columNames);
        }

        return modelo;
    }

    public List<Libro> getContenidoTabla() {

        List<Libro> listalibros = new ArrayList<>();

        for (int fila = 0; fila < LibroVista.__tabla_libros.getRowCount(); fila++) {
            Libro libro = new Libro();

            libro.setISBN(String.valueOf(LibroVista.__tabla_libros.getValueAt(fila, 0)));
            libro.setTitulo(String.valueOf(LibroVista.__tabla_libros.getValueAt(fila, 1)));
            libro.setAutor(String.valueOf(LibroVista.__tabla_libros.getValueAt(fila, 2)));
            libro.setEditorial(String.valueOf(LibroVista.__tabla_libros.getValueAt(fila, 3)));
            libro.setAnio((int) LibroVista.__tabla_libros.getValueAt(fila, 4));
            libro.setnPaginas((int) LibroVista.__tabla_libros.getValueAt(fila, 5));

            listalibros.add(libro);
        }

        return listalibros;
    }

}
