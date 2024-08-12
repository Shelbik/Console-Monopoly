package monopoly.mysticcard.karty;

import monopoly.hra.Mapa;
import monopoly.hrac.Hrac;
import monopoly.ulice.Jail;

public class KartaSanca extends Karta {


    private boolean ziskat;
    private int cislo;

    private int indexPolicka;
    private Mapa mapa;
    private Jail jail;


    public KartaSanca(String text, int cislo, Boolean ziskat, Mapa mapa) {
        super(MysticCard.SANCA, text);
        this.ziskat = ziskat;
        this.cislo = cislo;

        this.mapa = mapa;
    }

    public KartaSanca(String text, int indexPolicka, Mapa mapa) {
        super(MysticCard.SANCA, text);
        this.indexPolicka = indexPolicka;
        this.mapa = mapa;

    }


    public KartaSanca(String text, Jail jail, Mapa mapa) {
        super(MysticCard.SANCA, text);


        this.jail = jail;
        this.mapa = mapa;
    }

    /**
     * Podľa vlastností karty sa vykonávajú rôzne akcie, ako napríklad posun hráča na určité políčko, získanie alebo stratenie peňazí, ziskanie karty väzenia atď.
     * @param hrac
     */
    @Override
    public void vykonaj(Hrac hrac) {
        if (this.indexPolicka > 0) {
            hrac.setPozicia(this.indexPolicka);
            this.mapa.vykonaj(hrac);
        } else if (this.indexPolicka < 0 && this.getPopis().equals("Posunte sa o tri policka spat")) {
            int currentPozicia = hrac.getPozicia();
            if (currentPozicia > 2) {
                currentPozicia += this.indexPolicka;
                hrac.setPozicia(currentPozicia);
                this.mapa.vykonaj(hrac);
            } else {
                currentPozicia += this.indexPolicka;
                currentPozicia = 40 + currentPozicia;
                hrac.setPozicia(currentPozicia);
                this.mapa.vykonaj(hrac);

            }
        } else if (this.getPopis().equals("Priepustka z vazenia")) {
            hrac.ziskajKartu(this);
            System.out.println(this.getPopis());
        } else if (this.getPopis().equals("Chod do Vazenia")) {
            this.jail.vykonajAkciu(hrac);
            System.out.println(this.getPopis());
        } else {
            if (this.ziskat) {
                hrac.getPenazenka().addMoney(this.getCislo());
                System.out.println("Ziskali ste " + this.getCislo() + "$");
            } else {
                hrac.zaplat(this.getCislo());
                System.out.println("Zaplatili ste " + this.getCislo() + "$");
            }
        }
    }


    public int getCislo() {
        return this.cislo;
    }
}
