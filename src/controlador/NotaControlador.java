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
 *
 * @author migue
 */
public class NotaControlador implements ActionListener, MouseListener {

    /**
     * instancia a nuestra interfaz de usuario
     */
    public JPanel panel;
    public HomeVista vista;
    public static NuevaNotaControlador nuevaNota;

    Nota nota = new Nota();
    IOdatos io = new IOdatos();
    /**
     * instancia a nuestro modelo
     */
    NotaConexion notaConn = new NotaConexion();
    LibroConexion libroConn = new LibroConexion();
    ArticuloConexion articuloConn = new ArticuloConexion();

    /**
     * Se declaran en un ENUM las acciones que se realizan desde la interfaz de
     * usuario VISTA y posterior ejecución desde el controlador
     */
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
     * Constrcutor de clase
     *
     * @param panel Instancia de clase interfaz
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
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__ELIMINAR_NOTA.setActionCommand("__ELIMINAR_NOTA");
        this.vista.__ELIMINAR_NOTA.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__LIMPIAR_NOTA.setActionCommand("__LIMPIAR_NOTA");
        this.vista.__LIMPIAR_NOTA.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__GUARDAR_TABLA_NOTA.setActionCommand("__GUARDAR_TABLA_NOTA");
        this.vista.__GUARDAR_TABLA_NOTA.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__CARGAR_TABLA_NOTA.setActionCommand("__CARGAR_TABLA_NOTA");
        this.vista.__CARGAR_TABLA_NOTA.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__BUSCAR_NOTA.setActionCommand("__BUSCAR_NOTA");
        this.vista.__BUSCAR_NOTA.addActionListener(this);

        this.vista.__VOLVER_NOTA.setActionCommand("__VOLVER_NOTA");
        this.vista.__VOLVER_NOTA.addActionListener(this);

        this.vista.__ACTUALIZAR_TABLA_NOTAS.setActionCommand("__ACTUALIZAR_TABLA_NOTAS");
        this.vista.__ACTUALIZAR_TABLA_NOTAS.addActionListener(this);

        this.vista.__tabla_notas.addMouseListener(this);
        this.vista.__tabla_notas.setModel(crearTabla(notaConn.select()));
        
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

            case __MODIFICAR_NOTA:

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
                        nota.setIdArticulo(articuloConn.getId(this.vista.issnNotaBox.getText()));
                    }
                    if (this.vista.isbnNotaBox.getText().isEmpty()) {
                        nota.setIdArticulo(0);
                    } else {
                        nota.setIdLibro(libroConn.getId(this.vista.isbnNotaBox.getText()));
                    }

                    if (notaConn.update(nota)) {
                        JOptionPane.showMessageDialog(null, "Nota modificada con exito.");
                        this.vista.__tabla_notas.setModel(crearTabla(notaConn.select()));
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha habido un error.");
                    }
                } else {

                    JOptionPane.showMessageDialog(null, "No hay ninguna nota seleccionada.");
                }
                break;
            case __ELIMINAR_NOTA:

                if (notaConn.existeIdNota(Integer.parseInt(this.vista.idNotaBox.getText())) > 0) {

                    if (notaConn.delete(Integer.parseInt(this.vista.idNotaBox.getText()))) {
                        JOptionPane.showMessageDialog(null, "Nota eliminada con exito.");
                        this.vista.__tabla_notas.setModel(crearTabla(notaConn.select()));
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

                JFileChooser fileChooser = new JFileChooser();
                int seleccion = fileChooser.showSaveDialog(this.vista);
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
                if (seleccion == JFileChooser.APPROVE_OPTION) {
                    try {
                        File fichero = fileChooser.getSelectedFile();
                        this.vista.__tabla_notas.setModel(crearTabla(io.lecturaNota(fichero.getAbsolutePath())));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Debes seleccionar un archivo valido.");
                    }

                }
                break;
            case __BUSCAR_NOTA:
                if (this.vista.busquedaNotaBox.getText().isEmpty()) {
                } else {
                    if (!notaConn.buscar(this.vista.busquedaNotaBox.getText()).isEmpty()) {
                        this.vista.__tabla_notas.setModel(crearTabla(notaConn.buscar(this.vista.busquedaNotaBox.getText())));
                    } else {
                        JOptionPane.showMessageDialog(null, "Busquedas sin resultados.");
                    }
                }
                break;
            case __ACTUALIZAR_TABLA_NOTAS:

                this.vista.__tabla_notas.setModel(crearTabla(notaConn.select()));

                break;
            case __VOLVER_NOTA:

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

    private void clean() {

        this.vista.idNotaBox.setText("");
        this.vista.isbnNotaBox.setText("");
        this.vista.issnNotaBox.setText("");
        this.vista.temaNotaBox.setText("");
        this.vista.contenidoNotaArea.setText("");
    }

    public DefaultTableModel crearTabla(List<Nota> lista) {

        Collections.sort(lista, Nota.temaComparator);

        this.vista.__tabla_notas.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Tema", "Libro", "Articulo", "Contenido", "Usuario"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }

        });

        TableColumnModel tcm = this.vista.__tabla_notas.getColumnModel();

        DefaultTableModel modelo = (DefaultTableModel) this.vista.__tabla_notas.getModel();

        String[] columNames = {"ID", "Tema", "ISBN", "ISSN", "Contenido", "Usuario"};

        Object[][] fila = new Object[lista.size()][6];
        int i = 0;

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

        modelo.setDataVector(fila, columNames);

        try {

            tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("ID")));
            tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("Contenido")));
            tcm.removeColumn(tcm.getColumn(tcm.getColumnIndex("Usuario")));

        } catch (Exception e) {
        }

        return modelo;
    }

    public List<Nota> getContenidoTabla() {

        List<Nota> lista = new ArrayList<>();

        for (int fila = 0; fila < this.vista.__tabla_notas.getRowCount(); fila++) {
            Nota note = new Nota();

            note.setId(Integer.parseInt(this.vista.__tabla_notas.getModel().getValueAt(fila, 0).toString()));
            note.setTema(String.valueOf(this.vista.__tabla_notas.getModel().getValueAt(fila, 1)));

            if (this.vista.__tabla_notas.getModel().getValueAt(fila, 2).equals("-")) {
                note.setIdLibro(0);
            } else {
                note.setIdLibro(libroConn.getId(this.vista.__tabla_notas.getModel().getValueAt(fila, 2).toString()));
            }

            if (this.vista.__tabla_notas.getModel().getValueAt(fila, 3).equals("-")) {
                note.setIdArticulo(0);
            } else {
                note.setIdArticulo(articuloConn.getId(this.vista.__tabla_notas.getModel().getValueAt(fila, 3).toString()));
            }

            note.setContenido(String.valueOf(this.vista.__tabla_notas.getModel().getValueAt(fila, 4)));

            note.setIdUser(Integer.parseInt(this.vista.__tabla_notas.getModel().getValueAt(fila, 5).toString()));

            lista.add(note);
        }

        return lista;
    }
}
