package monopoly.ulice;

import monopoly.hrac.Hrac;
import monopoly.hra.Policko;

public class Start extends Policko  {
    public Start() {
        super( "Start");
    }

    /**
     * Celkovo táto metóda slúži na pridanie peňazí do peňaženky hráča za jedno kolo.
     * @param hrac
     */
    @Override
    public void vykonajAkciu(Hrac hrac) {
        try {
            double pocetPeniaziZaJednoPrejdeneKolo = 200;
            hrac.getPenazenka().addMoney(pocetPeniaziZaJednoPrejdeneKolo);

        } catch (Exception e) {
            System.out.println("Taky hrac neexistuje");
        }
    }
}
