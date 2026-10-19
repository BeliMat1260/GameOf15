import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.prefs.Preferences;

public class Frame extends JFrame {

    private static final Color SFONDO = new Color(24, 78, 119);
    private static final Color TESSERA = new Color(26, 117, 159);
    private static final Color TESSERA_AL_POSTO = new Color(52, 160, 164);
    private static final Color TESTO = new Color(242, 244, 243);

    private static final String PREF_TEMPO = "recordSecondi";
    private static final String PREF_MOSSE = "recordMosse";

    private final Tabellone tabellone = new Tabellone();
    private final JButton[][] tessere = new JButton[Tabellone.LATO][Tabellone.LATO];
    private final Preferences preferenze = Preferences.userRoot().node("gioco-del-15");
    private final Random random = new Random();

    private final JLabel lblMosse = new JLabel();
    private final JLabel lblTempo = new JLabel();
    private final JLabel lblRecord = new JLabel();

    private int secondi = 0;
    private final Timer timer = new Timer(1000, e -> {
        secondi++;
        aggiornaStatistiche();
    });

    public Frame() {
        super("Gioco del 15");

        JPanel nord = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 8));
        nord.setBackground(SFONDO);
        for (JLabel label : new JLabel[]{lblTempo, lblMosse, lblRecord}) {
            label.setForeground(TESTO);
            label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
            nord.add(label);
        }
        JButton nuova = new JButton("Nuova partita");
        nuova.setFocusable(false);
        nuova.addActionListener(e -> nuovaPartita());
        nord.add(nuova);

        JPanel centro = new JPanel(new GridLayout(Tabellone.LATO, Tabellone.LATO, 4, 4));
        centro.setBackground(SFONDO);
        centro.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        for (int riga = 0; riga < Tabellone.LATO; riga++) {
            for (int colonna = 0; colonna < Tabellone.LATO; colonna++) {
                JButton tessera = new JButton();
                tessera.setForeground(TESTO);
                tessera.setFont(tessera.getFont().deriveFont(Font.BOLD, 28f));
                tessera.setBorderPainted(false);
                tessera.setFocusPainted(false);
                tessera.setFocusable(false);
                int r = riga, c = colonna;
                tessera.addActionListener(e -> eseguiMossa(tabellone.muovi(r, c)));
                tessere[riga][colonna] = tessera;
                centro.add(tessera);
            }
        }

        JLabel aiuto = new JLabel("Clicca una tessera o usa frecce / WASD", SwingConstants.CENTER);
        aiuto.setForeground(TESTO);
        aiuto.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel contenuto = new JPanel(new BorderLayout());
        contenuto.setBackground(SFONDO);
        contenuto.add(nord, BorderLayout.NORTH);
        contenuto.add(centro, BorderLayout.CENTER);
        contenuto.add(aiuto, BorderLayout.SOUTH);
        setContentPane(contenuto);

        registraTasti();
        nuovaPartita();

        setSize(520, 600);
        setMinimumSize(new Dimension(380, 440));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void registraTasti() {
        collegaTasto(KeyEvent.VK_UP, KeyEvent.VK_W, -1, 0, "su");
        collegaTasto(KeyEvent.VK_DOWN, KeyEvent.VK_S, 1, 0, "giu");
        collegaTasto(KeyEvent.VK_LEFT, KeyEvent.VK_A, 0, -1, "sinistra");
        collegaTasto(KeyEvent.VK_RIGHT, KeyEvent.VK_D, 0, 1, "destra");
    }

    private void collegaTasto(int freccia, int lettera, int dRiga, int dColonna, String nome) {
        InputMap input = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        input.put(KeyStroke.getKeyStroke(freccia, 0), nome);
        input.put(KeyStroke.getKeyStroke(lettera, 0), nome);
        getRootPane().getActionMap().put(nome, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eseguiMossa(tabellone.scorri(dRiga, dColonna));
            }
        });
    }

    private void nuovaPartita() {
        timer.stop();
        secondi = 0;
        tabellone.mescola(random);
        aggiornaTessere();
        aggiornaStatistiche();
    }

    private void eseguiMossa(boolean valida) {
        if (!valida) {
            return;
        }
        if (!timer.isRunning()) {
            timer.start();
        }
        aggiornaTessere();
        aggiornaStatistiche();
        if (tabellone.isRisolto()) {
            vittoria();
        }
    }

    private void vittoria() {
        timer.stop();
        boolean nuovoRecord = salvaRecord();
        aggiornaStatistiche();
        String messaggio = "Hai vinto in " + formatta(secondi) + " con " + tabellone.getMosse() + " mosse";
        Vittoria dialogo = new Vittoria(this, messaggio, nuovoRecord);
        if (dialogo.isRigioca()) {
            nuovaPartita();
        } else {
            dispose();
            System.exit(0);
        }
    }

    /** Il record è il tempo migliore; a parità di tempo vincono meno mosse. */
    private boolean salvaRecord() {
        int recordSecondi = preferenze.getInt(PREF_TEMPO, Integer.MAX_VALUE);
        int recordMosse = preferenze.getInt(PREF_MOSSE, Integer.MAX_VALUE);
        int mosse = tabellone.getMosse();
        if (secondi < recordSecondi || (secondi == recordSecondi && mosse < recordMosse)) {
            preferenze.putInt(PREF_TEMPO, secondi);
            preferenze.putInt(PREF_MOSSE, mosse);
            return true;
        }
        return false;
    }

    private void aggiornaTessere() {
        for (int riga = 0; riga < Tabellone.LATO; riga++) {
            for (int colonna = 0; colonna < Tabellone.LATO; colonna++) {
                JButton tessera = tessere[riga][colonna];
                int valore = tabellone.getValore(riga, colonna);
                tessera.setVisible(valore != 0);
                tessera.setText(valore == 0 ? "" : String.valueOf(valore));
                tessera.setBackground(tabellone.isAlPosto(riga, colonna) ? TESSERA_AL_POSTO : TESSERA);
            }
        }
    }

    private void aggiornaStatistiche() {
        lblTempo.setText("Tempo: " + formatta(secondi));
        lblMosse.setText("Mosse: " + tabellone.getMosse());
        int recordSecondi = preferenze.getInt(PREF_TEMPO, -1);
        if (recordSecondi < 0) {
            lblRecord.setText("Record: -");
        } else {
            lblRecord.setText("Record: " + formatta(recordSecondi) + " / " + preferenze.getInt(PREF_MOSSE, 0) + " mosse");
        }
    }

    private static String formatta(int totaleSecondi) {
        return String.format("%d:%02d", totaleSecondi / 60, totaleSecondi % 60);
    }
}
