package monopoly.mysticcard.karty;

import monopoly.hrac.Hrac;

public class KartaPokladna extends Karta {


    private final int mnozstvo;
    private final boolean ziskat;

    public KartaPokladna(String text, int mnozstvo, boolean ziskat) {
        super(MysticCard.POKLADNA, text);
        this.mnozstvo = mnozstvo;

        this.ziskat = ziskat;
    }

    /**
     * V závislosti od hodnoty ziskat metóda buď pridá množstvo peňazí do peňaženky hráča alebo odobere
     * @param hrac
     */

    public void vykonaj(Hrac hrac) {
        if (this.ziskat) {
            hrac.getPenazenka().addMoney(this.getMnozstvo());
        } else {
            hrac.zaplat(this.getMnozstvo());
        }
    }


    public int getMnozstvo() {
        return this.mnozstvo;
    }


}
