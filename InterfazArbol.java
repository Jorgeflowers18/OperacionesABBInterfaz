/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package operacionesABB;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;
import javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.T;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Toloza XD
 */
class Arbol {

    private Nodo raiz;
    int num_nodos;
    int alt;
    private String preorden = "Recorrido PreOrden\n";
    private String inorden = "Recorrido EnOrden\n";
    private String postorden = "Recorrido PostOrden\n";

    public Arbol() {
        raiz = null;
    }

    public boolean agregar(int dato) {
        Nodo nuevo = new Nodo(dato, null, null);
        insertar(nuevo, raiz);
        return true;
    }

    //Metodo para insertar un dato en el arbol...no acepta repetidos :)
    public void insertar(Nodo nuevo, Nodo pivote) {
        if (this.raiz == null) {
            raiz = nuevo;
        } else {
            if (nuevo.getDato() <= pivote.getDato()) {
                if (pivote.getIzq() == null) {
                    pivote.setIzq(nuevo);
                } else {
                    insertar(nuevo, pivote.getIzq());
                }
            } else {
                if (pivote.getDer() == null) {
                    pivote.setDer(nuevo);
                } else {
                    insertar(nuevo, pivote.getDer());
                }
            }
        }

    }

    public Nodo getRaiz() {
        return raiz;
    }

    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }

    public String recorridoPostorden() {
        postOrden(getRaiz());
        return this.postorden;
    }

    private void postOrden(Nodo nodoAr) {
        if (nodoAr == null) {
            return;
        }
        postOrden(nodoAr.getIzq());
        postOrden(nodoAr.getDer());
        this.postorden = String.format("%s%d   ", this.postorden, nodoAr.getDato());
    }

    public String recorridoEnorden() {
        enOrden(getRaiz());
        return this.inorden;
    }

    private void enOrden(Nodo nodoAr) {
        if (nodoAr == null) {
            return;
        }

        enOrden(nodoAr.getIzq());
        this.inorden = String.format("%s%d   ", this.inorden, nodoAr.getDato());
        enOrden(nodoAr.getDer());
    }

    public String recorridoPreorden() {
        preOrden(getRaiz());
        return this.preorden;
    }

    private void preOrden(Nodo nodoAr) {
        if (nodoAr == null) {
            return;
        }
        this.preorden = String.format("%s%d   ", this.preorden, nodoAr.getDato());
        preOrden(nodoAr.getIzq());
        preOrden(nodoAr.getDer());
    }

    //Metodo para verificar si hay un nodo en el arbol
    public boolean existe(int dato) {
        Nodo aux = raiz;
        while (aux != null) {
            if (dato == aux.getDato()) {
                return true;
            } else if (dato > aux.getDato()) {
                aux = aux.getDer();
            } else {
                aux = aux.getIzq();
            }
        }
        return false;
    }

    private void altura(Nodo aux, int nivel) {
        if (aux != null) {
            altura(aux.getIzq(), nivel + 1);
            alt = nivel;
            altura(aux.getDer(), nivel + 1);
        }
    }

    //Devuleve la altura del arbol
    public int getAltura() {
        altura(raiz, 1);
        return alt;
    }

    public JPanel getdibujo() {
        return new Graficar(this);
    }

    public void limpiar() {
        this.preorden = "Recorrido PreOrden\n";
        this.inorden = "Recorrido EnOrden\n";
        this.postorden = "Recorrido PostOrden\n";
    }
    public int borrar(int x) {
        if (!this.buscar(x)) {
            return 0;
        }

        Nodo z = borrar(this.raiz, x);
        this.setRaiz(z);
        return x;

    }

    private Nodo borrar(Nodo r, int x) {
        if (r == null) {
            return null;//<--Dato no encontrado		
        }
        int compara = ((Comparable) r.getDato()).compareTo(x);
        if (compara > 0) {
            r.setIzq(borrar(r.getIzq(), x));
        } else if (compara < 0) {
            r.setDer(borrar(r.getDer(), x));
        } else {
            System.out.println("Dato encontrado: " + Integer.toString(x));
            if (r.getIzq() != null && r.getDer() != null) {
                /*
		 *	Buscar el menor de los derechos y lo intercambia por el dato
		 *	que desea borrar. La idea del algoritmo es que el dato a borrar 
		 *	se coloque en una hoja o en un nodo que no tenga una de sus ramas.
		 **/
                Nodo cambiar = buscarMin(r.getDer());
                int aux = cambiar.getDato();
                cambiar.setDato(r.getDato());
                r.setDato(aux);
                r.setDer(borrar(r.getDer(), x));
                System.out.println("2 ramas");
            } else {
                r = (r.getIzq() != null) ? r.getIzq() : r.getDer();
                System.out.println("Reduciendo árbol...");
            }
        }
        return r;
    }
    
    /*
    Métodos para encontrar el nodo para borrarlo
    */
    
    public boolean buscar(int x) {
        return (buscar(this.raiz, x));

    }

    private boolean buscar(Nodo r, int x) {
        if (r == null) {
            return (false);
        }
        int compara = ((Comparable) r.getDato()).compareTo(x);
        if (compara > 0) {
            return (buscar(r.getIzq(), x));
        } else if (compara < 0) {
            return (buscar(r.getDer(), x));
        } else {
            return (true);
        }
    }
    /*
    Retorna el menor nodo del árbol
    */
    private Nodo buscarMin(Nodo r) {
        for (; r.getIzq() != null; r = r.getIzq());
        return (r);
    }
}

