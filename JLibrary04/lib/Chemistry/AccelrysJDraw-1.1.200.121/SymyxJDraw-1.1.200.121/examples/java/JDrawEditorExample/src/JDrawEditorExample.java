import com.symyx.draw.Editor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class JDrawEditorExample extends JDialog {
    private Editor editor = new Editor();

    private static final FileFilter ffMOL = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().toUpperCase().endsWith(".MOL");
        }
        @Override
        public String getDescription() {
            return "Molfile (*.mol)";
        }
    };
    private static final FileFilter ffRXN = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().toUpperCase().endsWith(".RXN");
        }
        @Override
        public String getDescription() {
            return "Rxnfile (*.rxn)";
        }
    };
    private static final FileFilter ffGIF = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().toUpperCase().endsWith(".GIF");
        }
        @Override
        public String getDescription() {
            return "GIF file (*.gif)";
        }
    };
    private static final FileFilter ffPNG = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getName().toUpperCase().endsWith(".PNG");
        }
        @Override
        public String getDescription() {
            return "PNG file (*.png)";
        }
    };

    public JDrawEditorExample() {
        super((JFrame)null, "JDraw Editor example");

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(750, 550);
        setLocationRelativeTo(null);

        setJMenuBar(new JMenuBar());
        JMenu mnuFile = new JMenu("File");
        getJMenuBar().add(mnuFile);
        mnuFile.add("Open...").addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
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
                    editor.setMolString(molString);
                    editor.repaint();
                }
            }
        });
        mnuFile.add("Save...").addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                FileFilter ff = editor.getRxnString() == null ? ffMOL : ffRXN;
                fc.addChoosableFileFilter(ff);
                fc.addChoosableFileFilter(ffGIF);
                fc.addChoosableFileFilter(ffPNG);
                fc.setFileFilter(ff);
                if (fc.showSaveDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                    try {
                        if (fc.getFileFilter() == ffGIF) {
                            ImageIO.write(editor.getMolImage(), "GIF", fc.getSelectedFile());
                        } else if (fc.getFileFilter() == ffPNG) {
                            ImageIO.write(editor.getMolImage(), "PNG", fc.getSelectedFile());
                        } else {
                            String molString = editor.getMolString();
                            try {
                                FileWriter fw = new FileWriter(fc.getSelectedFile());
                                fw.write(molString);
                                fw.close();
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        mnuFile.addSeparator();
        mnuFile.add("Exit").addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(editor, BorderLayout.CENTER);
    }

    private static JDrawEditorExample dialog = new JDrawEditorExample();

    public static void main(String[] args) {
        dialog.setVisible(true);
    }
}