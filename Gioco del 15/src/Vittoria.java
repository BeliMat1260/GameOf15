import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Vittoria extends JDialog {

    private boolean rigioca = false;

    public Vittoria(JFrame proprietario, String messaggio, boolean nuovoRecord) {
        super(proprietario, "VITTORIA", true);

        JPanel tutto = new JPanel(new BorderLayout(0, 10));
        tutto.setBackground(new Color(136, 189, 195));
        tutto.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel testi = new JPanel(new GridLayout(0, 1));
        testi.setOpaque(false);
        JLabel lblMessaggio = new JLabel(messaggio, SwingConstants.CENTER);
        lblMessaggio.setFont(lblMessaggio.getFont().deriveFont(Font.BOLD, 14f));
        testi.add(lblMessaggio);
        if (nuovoRecord) {
            testi.add(new JLabel("Nuovo record!", SwingConstants.CENTER));
        }
        tutto.add(testi, BorderLayout.NORTH);

        URL immagine = Vittoria.class.getResource("vittoria.jpg");
        if (immagine != null) {
            tutto.add(new JLabel(new ImageIcon(immagine)), BorderLayout.CENTER);
        }

        JButton btnRigioca = new JButton("Rigioca");
        btnRigioca.addActionListener(e -> {
            rigioca = true;
            dispose();
        });
        JButton btnEsci = new JButton("Esci");
        btnEsci.addActionListener(e -> dispose());
        JPanel pulsanti = new JPanel();
        pulsanti.setOpaque(false);
        pulsanti.add(btnRigioca);
        pulsanti.add(btnEsci);
        tutto.add(pulsanti, BorderLayout.SOUTH);

        setContentPane(tutto);
        getRootPane().setDefaultButton(btnRigioca);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(proprietario);
        setVisible(true);
    }

    public boolean isRigioca() {
        return rigioca;
    }
}
