package monopoly.hra;

import monopoly.hrac.Hrac;

public abstract class Policko {
    private final String nazov;

    /**
     * Konštruktor triedy Policko.
     *
     * @param nazov názov políčka
     */

    public Policko(String nazov) {
        this.nazov = nazov;

    }

    /**
     * Vráti názov políčka.
     *
     * @return názov políčka
     */
    public String getNazov() {
        return this.nazov;
    }
    /**
     * Vykoná akciu pre dané políčko na základe aktuálneho hráča.
     *
     * @param hrac hráč, ktorý vykonáva akciu
     */
    public abstract void vykonajAkciu(Hrac hrac);



}