class Nodo {

    private int dato;
    private Nodo izq, der;

    public Nodo(int dato, Nodo izq, Nodo der) {
        this.dato = dato;
        this.izq = izq;
        this.der = der;
    }

    public int getDato() {
        return dato;
    }

    public void setDato(int dato) {
        this.dato = dato;
    }

    public Nodo getIzq() {
        return izq;
    }

    public void setIzq(Nodo izq) {
        this.izq = izq;
    }

    public Nodo getDer() {
        return der;
    }

    public void setDer(Nodo der) {
        this.der = der;
    }

}

class Intermediario {

    Arbol ABB = new Arbol();

    public Intermediario() {
    }

    public boolean insertar(Integer dato) {
        return (this.ABB.agregar(dato));
    }

    private String recorrido(LinkedList it, String msg) {
        int i = 0;
        String r = msg + "\n";
        while (i < it.size()) {
            r += "\t" + it.get(i).toString() + "";
            i++;
        }
        return (r);
    }

    //Metodo para buscar dato en el nodo
    public String buscar(Integer dato) {
        boolean siEsta = this.ABB.existe(dato);
        String r = "El dato:" + dato.toString() + "\n";
        r += siEsta ? "Si se encuentra en el arbol" : "No se encuentra en el arbol";
        return (r);
    }    
    
    public String borrar(int dato)
    {
        Integer x = this.ABB.borrar(dato);
        if (x == 0)
            return ("No existe el dato en el arbol");
       return ("Borrado el dato: " + x.toString());
    }
    
    public JPanel getDibujo() {
        return this.ABB.getdibujo();
    }
}

public class InterfazArbol extends javax.swing.JFrame {

    private Intermediario simulador = new Intermediario();

    /**
     * Creates new form Vista
     */
    public InterfazArbol() {
        initComponents();
        this.inicializar(false);

    }

