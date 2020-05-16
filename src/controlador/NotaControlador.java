package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import modelo.ArticuloConexion;
import modelo.IOdatos;
import modelo.LibroConexion;
import modelo.Nota;
import modelo.NotaConexion;
import vista.HomeVista;

/**
 * NotaControlador.java
 *
 * @author Miguel Alcantara
 * @version 1.0
 * @since 01/05/2020
 */
public class NotaControlador implements ActionListener, MouseListener {

    //Declaración de objetos necesarios
    public JPanel panel;
    public HomeVista vista;
    public static NuevaNotaControlador nuevaNota;

    NotaConexion notaConn = new NotaConexion();
    LibroConexion libroConn = new LibroConexion();
    ArticuloConexion articuloConn = new ArticuloConexion();
    Nota nota = new Nota();
    IOdatos io = new IOdatos();

    public enum AccionMVC {
        __MODIFICAR_NOTA,
        __ELIMINAR_NOTA,
        __LIMPIAR_NOTA,
        __GUARDAR_TABLA_NOTA,
        __CARGAR_TABLA_NOTA,
        __BUSCAR_NOTA,
        __VOLVER_NOTA,
        __ACTUALIZAR_TABLA_NOTAS
    }

    /**
     * Constructor de la clase NotaControlador
     *
     * @param panel Instancia de clase interfaz
     *
     */
    public NotaControlador(JPanel panel) {
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
        this.vista.__MODIFICAR_NOTA.setActionCommand("__MODIFICAR_NOTA");
        this.vista.__MODIFICAR_NOTA.addActionListener(this);

        this.vista.__ELIMINAR_NOTA.setActionCommand("__ELIMINAR_NOTA");
        this.vista.__ELIMINAR_NOTA.addActionListener(this);

        this.vista.__LIMPIAR_NOTA.setActionCommand("__LIMPIAR_NOTA");
        this.vista.__LIMPIAR_NOTA.addActionListener(this);

        this.vista.__GUARDAR_TABLA_NOTA.setActionCommand("__GUARDAR_TABLA_NOTA");
        this.vista.__GUARDAR_TABLA_NOTA.addActionListener(this);

        this.vista.__CARGAR_TABLA_NOTA.setActionCommand("__CARGAR_TABLA_NOTA");
        this.vista.__CARGAR_TABLA_NOTA.addActionListener(this);

        this.vista.__BUSCAR_NOTA.setActionCommand("__BUSCAR_NOTA");
        this.vista.__BUSCAR_NOTA.addActionListener(this);

        this.vista.__VOLVER_NOTA.setActionCommand("__VOLVER_NOTA");
        this.vista.__VOLVER_NOTA.addActionListener(this);

        this.vista.__ACTUALIZAR_TABLA_NOTAS.setActionCommand("__ACTUALIZAR_TABLA_NOTAS");
        this.vista.__ACTUALIZAR_TABLA_NOTAS.addActionListener(this);

        //Añade, define e inicia el jtable
        this.vista.__tabla_notas.addMouseListener(this);
        this.definirTabla();
        this.vista.__tabla_notas.setModel(setTabla(notaConn.select()));

        //Hace invisible la caja idNota
        this.vista.idNotaBox.setVisible(false);
    }

