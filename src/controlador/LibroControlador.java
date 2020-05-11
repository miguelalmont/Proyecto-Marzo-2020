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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import modelo.IOdatos;
import modelo.Libro;
import modelo.LibroJDBC;
import vista.HomeVista;
import vista.NuevaNotaVista;

/**
 *
 * @author migue
 */
public class LibroControlador implements ActionListener, MouseListener {

    /**
     * instancia a nuestra interfaz de usuario
     */
    public JPanel panel;
    public HomeVista vista;

    public static NuevaNotaControlador nuevaNota;
    /**
     * instancia a nuestro modelo
     */
    LibroJDBC libroConn = new LibroJDBC();
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
        __ANIADIR_NOTA_LIBRO,
        __GUARDAR_TABLA_LIBRO,
        __CARGAR_TABLA_LIBRO,
        __VOLVER_LIBRO,
        __BUSCAR_LIBRO,
        __ACTUALIZAR_TABLA_LIBROS
    }

    /**
     * Constrcutor de clase
     *
     * @param panel Instancia de clase interfaz
     */
    public LibroControlador(JPanel panel) {
        this.vista = HomeControlador.vista;
        this.panel = panel;
    }

    /**
     * Inicia el skin y las diferentes variables que se utilizan
     */
    public void iniciar() {
        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(vista);

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

        this.vista.__ANIADIR_NOTA_LIBRO.setActionCommand("__ANIADIR_NOTA_LIBRO");
        this.vista.__ANIADIR_NOTA_LIBRO.addActionListener(this);

        this.vista.__GUARDAR_TABLA_LIBRO.setActionCommand("__GUARDAR_TABLA_LIBRO");
        this.vista.__GUARDAR_TABLA_LIBRO.addActionListener(this);

        this.vista.__CARGAR_TABLA_LIBRO.setActionCommand("__CARGAR_TABLA_LIBRO");
        this.vista.__CARGAR_TABLA_LIBRO.addActionListener(this);

        this.vista.__BUSCAR_LIBRO.setActionCommand("__BUSCAR_LIBRO");
        this.vista.__BUSCAR_LIBRO.addActionListener(this);

        this.vista.__VOLVER_LIBRO.setActionCommand("__VOLVER_LIBRO");
        this.vista.__VOLVER_LIBRO.addActionListener(this);
        
        this.vista.__ACTUALIZAR_TABLA_LIBROS.setActionCommand("__ACTUALIZAR_TABLA_LIBROS");
        this.vista.__ACTUALIZAR_TABLA_LIBROS.addActionListener(this);

        //añade e inicia el jtable
        this.vista.__tabla_libros.addMouseListener(this);
        this.vista.__tabla_libros.setModel(crearTabla(libroConn.select()));

        this.vista.isbnCheckBox.addActionListener((ActionEvent event) -> {
            JCheckBox cb = (JCheckBox) event.getSource();
            if (cb.isSelected()) {
                HomeVista.isbnLibroBox.setEnabled(true);
            } else {
                HomeVista.isbnLibroBox.setEnabled(false);
            }
        });
        
        this.vista.isbnCheckBox.setSelected(true);
    }

    //Eventos que suceden por el mouse
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1)//boton izquierdo
        {
            int fila = this.vista.__tabla_libros.rowAtPoint(e.getPoint());
            if (fila > -1) {
                HomeVista.isbnLibroBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 0)));
                this.vista.tituloLibroBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 1)));
                this.vista.autorLibroBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 2)));
                this.vista.editorialLibroBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 3)));
                this.vista.anioLibroFormatedBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 4)));
                this.vista.nPaginasLibroFormatedBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 5)));
                if (this.vista.isbnCheckBox.isSelected()) {
                    HomeVista.isbnLibroBox.setEnabled(false);
                    this.vista.isbnCheckBox.setSelected(false);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __NUEVO_LIBRO:

                if (HomeVista.isbnLibroBox.getText().isEmpty() || 
                        this.vista.tituloLibroBox.getText().isEmpty() || 
                        this.vista.autorLibroBox.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Los campos ISBN, Titulo y Atutor no pueden estar vacios.");
                } else {

                    if (libroConn.existeISBN(HomeVista.isbnLibroBox.getText()) == 0) {

                        Libro libro = new Libro();

                        libro.setISBN(HomeVista.isbnLibroBox.getText());
                        libro.setTitulo(this.vista.tituloLibroBox.getText());
                        libro.setAutor(this.vista.autorLibroBox.getText());
                        libro.setEditorial(this.vista.editorialLibroBox.getText());
                        if(this.vista.anioLibroFormatedBox.getText().isEmpty())
                            libro.setAnio(0);
                        else
                            libro.setAnio(Integer.parseInt(this.vista.anioLibroFormatedBox.getText()));
                        if(this.vista.nPaginasLibroFormatedBox.getText().isEmpty())
                            libro.setnPaginas(0);
                        else
                            libro.setnPaginas(Integer.parseInt(this.vista.nPaginasLibroFormatedBox.getText()));
                        libroConn.insert(libro);
                        this.vista.__tabla_libros.setModel(crearTabla(libroConn.select()));
                        clean();
                    } else {
                        JOptionPane.showMessageDialog(null, "El ISBN ya existe.");
                    }
                }
                break;

            case __MODIFICAR_LIBRO:
                
                if (HomeVista.isbnLibroBox.getText().isEmpty() || 
                        this.vista.tituloLibroBox.getText().isEmpty() || 
                        this.vista.autorLibroBox.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Los campos ISBN, Titulo y Atutor no pueden estar vacios.");
                }
                else{
                    if (libroConn.existeISBN(HomeVista.isbnLibroBox.getText()) > 0) {

                        Libro libro = new Libro();

                        libro.setISBN(HomeVista.isbnLibroBox.getText());
                        libro.setTitulo(this.vista.tituloLibroBox.getText());
                        libro.setAutor(this.vista.autorLibroBox.getText());
                        libro.setEditorial(this.vista.editorialLibroBox.getText());
                        if(this.vista.anioLibroFormatedBox.getText().length() == 0) {
                            libro.setAnio(0);
                        }
                        else {
                            int anio = Integer.parseInt(this.vista.anioLibroFormatedBox.getText());
                            libro.setAnio(anio);
                        }
                        if(this.vista.nPaginasLibroFormatedBox.getText().length() == 0){
                            libro.setAnio(0);
                        }
                        else {
                            int pags = Integer.parseInt(this.vista.nPaginasLibroFormatedBox.getText());
                            libro.setnPaginas(pags);
                        }
                        libroConn.update(libro, HomeVista.isbnLibroBox.getText());

                        clean();
                        this.vista.__tabla_libros.setModel(crearTabla(libroConn.select()));
                    } else {
                        JOptionPane.showMessageDialog(null, "El ISBN introducido no esta registrado.");
                    }
                }
                break;

            case __ELIMINAR_LIBRO:

                if (libroConn.existeISBN(HomeVista.isbnLibroBox.getText()) > 0) {
                    libroConn.delete(HomeVista.isbnLibroBox.getText());

                    this.vista.__tabla_libros.setModel(crearTabla(libroConn.select()));
                } else {
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un ISBN valido.");
                }
                break;

            case __ANIADIR_NOTA_LIBRO:

                if (libroConn.existeISBN(HomeVista.isbnLibroBox.getText()) > 0) {

                    nuevaNota = new NuevaNotaControlador(new NuevaNotaVista());
                    nuevaNota.fromLibro = true;
                    nuevaNota.iniciar();

                } else {

                    JOptionPane.showMessageDialog(null, "Debes seleccionar un ISBN valido.");

                }

                break;

            case __BUSCAR_LIBRO:
                
                if (this.vista.busquedaLibroBox.getText().isEmpty()){}
                else{
                    if (libroConn.coincidencias(this.vista.busquedaLibroBox.getText()) > 0)
                        this.vista.__tabla_libros.setModel(crearTabla(libroConn.buscar(this.vista.busquedaLibroBox.getText())));
                    else
                        JOptionPane.showMessageDialog(null, "Busquedas sin resultados.");
                }
                break;
            case __GUARDAR_TABLA_LIBRO:

                JFileChooser fileChooser = new JFileChooser();
                int seleccion = fileChooser.showSaveDialog(this.vista);
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        io.escrituraLibro(getContenidoTabla(), fichero.getAbsolutePath());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ha habido un error.");
                    }
                }

                break;
            case __CARGAR_TABLA_LIBRO:

                fileChooser = new JFileChooser();
                seleccion = fileChooser.showOpenDialog(this.vista);
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        this.vista.__tabla_libros.setModel(crearTabla(io.lecturaLibro(fichero.getAbsolutePath())));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar un archivo valido.");
                    }

                }

                break;
            case __ACTUALIZAR_TABLA_LIBROS:
                
                this.vista.__tabla_libros.setModel(crearTabla(libroConn.select()));
                
                break;
            case __VOLVER_LIBRO:

                int option = JOptionPane.showConfirmDialog(null,
                        "¿Estás seguro de que quieres cerrar la sesion?",
                        "Cierre de sesion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    this.vista.dispose();
                    LoginControlador.vista.dispose();
                    InicioControlador.vista.setEnabled(true);
                    InicioControlador.vista.setVisible(true);
                }

                break;

        }

    }

    public void clean() {
        HomeVista.isbnLibroBox.setText("");
        this.vista.tituloLibroBox.setText("");
        this.vista.autorLibroBox.setText("");
        this.vista.editorialLibroBox.setText("");
        this.vista.anioLibroFormatedBox.setText("");
        this.vista.nPaginasLibroFormatedBox.setText("");
    }

    public DefaultTableModel crearTabla(List<Libro> lista) {
        this.vista.__tabla_libros.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ISBN", "Titulo", "Autor", "Editorial", "Año", "Nº Pags", "Usuario"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        TableColumnModel tcm = this.vista.__tabla_libros.getColumnModel();

        DefaultTableModel modelo = (DefaultTableModel) this.vista.__tabla_libros.getModel();

        String[] columNames = {"ISBN", "Titulo", "Autor", "Editorial", "Año", "Nº Pags", "Usuario"};

        Object[][] fila = new Object[lista.size()][7];
        int i = 0;

        for (Libro libro : lista) {
            fila[i][0] = libro.getISBN();
            fila[i][1] = libro.getTitulo();
            fila[i][2] = libro.getAutor();
            if (libro.getEditorial() == null)
                fila[i][3] = "";
            else
                fila[i][3] = libro.getEditorial();
            if(libro.getAnio() == 0)
                fila[i][4] = "";
            else
                fila[i][4] = libro.getAnio();
            if(libro.getnPaginas() == 0)
                fila[i][5] = "";
            else
                fila[i][5] = libro.getnPaginas();
            fila[i][6] = libro.getIdUser();
            i++;

        }

        modelo.setDataVector(fila, columNames);

        tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("Usuario")));

        return modelo;
    }

    public List<Libro> getContenidoTabla() {

        List<Libro> lista = new ArrayList<>();

        for (int fila = 0; fila < this.vista.__tabla_libros.getRowCount(); fila++) {
            Libro libro = new Libro();

            libro.setISBN(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 0)));
            libro.setTitulo(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 1)));
            libro.setAutor(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 2)));
            libro.setEditorial(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 3)));
            libro.setAnio((int) this.vista.__tabla_libros.getValueAt(fila, 4));
            libro.setnPaginas((int) this.vista.__tabla_libros.getValueAt(fila, 5));
            libro.setIdUser((int) this.vista.__tabla_libros.getModel().getValueAt(fila, 6));

            lista.add(libro);
        }

        return lista;
    }

}