    private void inicializar(boolean enable) {
        this.InOrden.setEnabled(enable);
        this.PostOrden.setEnabled(enable);
        this.PreOrden.setEnabled(enable);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        impresion = new javax.swing.JTextArea();
        InOrden = new javax.swing.JButton();
        PreOrden = new javax.swing.JButton();
        txtdato = new javax.swing.JTextField();
        botonInsertar = new javax.swing.JButton();
        PostOrden = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        borrarNodo = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 51, 255));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Panel de Pruebas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10))); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jDesktopPane1.setOpaque(false);

        jInternalFrame2.setIconifiable(true);
        jInternalFrame2.setMaximizable(true);
        jInternalFrame2.setResizable(true);
        jInternalFrame2.setEnabled(false);
        jInternalFrame2.setFocusCycleRoot(false);
        jInternalFrame2.setVisible(true);
        jDesktopPane1.add(jInternalFrame2);
        jInternalFrame2.setBounds(0, 0, 590, 320);

        jPanel2.add(jDesktopPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 28, 590, 327));

        jScrollPane1.setOpaque(false);

        impresion.setEditable(false);
        impresion.setColumns(20);
        impresion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        impresion.setRows(5);
        impresion.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados de operaciones"));
        impresion.setOpaque(false);
        jScrollPane1.setViewportView(impresion);
        impresion.getAccessibleContext().setAccessibleName("Recorridos");

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 590, 110));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 630, 490));
        jPanel2.getAccessibleContext().setAccessibleName("Gráfica del Árbol Binario");
        jPanel2.getAccessibleContext().setAccessibleDescription("Gráfica del Árbol Binario");

        InOrden.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        InOrden.setText("InOrden");
        InOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InOrdenActionPerformed(evt);
            }
        });
        getContentPane().add(InOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 370, 82, 33));

        PreOrden.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        PreOrden.setText("PreOrden");
        PreOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreOrdenActionPerformed(evt);
            }
        });
        getContentPane().add(PreOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, -1, 33));

        txtdato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdatoActionPerformed(evt);
            }
        });
        getContentPane().add(txtdato, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 140, 30));

        botonInsertar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botonInsertar.setText("Insertar");
        botonInsertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonInsertarActionPerformed(evt);
            }
        });
        getContentPane().add(botonInsertar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 220, 100, 30));

        PostOrden.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        PostOrden.setText("PostOrden");
        PostOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PostOrdenActionPerformed(evt);
            }
        });
        getContentPane().add(PostOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 370, -1, 33));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("SISTEMA DE OPERACIONES");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 260, 40));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("CON");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("ÁRBOLES BINARIOS");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, -1, -1));

        borrarNodo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        borrarNodo.setText("Borrar");
        borrarNodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarNodoActionPerformed(evt);
            }
        });
        getContentPane().add(borrarNodo, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, 100, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Dato a procesar:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 110, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonInsertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonInsertarActionPerformed
        try {
            int dato = Integer.parseInt(txtdato.getText());
            if (this.simulador.insertar(dato)) {
                JOptionPane.showMessageDialog(null, "El dato fue insertado correctamente", " ...", 1);
                this.inicializar(true);

                complementos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se pudo insertar el dato", "Intenta de nuevo...", 0);

        }
        limpiar();
    }//GEN-LAST:event_botonInsertarActionPerformed

    private void InOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InOrdenActionPerformed
        // TODO add your handling code here:
        String recorrido = "";
        recorrido = simulador.ABB.recorridoEnorden();
        this.impresion.setText("");
        this.impresion.setText(recorrido);
        this.simulador.ABB.limpiar();
        limpiar();
    }//GEN-LAST:event_InOrdenActionPerformed

    private void PreOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PreOrdenActionPerformed
        // TODO add your handling code here:
        String recorrido = "";
        recorrido = simulador.ABB.recorridoPreorden();
        this.impresion.setText("");
        this.impresion.setText(recorrido);
        this.simulador.ABB.limpiar();
        limpiar();
    }//GEN-LAST:event_PreOrdenActionPerformed

    private void PostOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PostOrdenActionPerformed
        // TODO add your handling code here:
        String recorrido = "";
        recorrido = simulador.ABB.recorridoPostorden();
        this.impresion.setText("");
        this.impresion.setText(recorrido);
        this.simulador.ABB.limpiar();
        limpiar();
    }//GEN-LAST:event_PostOrdenActionPerformed

    private void txtdatoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdatoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdatoActionPerformed

    private void borrarNodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarNodoActionPerformed
        int dato = Integer.parseInt(txtdato.getText());
        this.impresion.setText(this.simulador.borrar(dato));
        this.repintarArbol();
        limpiar();
    }//GEN-LAST:event_borrarNodoActionPerformed

    public void complementos() {
        this.repintarArbol();
    }

    private void repintarArbol() {
        this.jDesktopPane1.removeAll();
        Rectangle tamaño = this.jInternalFrame2.getBounds();
        this.jInternalFrame2 = null;
        this.jInternalFrame2 = new JInternalFrame("Representación gráfica", true);
        this.jDesktopPane1.add(this.jInternalFrame2, JLayeredPane.DEFAULT_LAYER);
        this.jInternalFrame2.setVisible(true);
        this.jInternalFrame2.setBounds(tamaño);
        this.jInternalFrame2.setEnabled(false);
        this.jInternalFrame2.add(this.simulador.getDibujo(), BorderLayout.CENTER);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfazArbol.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazArbol.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazArbol.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazArbol.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazArbol().setVisible(true);
            }
        });
    }

    private void limpiar() {
        txtdato.setText("");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton InOrden;
    private javax.swing.JButton PostOrden;
    private javax.swing.JButton PreOrden;
    private javax.swing.JButton borrarNodo;
    private javax.swing.JButton botonInsertar;
    private javax.swing.JTextArea impresion;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField txtdato;
    // End of variables declaration//GEN-END:variables
}

