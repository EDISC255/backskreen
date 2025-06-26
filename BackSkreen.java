import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class BackSkreen extends JFrame {
    public BackSkreen() {
        init();
        llenarLista(System.getenv("HOME"));
    }

    private void init() {
        // ventana
        this.setSize(400, 300);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);

        // lista de imagenes
        lstImagenes = new JList();
        lstImagenes.setBounds(0, 0, 190, 160);

        scrollPane = new JScrollPane(lstImagenes);
        scrollPane.setBounds(10, 10, 190, 160);
        // scrollPane.add(lstImagenes);
        scrollPane.setVerticalScrollBar(new JScrollBar());

        lstImagenes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                prevusualizarImagen(evt);
            }
        });

        // panel previsualizador
        pnlPreview = new JPanel();
        pnlPreview.setSize(160, 90);
        pnlPreview.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pnlPreview.setBounds(200, 10, 160, 90);

        // boton para aplicar el fondo de pantalla
        btnAplicar = new JButton();
        btnAplicar.setText("APLICAR");
        btnAplicar.setSize(100, 50);
        btnAplicar.setBounds(200, 100, 100, 25);

        btnAplicar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                aplicar();
            }
        });

        btnSeleccionarCarpeta = new JButton();
        btnSeleccionarCarpeta.setText("SELECCIONAR CARPETA");
        btnSeleccionarCarpeta.setSize(100, 50);
        btnSeleccionarCarpeta.setBounds(200, 135, 200, 25);

        btnSeleccionarCarpeta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                llenarLista(seleccionarCarpeta());
            }
        });

        this.add(scrollPane);
        scrollPane.updateUI();
        this.add(pnlPreview);
        this.add(btnAplicar);
        this.add(btnSeleccionarCarpeta);
    }

    private void aplicar() {
        try {
            String cmd[] = { "feh", "--bg-fill", imagen };
            Runtime.getRuntime().exec(cmd);
            guardar("feh --bg-fill " + imagen);
            // System.out.println(cmd[2]);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void guardar(String cmd) {
        try {
            String homeDir = System.getenv("HOME");
            aux = new File(homeDir + "/.config/bg.sh");
            if (!aux.exists()) {
                aux.createNewFile();
            }
            FileWriter fw = new FileWriter(aux);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cmd);
            bw.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    public void llenarLista(String directorio) {
        try {
            DefaultListModel dlm = new DefaultListModel();
            aux = new File(directorio);
            String[] archivos = aux.list();
            if (archivos == null || archivos.length == 0) {
                JOptionPane.showMessageDialog(this, "directorio vacio");
            } else {
                for (int i = 0; i < archivos.length; i++) {
                    if (archivos[i].endsWith(".png") || archivos[i].endsWith(".jpg") || archivos[i].endsWith(".jpeg")) {
                        dlm.addElement(archivos[i]);
                    }
                }
            }
            lstImagenes.setModel(dlm);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void prevusualizarImagen(MouseEvent evt) {
        imagen = aux.getAbsolutePath() + "/" + lstImagenes.getSelectedValue();
        Graphics preview = pnlPreview.getGraphics();
        preview.drawImage(new ImageIcon(imagen).getImage(), 0, 0, pnlPreview.getWidth(), pnlPreview.getHeight(), null);
    }

    public String seleccionarCarpeta() {
        JFileChooser carpeta = new JFileChooser();
        carpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int opcionSeleccionada = carpeta.showOpenDialog(this);
        if (opcionSeleccionada == JFileChooser.APPROVE_OPTION) {
            return carpeta.getSelectedFile().getAbsolutePath();
        }
        return System.getenv("HOME");
    }

    public static void main(String arg[]) {
        new BackSkreen().setVisible(true);
    }

    private File aux;
    private String imagen;
    private JPanel pnlPreview;
    private JButton btnAplicar, btnSeleccionarCarpeta;
    private JList lstImagenes;
    private JScrollPane scrollPane;
}