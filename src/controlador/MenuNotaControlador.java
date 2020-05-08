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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import modelo.Nota;
import modelo.NotasJDBC;
import vista.MenuNota;
import static vista.MenuNota.__tabla_notas;

/**
 *
 * @author migue
 */
public class MenuNotaControlador implements ActionListener, MouseListener{
    
    /** instancia a nuestra interfaz de usuario*/
    MenuNota vista ;
    public static NuevaNotaControlador nuevaNota;
    
    Nota nota = new Nota();
    /** instancia a nuestro modelo */
    NotasJDBC notasConn = new NotasJDBC();
    

    /** Se declaran en un ENUM las acciones que se realizan desde la
     * interfaz de usuario VISTA y posterior ejecución desde el controlador
     */
    public enum AccionMVC
    {
        __NUEVA_NOTA,
        __MODIFICAR_NOTA,
        __ELIMINAR_NOTA,
        __VOLVER
    }

    /** Constrcutor de clase
     * @param vista Instancia de clase interfaz
     */
    public MenuNotaControlador( MenuNota vista )
    {
        this.vista = vista;
    }

    /** Inicia el skin y las diferentes variables que se utilizan */
    public void iniciar()
    {
        // Skin tipo WINDOWS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(vista);
            vista.setVisible(true);
            
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {}

        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__NUEVA_NOTA.setActionCommand( "__NUEVA_NOTA" );
        this.vista.__NUEVA_NOTA.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__MODIFICAR_NOTA.setActionCommand( "__MODIFICAR_NOTA" );
        this.vista.__MODIFICAR_NOTA.addActionListener(this);
        //declara una acción y añade un escucha al evento producido por el componente
        this.vista.__ELIMINAR_NOTA.setActionCommand( "__ELIMINAR_NOTA" );
        this.vista.__ELIMINAR_NOTA.addActionListener(this);
        
        this.vista.__VOLVER.setActionCommand( "__VOLVER" );
        this.vista.__VOLVER.addActionListener(this);
        
        MenuNota.__tabla_notas.addMouseListener(this);
        MenuNota.__tabla_notas.setModel( new DefaultTableModel() );
    }

    //Eventos que suceden por el mouse
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1)//boton izquierdo
        {
            int fila = MenuNota.__tabla_notas.rowAtPoint(e.getPoint());
            
            if (fila > -1) {
                this.vista.idNotaBox.setText(String.valueOf(MenuNota.__tabla_notas.getModel().getValueAt(fila, 0)));
                
                this.vista.temaBox.setText(String.valueOf(MenuNota.__tabla_notas.getValueAt(fila, 0)));
                
                this.vista.idLibroBox.setText(String.valueOf(MenuNota.__tabla_notas.getValueAt(fila, 1)));
                
                this.vista.idArticBox.setText(String.valueOf(MenuNota.__tabla_notas.getValueAt(fila, 2)));
                
                this.vista.contenidoArea.setText(String.valueOf(MenuNota.__tabla_notas.getModel().getValueAt(fila, 4)));
                
            }
        }
    }

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) { }
 
    //Control de eventos de los controles que tienen definido un "ActionCommand"
    public void actionPerformed(ActionEvent e) {

    switch (AccionMVC.valueOf(e.getActionCommand())) {
            case __NUEVA_NOTA:
                if (this.vista.temaBox.getText().length() == 0 || this.vista.contenidoArea.getText().length() == 0) {

                    JOptionPane.showMessageDialog(null, "Los campos no pueden estar vacios.");
                } else {

                nota.setTema(this.vista.temaBox.getText());
                nota.setContenido(this.vista.contenidoArea.getText());

                notasConn.insert(nota);
                MenuNota.__tabla_notas.setModel(getTabla());
                //clean();
                }
                break;
                
            case __MODIFICAR_NOTA:
                
                if(notasConn.existeIdNota(Integer.parseInt(this.vista.idNotaBox.getText())) > 0){

                        nota.setId(Integer.parseInt(this.vista.idNotaBox.getText()));
                        nota.setTema(this.vista.temaBox.getText());
                        nota.setContenido(this.vista.contenidoArea.getText());
                        
                        if(this.vista.idLibroBox.getText().equals("NO")) {
                            nota.setIdArticulo(Integer.parseInt(this.vista.idArticBox.getText()));
                            notasConn.updateArticulo(nota);
                        } else {
                            nota.setIdLibro(Integer.parseInt(this.vista.idLibroBox.getText()));
                            notasConn.updateLibro(nota);
                        }
                        
                        MenuNota.__tabla_notas.setModel(getTabla());
                } else {
                    
                    JOptionPane.showMessageDialog(null, "No hay ninguna nota seleccionada.");
                }
                
                break;
            case __ELIMINAR_NOTA:
                
                if(notasConn.existeIdNota(Integer.parseInt(this.vista.idNotaBox.getText())) > 0){
                    
                    notasConn.delete(Integer.parseInt(this.vista.idNotaBox.getText()));

                    MenuNota.__tabla_notas.setModel(getTabla());
                    clean();
                } else {
                    
                    JOptionPane.showMessageDialog(null, "No hay ninguna nota seleccionada.");
                }
                
                break;
            case __VOLVER:
                
                this.vista.dispose();
                HomeControlador.mNot = null;
                break;   
        }
    }
    
    private void clean() {
        
        this.vista.idNotaBox.setText("");
        this.vista.temaBox.setText("");
        this.vista.contenidoArea.setText("");
    }
    
    public static DefaultTableModel getTabla() {
        NotasJDBC notaConn = new NotasJDBC();
        
        __tabla_notas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Tema", "ID Libro", "ID Articulo", "Contenido"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };
            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
            
            
        });
        
        TableColumnModel tcm = MenuNota.__tabla_notas.getColumnModel();
        
        
        DefaultTableModel modelo = (DefaultTableModel) MenuNota.__tabla_notas.getModel();
        
        
        
        String[] columNames = {"ID","Tema","ID Libro","ID Articulo","Contenido"};
        
        
        if (notaConn.cuentaNotas() < 1) {
            //JOptionPane.showMessageDialog(null, "La lista de libros esta vacia.");  
        } else {
            
            Object[][] fila = new Object[notaConn.select().size()][5];
            int i = 0;
            
            for (Nota nota : notaConn.select()) {
                fila[i][0] = nota.getId();
                fila[i][1] = nota.getTema();
                if(nota.getIdLibro() == 0){
                    
                    fila[i][2] = "NO";
                    
                }else{
                    fila[i][2] = nota.getIdLibro();
                }
                    
                if(nota.getIdArticulo() == 0){
                    
                    fila[i][3] = "NO";
                    
                }else{
                    
                fila[i][3] = nota.getIdArticulo();
                
                }
                
                fila[i][4] = nota.getContenido();
                
                i++;
            }
            
            modelo.setDataVector(fila, columNames);
        }
        
        try{
            tcm.removeColumn(tcm.getColumn(0));
            tcm.removeColumn(tcm.getColumn(4));
        }
        catch (Exception e) {}
        
        return modelo;
    } 
    
}
