# Gioco del 15

Il classico puzzle scorrevole 4x4 in Java Swing: riordina le tessere da 1 a 15 lasciando vuota l'ultima casella.

## Funzionalità

- Mescolamento sempre risolvibile (mosse casuali a partire dalla soluzione)
- Controllo con il mouse oppure con frecce / WASD
- Timer e contatore delle mosse; il timer parte alla prima mossa
- Record personale (tempo migliore, a parità di tempo meno mosse) salvato tra una sessione e l'altra
- Tessere già al loro posto evidenziate
- Pulsante "Nuova partita" e finestra di vittoria con "Rigioca"

## Comandi

| Tasto | Effetto |
| --- | --- |
| Click su una tessera | La sposta nella casella vuota, se è adiacente |
| Freccia su / W | La tessera sotto il vuoto sale |
| Freccia giù / S | La tessera sopra il vuoto scende |
| Freccia sinistra / A | La tessera a destra del vuoto va a sinistra |
| Freccia destra / D | La tessera a sinistra del vuoto va a destra |

## Avvio

Richiede Java 17 o superiore.

Da IntelliJ IDEA: apri la cartella `Gioco del 15` ed esegui `Main`.

Da terminale:

```bash
cd "Gioco del 15"
javac -d out src/*.java
cp src/vittoria.jpg out/
java -cp out Main
```

## Struttura

| File | Ruolo |
| --- | --- |
| `Tabellone.java` | Stato e regole del gioco, senza dipendenze dalla grafica |
| `Frame.java` | Finestra principale, input, timer e record |
| `Vittoria.java` | Dialog di fine partita |
| `Main.java` | Punto di ingresso |
