import com.symyx.draw.Renderer;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JDrawRendererExample extends JDialog {
    private Renderer renderer = new Renderer();

    public JDrawRendererExample() {
        super((JFrame)null, "JDraw Renderer example");

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(renderer, BorderLayout.CENTER);

        JButton btnOpen = new JButton("Open File...");
        btnOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                final FileFilter ffMOL = new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().toUpperCase().endsWith(".MOL");
                    }

                    @Override
                    public String getDescription() {
                        return "Molfile (*.mol)";
                    }
                };
                final FileFilter ffRXN = new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().toUpperCase().endsWith(".RXN");
                    }

                    @Override
                    public String getDescription() {
                        return "Rxnfile (*.rxn)";
                    }
                };
                fc.addChoosableFileFilter(ffMOL);
                fc.addChoosableFileFilter(ffRXN);
                fc.setFileFilter(ffMOL);
                if (fc.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                    String molString = null;
                    try {
                        char molData[] = new char[(int)fc.getSelectedFile().length()];
                        FileReader fr = new FileReader(fc.getSelectedFile());
                        fr.read(molData);
                        fr.close();
                        molString = new String(molData);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    renderer.setMolString(molString);
                    renderer.repaint();
                }
            }
        });
        getContentPane().add(btnOpen, BorderLayout.SOUTH);
    }

    private static JDrawRendererExample dialog = new JDrawRendererExample();

    public static void main(String[] args) {
        dialog.setVisible(true);
    }
}
