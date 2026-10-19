import java.util.Random;

/**
 * Stato del gioco del 15, indipendente dalla grafica.
 * Le celle sono memorizzate riga per riga; 0 rappresenta la casella vuota.
 */
public class Tabellone {

    public static final int LATO = 4;
    private static final int MOSSE_MESCOLAMENTO = 400;

    private final int[] celle = new int[LATO * LATO];
    private int vuoto;
    private int mosse;

    public Tabellone() {
        ordina();
    }

    private void ordina() {
        for (int i = 0; i < celle.length - 1; i++) {
            celle[i] = i + 1;
        }
        vuoto = celle.length - 1;
        celle[vuoto] = 0;
        mosse = 0;
    }

    /**
     * Mescola eseguendo mosse valide a partire dalla soluzione:
     * una permutazione casuale delle tessere sarebbe irrisolvibile nella metà dei casi.
     */
    public void mescola(Random random) {
        ordina();
        int precedente = -1;
        int eseguite = 0;
        while (eseguite < MOSSE_MESCOLAMENTO || isRisolto()) {
            int[] vicine = vicineAlVuoto();
            int scelta = vicine[random.nextInt(vicine.length)];
            if (scelta == precedente) {
                continue;
            }
            precedente = vuoto;
            scambia(scelta);
            eseguite++;
        }
        mosse = 0;
    }

    private int[] vicineAlVuoto() {
        int riga = vuoto / LATO, colonna = vuoto % LATO;
        int[] tmp = new int[4];
        int n = 0;
        if (riga > 0) tmp[n++] = vuoto - LATO;
        if (riga < LATO - 1) tmp[n++] = vuoto + LATO;
        if (colonna > 0) tmp[n++] = vuoto - 1;
        if (colonna < LATO - 1) tmp[n++] = vuoto + 1;
        int[] vicine = new int[n];
        System.arraycopy(tmp, 0, vicine, 0, n);
        return vicine;
    }

    private void scambia(int indice) {
        celle[vuoto] = celle[indice];
        celle[indice] = 0;
        vuoto = indice;
    }

    /** Sposta nella casella vuota la tessera indicata, se è adiacente. */
    public boolean muovi(int riga, int colonna) {
        if (riga < 0 || riga >= LATO || colonna < 0 || colonna >= LATO) {
            return false;
        }
        int distanza = Math.abs(riga - getRigaVuoto()) + Math.abs(colonna - getColonnaVuoto());
        if (distanza != 1) {
            return false;
        }
        scambia(riga * LATO + colonna);
        mosse++;
        return true;
    }

    /**
     * Mossa da tastiera: la tessera accanto al vuoto scorre nella direzione indicata.
     * Esempio: freccia su = la tessera sotto il vuoto sale.
     */
    public boolean scorri(int dRiga, int dColonna) {
        return muovi(getRigaVuoto() - dRiga, getColonnaVuoto() - dColonna);
    }

    public boolean isRisolto() {
        for (int i = 0; i < celle.length - 1; i++) {
            if (celle[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    public boolean isAlPosto(int riga, int colonna) {
        int indice = riga * LATO + colonna;
        return celle[indice] == indice + 1;
    }

    public int getValore(int riga, int colonna) {
        return celle[riga * LATO + colonna];
    }

    public int getRigaVuoto() {
        return vuoto / LATO;
    }

    public int getColonnaVuoto() {
        return vuoto % LATO;
    }

    public int getMosse() {
        return mosse;
    }
}