class Graficar extends JPanel {

    private Arbol miArbol;
    private HashMap posicionNodos = null;
    private HashMap subArbTam    = null;
    private boolean dirty = true;
    private int parent2child = 20, child2child = 30;
    private Dimension empty = new Dimension(0, 0);
    private FontMetrics fm = null;

    public Graficar(Arbol miArbol) {
        this.miArbol = miArbol;
        this.setBackground(Color.WHITE);
        posicionNodos = new HashMap();
        subArbTam = new HashMap();
        dirty = true;
        repaint();
    }

    private void calcularPosiciones() {
        posicionNodos.clear();
        subArbTam.clear();
        Nodo root = this.miArbol.getRaiz();
        if (root != null) {
            calcularTamañoSubarbol(root);
            calcularPosicion(root, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        }
    }

    private Dimension calcularTamañoSubarbol(Nodo n) {
        if (n == null) {
            return new Dimension(0, 0);
        }

        Dimension ld = calcularTamañoSubarbol(n.getIzq());
        Dimension rd = calcularTamañoSubarbol(n.getDer());

        int h = fm.getHeight() + parent2child + Math.max(ld.height, rd.height);
        int w = ld.width + child2child + rd.width;

        Dimension d = new Dimension(w, h);
        subArbTam.put(n, d);

        return d;
    }

    private void calcularPosicion(Nodo n, int left, int right, int top) {
        if (n == null) {
            return;
        }

        Dimension ld = (Dimension) subArbTam.get(n.getIzq());
        if (ld == null) {
            ld = empty;
        }

        Dimension rd = (Dimension) subArbTam.get(n.getDer());
        if (rd == null) {
            rd = empty;
        }

        int center = 0;

        if (right != Integer.MAX_VALUE) {
            center = right - rd.width - child2child / 2;
        } else if (left != Integer.MAX_VALUE) {
            center = left + ld.width + child2child / 2;
        }
        int width = fm.stringWidth(n.getDato() + "");

        posicionNodos.put(n, new Rectangle(center - width / 2 - 3, top, width + 6, fm.getHeight()));

        calcularPosicion(n.getIzq(), Integer.MAX_VALUE, center - child2child / 2, top + fm.getHeight() + parent2child);
        calcularPosicion(n.getDer(), center + child2child / 2, Integer.MAX_VALUE, top + fm.getHeight() + parent2child);
    }

    private void dibujarArbol(Graphics2D g, Nodo n, int puntox, int puntoy, int yoffs) {
        if (n == null) {
            return;
        }

        Rectangle r = (Rectangle) posicionNodos.get(n);
        g.draw(r);
        g.drawString(n.getDato() + "", r.x + 3, r.y + yoffs);

        if (puntox != Integer.MAX_VALUE) {
            g.drawLine(puntox, puntoy, (int) (r.x + r.width / 2), r.y);
        }

        dibujarArbol(g, n.getIzq(), (int) (r.x + r.width / 2), r.y + r.height, yoffs);
        dibujarArbol(g, n.getDer(), (int) (r.x + r.width / 2), r.y + r.height, yoffs);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        fm = g.getFontMetrics();

        if (dirty) {
            calcularPosiciones();
            dirty = false;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getWidth() / 2, parent2child);
        dibujarArbol(g2d, this.miArbol.getRaiz(), Integer.MAX_VALUE, Integer.MAX_VALUE,
                fm.getLeading() + fm.getAscent());
        fm = null;
    }

}

class Lienzo extends javax.swing.JPanel {

    private String ruta;

    public Lienzo() {
        initComponents();
        this.setSize(933, 690);
        this.ruta = "";

    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension tam = getSize();
        ImageIcon ImagenFondo = new ImageIcon(new ImageIcon(getClass().getResource(ruta)).getImage());
        g.drawImage(ImagenFondo.getImage(), 0, 0, tam.width, tam.height, null);
        setOpaque(false);
        super.paintComponent(g);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 462, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 323, Short.MAX_VALUE)
        );
    }// </editor-fold>                        
    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
