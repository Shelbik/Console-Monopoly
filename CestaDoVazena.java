package monopoly.ulice;

import monopoly.hrac.Hrac;
import monopoly.hra.Policko;

public class CestaDoVazena extends Policko {
    private Jail jail;

    public CestaDoVazena( Jail jail) {
        super("Cesta do vazena");
        this.jail = jail;
    }


    /**
     * Metóda "vykonajAkciu" deleguje vykonanie akcie na inú triedu "jail" pre daného hráča.
     * Metóda slúži na umiestnenie hráča do väzenia.
     * @param hrac
     */

    @Override
    public void vykonajAkciu(Hrac hrac) {
        this.jail.vykonajAkciu(hrac);

    }
}
