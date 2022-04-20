package guis;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiChangeCanvasWidthJDialog extends JDialog {


    private JLabel labelDetail;
    private JSpinner widthSpinner;
    private SpinnerNumberModel widthSpinnerModel;
    private GuiGame guiGameJFrame;
    private JButton applyButton;
    private Integer currentWidth = 12;
    private Integer minWidth = 12;
    private Integer maxWidth = 20;
    private Integer stepWidth = 1;

    public GuiChangeCanvasWidthJDialog(GuiGame guiGameJFrame){

        this.guiGameJFrame = guiGameJFrame;

        this.labelDetail = new JLabel("Seleccione un valor");
        this.labelDetail.setHorizontalAlignment(SwingConstants.LEFT);
        this.labelDetail.setPreferredSize(new Dimension(250,30));
        this.labelDetail.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        this.widthSpinnerModel = new SpinnerNumberModel(currentWidth, minWidth, maxWidth, stepWidth);
        this.widthSpinner = new JSpinner(this.widthSpinnerModel);
        this.widthSpinner.setPreferredSize(new Dimension(150,30));

        this.applyButton = new JButton("Apply");
        this.applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiGameJFrame.setCanvasWidth((Integer)(widthSpinner.getValue()));
                setVisible(false);

            }
        });
        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(this.labelDetail);
        getContentPane().add(this.widthSpinner);
        getContentPane().add(this.applyButton);

        setSize (520,120);
        setLocationRelativeTo(null);
    }

    public void setCurrentWidth(int width){
        this.currentWidth = width;
        repaint();
    }
}