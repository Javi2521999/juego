package guis;
import game.GameEditor_4;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
   Ejemplo que:
   
    - Tiene una etiqueta y un campo de texto.
    - Maneja los eventos que se producen en el campo de texto.
    - Muestra un cuadro de di�logo que informa de los errores
      del operador.
  
   @author TIC-LSI 
 */
public class CampoDeTextoYDialogo extends JDialog implements ActionListener{
    
    
    private JLabel etiqueta;
    private JSpinner campoTexto;
    SpinnerNumberModel m_numberSpinnerModel;
    private GameEditor_4 parentJFrame;
    private JButton jButton;

    public CampoDeTextoYDialogo(GameEditor_4 parentJFrame){

       //super("Tamaño de cuadricula");
       this.parentJFrame = parentJFrame;
       // Creamos etiqueta.
       etiqueta = new JLabel("Seleccione un valor");
       etiqueta.setHorizontalAlignment(SwingConstants.LEFT); 
       etiqueta.setPreferredSize(new Dimension(250,30)); 
       etiqueta.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        Double current = new Double(5.00);
        Double min = new Double(3.00);
        Double max = new Double(12.00);
        Double step = new Double(1.00);
        m_numberSpinnerModel = new SpinnerNumberModel(current, min, max, step);
        campoTexto = new JSpinner(m_numberSpinnerModel);
       campoTexto.setPreferredSize(new Dimension(150,30));     
       
       // La ventana se suscribe a los eventos del campo de texto.
        jButton = new JButton("Apply");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentJFrame.setCanvasWidth(((Double)campoTexto.getValue()).intValue());
                setVisible(false);

            }
        });
       // A�adimos etiqueta y campo de texto.
       getContentPane().setLayout(new FlowLayout());
       getContentPane().add(etiqueta);
       getContentPane().add(campoTexto);
       getContentPane().add(jButton);
             
        
       // Fijamos tama�o de la ventana.       
       setSize (520,120);
       
       // Hacemos la ventana visible.
       //setVisible(true);
       
       // Forzamos a que la aplicaci�n termine al cerrar la ventana.     
      // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
 
    /*public void actionPerformed(ActionEvent ae){
    
       	try{
            int num = Integer.parseInt(campoTexto.getText());
            System.out.println("Numero = " + num);
            if (num <= 0)
                JOptionPane.showMessageDialog(this, "El n�mero debe ser > 0"); 
       	}
        catch(Exception e){
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(this,
                                           e.toString(),
                                           "Input error",
                                           JOptionPane.ERROR_MESSAGE);
        } 
    }*/
    
    public static void main(String [] args){
       CampoDeTextoYDialogo gui = new CampoDeTextoYDialogo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}