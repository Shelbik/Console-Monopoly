package monopoly.ulice;

import monopoly.hrac.Hrac;
import monopoly.hra.Policko;

public class Dan extends Policko {
    private final double kolkoZaplatit;


    public Dan(String nazov, double kolkoZaplatit) {
        super(nazov);
        this.kolkoZaplatit = kolkoZaplatit;

    }

    public double getKolkoZaplatit() {
        return this.kolkoZaplatit;
    }

    /**
     * Táto metóda je zodpovedná za spracovanie akcie, kde hráč zaplatí určitú sumu.
     * V metóde sa najskôr skontroluje, či je hráč rôzny od null.
     * Ak je hráč nie je null, vtedy zaplatí určitú sumu dané.
     * @param hrac
     */
    @Override
    public void vykonajAkciu(Hrac hrac) {
        if (hrac != null) {
            System.out.println("\nZaplatili ste dan = " + this.getKolkoZaplatit());
            hrac.zaplat(this.getKolkoZaplatit());
        }
    }
}
