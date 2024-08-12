package monopoly.mysticcard.karty;

import monopoly.hrac.Hrac;

import java.util.HashMap;

public abstract class Karta {
    private MysticCard mysticCard;
    private String popis;
    private final HashMap<MysticCard, String> karta = new HashMap<>();


    public Karta(MysticCard format, String popis) {
        this.mysticCard = format;
        this.popis = popis;

        this.karta.put(this.mysticCard, this.popis);
    }


    public HashMap<MysticCard, String> getKarta() {
        return this.karta;
    }

    public MysticCard getKey() {
        return this.mysticCard;
    }

    /**
     * Abstraktná metóda vykonaj(Hrac hrac) je definovaná v tejto triede, ale nemá implementáciu.
     * Očakáva sa, že potomkovia tejto triedy implementujú túto metódu a vykonajú príslušnú akciu pre konkrétnu kartu.
     * @param hrac
     */
    public abstract void vykonaj( Hrac hrac);

    public String getPopis() {
        return this.popis;
    }


}
