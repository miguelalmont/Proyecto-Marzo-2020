package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
import modelo.LibroConexion;
import modelo.Nota;
import modelo.NotaConexion;
import vista.HomeVista;
import vista.NuevaNotaVista;

/**
 * LibroControlador.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class LibroControlador implements ActionListener, MouseListener {

    //Declaración de objetos y variables necesarios
    public JPanel panel;
    public HomeVista vista;
    public static NuevaNotaControlador nuevaNota;
    public static long isbn = 0;

    LibroConexion libroConn = new LibroConexion();
    NotaConexion notaConn = new NotaConexion();
    IOdatos io = new IOdatos();

    public enum AccionMVC {

        __NUEVO_LIBRO,
        __MODIFICAR_LIBRO,
        __ELIMINAR_LIBRO,
        __ANIADIR_NOTA_LIBRO,
        __LIMPIAR_LIBRO,
        __GUARDAR_TABLA_LIBRO,
        __CARGAR_TABLA_LIBRO,
        __VOLVER_LIBRO,
        __BUSCAR_LIBRO,
        __ACTUALIZAR_TABLA_LIBROS,
        __EXPORTAR_LIBRO
    }

    /**
     * Constructor de la clase LibroControlador
     *
     * @param panel Instancia de clase interfaz
     *
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

        this.vista.__MODIFICAR_LIBRO.setActionCommand("__MODIFICAR_LIBRO");
        this.vista.__MODIFICAR_LIBRO.addActionListener(this);

        this.vista.__ELIMINAR_LIBRO.setActionCommand("__ELIMINAR_LIBRO");
        this.vista.__ELIMINAR_LIBRO.addActionListener(this);

        this.vista.__ANIADIR_NOTA_LIBRO.setActionCommand("__ANIADIR_NOTA_LIBRO");
        this.vista.__ANIADIR_NOTA_LIBRO.addActionListener(this);

        this.vista.__LIMPIAR_LIBRO.setActionCommand("__LIMPIAR_LIBRO");
        this.vista.__LIMPIAR_LIBRO.addActionListener(this);

        this.vista.__GUARDAR_TABLA_LIBRO.setActionCommand("__GUARDAR_TABLA_LIBRO");
        this.vista.__GUARDAR_TABLA_LIBRO.addActionListener(this);

        this.vista.__CARGAR_TABLA_LIBRO.setActionCommand("__CARGAR_TABLA_LIBRO");
        this.vista.__CARGAR_TABLA_LIBRO.addActionListener(this);

        this.vista.__BUSCAR_LIBRO.setActionCommand("__BUSCAR_LIBRO");
        this.vista.__BUSCAR_LIBRO.addActionListener(this);

        this.vista.__VOLVER_LIBRO.setActionCommand("__VOLVER_LIBRO");
        this.vista.__VOLVER_LIBRO.addActionListener(this);

        this.vista.__EXPORTAR_LIBRO.setActionCommand("__EXPORTAR_LIBRO");
        this.vista.__EXPORTAR_LIBRO.addActionListener(this);

        this.vista.__ACTUALIZAR_TABLA_LIBROS.setActionCommand("__ACTUALIZAR_TABLA_LIBROS");
        this.vista.__ACTUALIZAR_TABLA_LIBROS.addActionListener(this);

        //Añade, define e inicia el jtable
        this.vista.__tabla_libros.addMouseListener(this);
        this.definirTabla();
        this.vista.__tabla_libros.setModel(setTabla(libroConn.select()));

        //Define el comportacmiento del checkBox
        this.vista.isbnCheckBox.addActionListener((ActionEvent event) -> {
            JCheckBox cb = (JCheckBox) event.getSource();
            if (cb.isSelected()) {
                HomeVista.isbn1LibroBox.setEnabled(true);
                HomeVista.isbn2LibroBox.setEnabled(true);
            } else {
                HomeVista.isbn1LibroBox.setEnabled(false);
                HomeVista.isbn2LibroBox.setEnabled(false);
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

                HomeVista.isbn1LibroBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 0).toString().substring(0, 3)));
                HomeVista.isbn2LibroBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 0).toString().substring(3)));
                this.vista.tituloLibroBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 1)));
                this.vista.autorLibroBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 2)));
                this.vista.editorialLibroBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 3)));
                this.vista.anioLibroFormatedBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 4)));
                this.vista.nPaginasLibroFormatedBox.setText(String.valueOf(this.vista.__tabla_libros.getValueAt(fila, 5)));
                if (this.vista.isbnCheckBox.isSelected()) {
                    HomeVista.isbn1LibroBox.setEnabled(false);
                    HomeVista.isbn2LibroBox.setEnabled(false);
                    this.vista.isbnCheckBox.setSelected(false);
                }
            }

            //Añade a la variable isbn el contenido de las cajas unidas
            String cadena = HomeVista.isbn1LibroBox.getText() + HomeVista.isbn2LibroBox.getText();
            LibroControlador.isbn = Long.parseLong(cadena);
        }
    }

    //No se usa
    @Override
    public void mousePressed(MouseEvent e) {
    }

    //No se usa
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    //No se usa
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    //No se usa
    @Override
    public void mouseExited(MouseEvent e) {
    }

    //Control de eventos de los controles que tienen definido un "ActionCommand"
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __NUEVO_LIBRO:

                //Si algun campo obligatorio esta vacio salta un mensaje
                if (HomeVista.isbn1LibroBox.getText().isEmpty()
                        || HomeVista.isbn1LibroBox.getText().isEmpty()
                        || this.vista.tituloLibroBox.getText().isEmpty()
                        || this.vista.autorLibroBox.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Los campos ISBN, Titulo y Atutor no pueden estar vacios.");
                } else {

                    //Añade a la variable isbn el contenido de las cajas unidas
                    String cadena = HomeVista.isbn1LibroBox.getText() + HomeVista.isbn2LibroBox.getText();
                    LibroControlador.isbn = Long.parseLong(cadena);

                    //Si el issn no existe crea un objeto articulo con los parametros de las cajas
                    if (libroConn.existeISBN(LibroControlador.isbn) == 0) {
                        Libro libro = new Libro();

                        libro.setISBN(LibroControlador.isbn);
                        libro.setTitulo(this.vista.tituloLibroBox.getText());
                        libro.setAutor(this.vista.autorLibroBox.getText());
                        libro.setEditorial(this.vista.editorialLibroBox.getText());
                        if (this.vista.anioLibroFormatedBox.getText().isEmpty()) {
                            libro.setAnio(0);
                        } else {
                            libro.setAnio(Integer.parseInt(this.vista.anioLibroFormatedBox.getText()));
                        }
                        if (this.vista.nPaginasLibroFormatedBox.getText().isEmpty()) {
                            libro.setnPaginas(0);
                        } else {
                            libro.setnPaginas(Integer.parseInt(this.vista.nPaginasLibroFormatedBox.getText()));
                        }

                        //Si el metodo insert retorna true muestra un mensaje de exito
                        if (libroConn.insert(libro)) {
                            JOptionPane.showMessageDialog(null, "Libro introducido con exito.");
                            this.vista.__tabla_libros.setModel(setTabla(libroConn.select()));
                            this.clean();
                        } else {
                            JOptionPane.showMessageDialog(null, "Ha habido un error.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "El ISBN ya existe.");
                    }
                }
                break;

            case __MODIFICAR_LIBRO:

                if (HomeVista.isbn1LibroBox.getText().isEmpty()
                        || HomeVista.isbn2LibroBox.getText().isEmpty()
                        || this.vista.tituloLibroBox.getText().isEmpty()
                        || this.vista.autorLibroBox.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Los campos ISBN, Titulo y Atutor no pueden estar vacios.");
                } else {
                    if (libroConn.existeISBN(LibroControlador.isbn) > 0) {

                        Libro libro = new Libro();

                        libro.setISBN(LibroControlador.isbn);
                        libro.setTitulo(this.vista.tituloLibroBox.getText());
                        libro.setAutor(this.vista.autorLibroBox.getText());
                        libro.setEditorial(this.vista.editorialLibroBox.getText());
                        if (this.vista.anioLibroFormatedBox.getText().length() == 0) {
                            libro.setAnio(0);
                        } else {
                            int anio = Integer.parseInt(this.vista.anioLibroFormatedBox.getText());
                            libro.setAnio(anio);
                        }
                        if (this.vista.nPaginasLibroFormatedBox.getText().length() == 0) {
                            libro.setAnio(0);
                        } else {
                            int pags = Integer.parseInt(this.vista.nPaginasLibroFormatedBox.getText());
                            libro.setnPaginas(pags);
                        }
                        if (libroConn.update(libro, LibroControlador.isbn)) {
                            JOptionPane.showMessageDialog(null, "Libro modificado con exito.");
                            this.vista.__tabla_libros.setModel(setTabla(libroConn.select()));
                        } else {
                            JOptionPane.showMessageDialog(null, "Ha habido un error.");
                        }
                        clean();

                    } else {
                        JOptionPane.showMessageDialog(null, "El ISBN introducido no esta registrado.");
                    }
                }
                break;

            case __ELIMINAR_LIBRO:

                //Si el isbn existe borra el registro, en otro caso muestra un mensaje de error
                if (libroConn.existeISBN(LibroControlador.isbn) > 0) {
                    if (libroConn.delete(LibroControlador.isbn)) {
                        JOptionPane.showMessageDialog(null, "Libro eliminado con exito.");
                        this.vista.__tabla_libros.setModel(setTabla(libroConn.select()));
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha habido un error.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debes seleccionar un ISBN valido.");
                }
                break;

            case __ANIADIR_NOTA_LIBRO:

                //Si el isbn existe llama a la ventana Nueva Nota
                if (libroConn.existeISBN(LibroControlador.isbn) > 0) {

                    nuevaNota = new NuevaNotaControlador(new NuevaNotaVista());
                    nuevaNota.fromLibro = true;
                    nuevaNota.iniciar();

                } else {

                    JOptionPane.showMessageDialog(null, "Debes seleccionar un ISBN valido.");

                }

                break;

            case __BUSCAR_LIBRO:
                //Si la caja de buscar registro esta vacia no hace nada
                if (this.vista.busquedaLibroBox.getText().isEmpty()) {
                } else {
                    if (!libroConn.buscar(this.vista.busquedaLibroBox.getText()).isEmpty()) {
                        this.vista.__tabla_libros.setModel(setTabla(libroConn.buscar(this.vista.busquedaLibroBox.getText())));
                    } else {
                        JOptionPane.showMessageDialog(null, "Busqueda sin resultados.");
                    }
                }
                break;
            case __LIMPIAR_LIBRO:
                this.clean();
                break;
            case __GUARDAR_TABLA_LIBRO:
                //Intancia un objeto JFileChooser
                JFileChooser fileChooser = new JFileChooser();
                int seleccion = fileChooser.showSaveDialog(this.vista);

                //Si selecciona un fichero y la ruta es valida, guarda el contenido de la tabla en ese fichero
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

                //Si selecciona un fichero y la ruta es valida, carga el contenido del fichero en la tabla
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        this.vista.__tabla_libros.setModel(setTabla(io.lecturaLibro(fichero.getAbsolutePath())));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar un archivo valido.");
                    }

                }

                break;
            case __EXPORTAR_LIBRO:

                fileChooser = new JFileChooser();
                seleccion = fileChooser.showSaveDialog(this.vista);

                //Si selecciona un fichero y la ruta es valida, guarda el contenido de la tabla en un txt
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        io.exportarTxt(generarTxt(getContenidoTabla()), fichero.getAbsolutePath());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar un archivo valido.");
                    }

                }

                break;
            case __ACTUALIZAR_TABLA_LIBROS:

                //Refresca el contenido de la tabla con todos los registros en la base de datos
                this.vista.__tabla_libros.setModel(setTabla(libroConn.select()));

                break;
            case __VOLVER_LIBRO:

                //Muestra mensaje de confirmación, si es positivo, cierra la sesion y vuelve a la pantalla de login
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

    /**
     * Pone en blanco todas las cajas de texto
     */
    public void clean() {
        HomeVista.isbn1LibroBox.setText("");
        HomeVista.isbn2LibroBox.setText("");

        //Si el chechBox no esta marcado, lo marca y habilita las cajas de isbn
        if (!this.vista.isbnCheckBox.isSelected()) {
            HomeVista.isbn1LibroBox.setEnabled(true);
            HomeVista.isbn2LibroBox.setEnabled(true);
            this.vista.isbnCheckBox.setSelected(true);
        }
        this.vista.tituloLibroBox.setText("");
        this.vista.autorLibroBox.setText("");
        this.vista.editorialLibroBox.setText("");
        this.vista.anioLibroFormatedBox.setText("");
        this.vista.nPaginasLibroFormatedBox.setText("");
    }

    /**
     * Define la tabla de libros
     */
    public void definirTabla() {
        this.vista.__tabla_libros.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ISBN", "Titulo", "Autor", "Editorial", "Año", "Nº Pags", "Usuario"
                }
        ) {
            //Define la clase de cada columna
            Class[] types = new Class[]{
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            //Niega la edicion de las columnas
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
    }

    /**
     * Introduce los atributos de una coleccion de libros en la tabla
     *
     * @param lista Entra por parametro un List de objetos Libro
     * @return Retorna el modelo de la tabla con contenido
     */
    public DefaultTableModel setTabla(List<Libro> lista) {

        //Ordena la lista por nombre
        Collections.sort(lista, Libro.tituloComparator);

        //Instancia un objeto para modificar columnas
        TableColumnModel tcm = this.vista.__tabla_libros.getColumnModel();

        //Instancia el modelo
        DefaultTableModel modelo = (DefaultTableModel) this.vista.__tabla_libros.getModel();

        String[] columNames = {"ISBN", "Titulo", "Autor", "Editorial", "Año", "Nº Pags", "Usuario"};

        //Instancia una matriz (numero de objetos)*(numero de columnas)
        Object[][] fila = new Object[lista.size()][7];
        int i = 0;

        //Recorre la lista e introduce el contenido en la matriz
        for (Libro libro : lista) {
            fila[i][0] = libro.getISBN();
            fila[i][1] = libro.getTitulo();
            fila[i][2] = libro.getAutor();
            if (libro.getEditorial() == null) {
                fila[i][3] = "";
            } else {
                fila[i][3] = libro.getEditorial();
            }
            if (libro.getAnio() == 0) {
                fila[i][4] = "";
            } else {
                fila[i][4] = libro.getAnio();
            }
            if (libro.getnPaginas() == 0) {
                fila[i][5] = "";
            } else {
                fila[i][5] = libro.getnPaginas();
            }
            fila[i][6] = libro.getIdUser();
            i++;

        }

        //Inserta el contenido de la matriz en el modelo
        modelo.setDataVector(fila, columNames);

        //Oculta una columna
        tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("Usuario")));

        return modelo;
    }

    /**
     * Devuelve el contenido de la tabla en forma de coleccion
     *
     * @return Retorna un ArrayList de Libro
     */
    public List<Libro> getContenidoTabla() {

        List<Libro> lista = new ArrayList<>();

        for (int fila = 0; fila < this.vista.__tabla_libros.getRowCount(); fila++) {
            Libro libro = new Libro();

            libro.setISBN((long) this.vista.__tabla_libros.getValueAt(fila, 0));
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

    /**
     * Genera un String de datos con formato a partir de una coleccion
     *
     * @param lista La coleccion de Libro que se va a introducir
     * @return El String con los datos de la coleccion
     */
    public String generarTxt(List<Libro> lista) {

        //Inicializa el String vacio;
        String contenido = "";

        //Inicializa el String que servira para el salto de linea
        String saltolinea = System.getProperty("line.separator");

        //Por cada libro en la lista añade los datos requeridos al String
        for (Libro libro : lista) {

            List<Nota> notas = notaConn.selectPorLibro(libroConn.getId(libro.getISBN()));

            contenido += libro.getTitulo();
            contenido += ", ";
            contenido += libro.getAutor();

            if (libro.getEditorial().isEmpty()) {
                contenido += ", ";
                contenido += libro.getEditorial();
            }
            if (libro.getAnio() > 0) {
                contenido += ", ";
                contenido += libro.getAnio();
            }
            if (libro.getnPaginas() > 0) {
                contenido += ", ";
                contenido += libro.getnPaginas();
            }

            //Si la lista de notas no esta vacía la recorre
            if (!notas.isEmpty()) {
                contenido += saltolinea;

                //Por cada nota en la lista añade los datos requeridos al String
                for (Nota nota : notas) {

                    contenido += "     " + nota.getTema();

                    if (!nota.getContenido().isEmpty()) {
                        contenido += ": ";
                        contenido += nota.getContenido();
                    }

                    contenido += saltolinea;
                }
            }
            contenido += saltolinea;
        }
        return contenido;
    }
}