    //Eventos que suceden por el mouse
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1)//boton izquierdo
        {
            int fila = this.vista.__tabla_notas.rowAtPoint(e.getPoint());

            if (fila > -1) {
                this.vista.idNotaBox.setText(String.valueOf(this.vista.__tabla_notas.getModel().getValueAt(fila, 0)));

                this.vista.temaNotaBox.setText(String.valueOf(this.vista.__tabla_notas.getModel().getValueAt(fila, 1)));

                this.vista.isbnNotaBox.setText(String.valueOf(this.vista.__tabla_notas.getModel().getValueAt(fila, 2)));

                this.vista.issnNotaBox.setText(String.valueOf(this.vista.__tabla_notas.getModel().getValueAt(fila, 3)));

                this.vista.contenidoNotaArea.setText(String.valueOf(this.vista.__tabla_notas.getModel().getValueAt(fila, 4)));

            }
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

            case __MODIFICAR_NOTA:

                //Si la nota existe crea un objeto para modificarla
                if (notaConn.existeIdNota(Integer.parseInt(this.vista.idNotaBox.getText())) > 0) {

                    nota.setId(Integer.parseInt(this.vista.idNotaBox.getText()));
                    nota.setTema(this.vista.temaNotaBox.getText());
                    if (this.vista.contenidoNotaArea.getText().isEmpty()) {
                        nota.setContenido(null);
                    } else {
                        nota.setContenido(this.vista.contenidoNotaArea.getText());
                    }

                    if (this.vista.issnNotaBox.getText().isEmpty()) {
                        nota.setIdArticulo(0);
                    } else {
                        nota.setIdArticulo(articuloConn.getId(ArticuloControlador.issn));
                    }
                    if (this.vista.isbnNotaBox.getText().isEmpty()) {
                        nota.setIdArticulo(0);
                    } else {
                        nota.setIdLibro(libroConn.getId(LibroControlador.isbn));
                    }

                    //Si el metodo update retorna true muestra un mensaje de exito
                    if (notaConn.update(nota)) {
                        JOptionPane.showMessageDialog(null, "Nota modificada con exito.");
                        this.vista.__tabla_notas.setModel(setTabla(notaConn.select()));
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha habido un error.");
                    }
                } else {

                    JOptionPane.showMessageDialog(null, "No hay ninguna nota seleccionada.");
                }
                break;
            case __ELIMINAR_NOTA:

                //Si el id de la nota existe, borra ese registro, en otro caso muestra error
                if (notaConn.existeIdNota(Integer.parseInt(this.vista.idNotaBox.getText())) > 0) {

                    if (notaConn.delete(Integer.parseInt(this.vista.idNotaBox.getText()))) {
                        JOptionPane.showMessageDialog(null, "Nota eliminada con exito.");
                        this.vista.__tabla_notas.setModel(setTabla(notaConn.select()));
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha habido un error.");
                    }

                    clean();
                } else {

                    JOptionPane.showMessageDialog(null, "No hay ninguna nota seleccionada.");
                }
                break;
            case __LIMPIAR_NOTA:
                this.clean();
                break;
            case __GUARDAR_TABLA_NOTA:

                //Intancia un objeto JFileChooser
                JFileChooser fileChooser = new JFileChooser();
                int seleccion = fileChooser.showSaveDialog(this.vista);

                //Si selecciona un fichero y la ruta es valida, guarda el contenido de la tabla en ese fichero
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        io.escrituraNota(getContenidoTabla(), fichero.getAbsolutePath());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Ha habido un error.");
                    }
                }
                break;
            case __CARGAR_TABLA_NOTA:

                fileChooser = new JFileChooser();
                seleccion = fileChooser.showOpenDialog(this.vista);

                //Si selecciona un fichero y la ruta es valida, carga el contenido del fichero en la tabla
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        this.vista.__tabla_notas.setModel(setTabla(io.lecturaNota(fichero.getAbsolutePath())));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar un archivo valido.");
                    }

                }
                break;
            case __BUSCAR_NOTA:

                //Si la caja de buscar registro esta vacia no hace nada
                if (this.vista.busquedaNotaBox.getText().isEmpty()) {
                } else {

                    //Si la busqueda retorna contenido, refresca la tabla con ese contenido
                    if (!notaConn.buscar(this.vista.busquedaNotaBox.getText()).isEmpty()) {
                        this.vista.__tabla_notas.setModel(setTabla(notaConn.buscar(this.vista.busquedaNotaBox.getText())));
                    } else {
                        JOptionPane.showMessageDialog(null, "Busquedas sin resultados.");
                    }
                }
                break;
            case __ACTUALIZAR_TABLA_NOTAS:

                //Refresca el contenido de la tabla con todos los registros en la base de datos
                this.vista.__tabla_notas.setModel(setTabla(notaConn.select()));

                break;
            case __VOLVER_NOTA:

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
    private void clean() {

        this.vista.idNotaBox.setText("");
        this.vista.isbnNotaBox.setText("");
        this.vista.issnNotaBox.setText("");
        this.vista.temaNotaBox.setText("");
        this.vista.contenidoNotaArea.setText("");
    }

    /**
     * Define la tabla de notas
     */
    public void definirTabla() {
        this.vista.__tabla_notas.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Tema", "Libro", "Articulo", "Contenido", "Usuario"
                }
        ) {
            //Define la clase de cada columna
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };

            //Niega la edicion de las columnas
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

        });
    }

    /**
     * Introduce los atributos de una coleccion de notas en la tabla
     *
     * @param lista Entra por parametro un List de objetos Nota
     * @return Retorna el modelo de la tabla con contenido
     */
    public DefaultTableModel setTabla(List<Nota> lista) {

        //Ordena la lista por nombre
        Collections.sort(lista, Nota.temaComparator);

        //Instancia un objeto para modificar columnas
        TableColumnModel tcm = this.vista.__tabla_notas.getColumnModel();

        //Instancia el modelo
        DefaultTableModel modelo = (DefaultTableModel) this.vista.__tabla_notas.getModel();

        String[] columNames = {"ID", "Tema", "ISBN", "ISSN", "Contenido", "Usuario"};

        //Instancia una matriz (numero de objetos)*(numero de columnas)
        Object[][] fila = new Object[lista.size()][6];
        int i = 0;

        //Recorre la lista e introduce el contenido en la matriz
        for (Nota note : lista) {
            fila[i][0] = note.getId();
            fila[i][1] = note.getTema();

            if (note.getIdLibro() < 1) {
                fila[i][2] = "-";
            } else {
                fila[i][2] = libroConn.getISBN(note.getIdLibro());
            }

            if (note.getIdArticulo() < 1) {
                fila[i][3] = "-";
            } else {
                fila[i][3] = articuloConn.getISSN(note.getIdArticulo());
            }

            if (note.getContenido() == null) {
                fila[i][4] = "";
            } else {
                fila[i][4] = note.getContenido();
            }

            fila[i][5] = note.getIdUser();

            i++;
        }

        //Inserta el contenido de la matriz en el modelo
        modelo.setDataVector(fila, columNames);

        //Oculta varias columnas
        tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("ID")));
        tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("Contenido")));
        tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("Usuario")));

        return modelo;
    }

    /**
     * Devuelve el contenido de la tabla en forma de coleccion
     *
     * @return Retorna un ArrayList de Nota
     */
    public List<Nota> getContenidoTabla() {

        List<Nota> lista = new ArrayList<>();

        for (int fila = 0; fila < this.vista.__tabla_notas.getRowCount(); fila++) {
            Nota note = new Nota();

            note.setId(Integer.parseInt(this.vista.__tabla_notas.getModel().getValueAt(fila, 0).toString()));
            note.setTema(String.valueOf(this.vista.__tabla_notas.getModel().getValueAt(fila, 1)));

            if (this.vista.__tabla_notas.getModel().getValueAt(fila, 2).equals("-")) {
                note.setIdLibro(0);
            } else {
                note.setIdLibro(libroConn.getId(LibroControlador.isbn));
            }

            if (this.vista.__tabla_notas.getModel().getValueAt(fila, 3).equals("-")) {
                note.setIdArticulo(0);
            } else {
                note.setIdArticulo(articuloConn.getId(ArticuloControlador.issn));
            }

            note.setContenido(String.valueOf(this.vista.__tabla_notas.getModel().getValueAt(fila, 4)));

            note.setIdUser(Integer.parseInt(this.vista.__tabla_notas.getModel().getValueAt(fila, 5).toString()));

            lista.add(note);
        }

        return lista;
    }

}
