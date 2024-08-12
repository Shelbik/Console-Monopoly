package monopoly.mysticcard;

import monopoly.hra.Mapa;
import monopoly.hra.Policko;
import monopoly.hrac.Hrac;
import monopoly.mysticcard.karty.Karta;
import monopoly.mysticcard.karty.KartaSanca;
import monopoly.ulice.Jail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class SancaNahodnaKartaZoznam extends Policko implements AkciePreMysticCard {
    private ArrayList<Karta> zoznamKarticiekSanca;
    private ArrayList<Karta> pouziteKartickySanca;
    private final Jail jail;
    private final Mapa mapa;

    public SancaNahodnaKartaZoznam(Jail jail, Mapa mapa) {
        super("Sanca");
        this.zoznamKarticiekSanca = new ArrayList<>();
        this.pouziteKartickySanca = new ArrayList<>();

        this.jail = jail;
        this.mapa = mapa;
        this.zapisKariet();
    }

    /**
     * Metóda "pridajKartuDoZoznamu" slúži na pridanie karty do zoznamu kariet.
     * Ak karta neexistuje, metóda vráti hodnotu "false" na označenie neúspešného pridania karty.
     * <p>
     * Ak karta existuje, vykoná sa nasledujúca logika:
     * 1 - Skontroluje sa, či je karta typu "KartaSanca"
     * 2 - Ak je zoznam kariet "zoznamKarticiekSanca" prázdny, karta sa pridá do zoznamu
     * 3 - Ak zoznam kariet nie je prázdny, skontroluje sa, či sa karta už nachádza v zozname.
     * 3.1 - Ak áno, metóda vráti hodnotu "false" na označenie neúspešného pridania karty.
     * <p>
     * 4 -Ak sa karta nenachádza v zozname, pridá sa do zoznamu.
     *
     * @param karta
     * @return
     */
    @Override
    public boolean pridajKartuDoZoznamu(Karta karta) {
        var popisKartyPokladna1 = Optional.ofNullable(karta);

        if (popisKartyPokladna1.isEmpty()) {
            return false;
        }

        if (karta instanceof KartaSanca) {
            if (this.zoznamKarticiekSanca.isEmpty()) {
                this.zoznamKarticiekSanca.add(karta);
                return true;
            } else {
                if (this.zoznamKarticiekSanca.contains(karta)) {
                    return false;
                } else {
                    this.zoznamKarticiekSanca.add(karta);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Metóda "vymazKartuZoZoznamu" slúži na odstránenie karty zo zoznamu kariet v kontexte hry Monopoly.
     * <p>
     * Metóda najskôr skontroluje, či je karta rôzna od null. Ak karta existuje, vykoná sa nasledujúca logika:
     * 1 - Skontroluje sa, či sa karta nachádza v zozname kariet "zoznamKarticiekSanca".
     * 2 - Ak sa karta nachádza v zozname, odstráni sa z neho a pridá sa do zoznamu "pouziteKartickySanca".
     * 3 - Metóda vráti hodnotu "true" na označenie úspešného odstránenia karty zo zoznamu.
     * <p>
     * Ak karta nie je prítomná v zozname "zoznamKarticiekSanca" alebo ak je karta null, metóda vráti hodnotu "false" na označenie neúspešného odstránenia karty zo zoznamu.
     * <p>
     * Týmto spôsobom je možné vymazať kartu zo zoznamu kariet v kontexte hry Monopoly a pridať ju medzi použité karty.
     *
     * @param karta
     * @return
     */
    @Override
    public boolean vymazKartuZoZoznamu(Karta karta) {
        var kartaSanca = Optional.ofNullable(karta);

        if (kartaSanca.isPresent()) {

            if (this.zoznamKarticiekSanca.contains(karta)) {
                this.zoznamKarticiekSanca.remove(karta);

                this.pouziteKartickySanca.add(karta);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * V tejto metóde sa najskôr skontroluje, či je hráč rôzny od null (teda či je prítomný).
     * Ak je hráč prítomný, vykoná sa nasledujúca logika:
     * <p>
     * 1 - Vytiahne sa karta zo zoznamu kariet.
     * 2 - Vypíše sa popis tejto karty.
     * 3 - Ak je popis karty "Chod do Vazenia", volá sa metóda "vykonajAkciu" na objekte "jail" pre daného hráča.
     * 4 - V opačnom prípade, ak zoznam kariet šance nie je prázdny, hráč získa túto kartu.
     *
     * @param hrac
     */

    @Override
    public void vykonajAkciu(Hrac hrac) {
        if (Optional.ofNullable(hrac).isPresent()) {
            Karta karta = this.vytiahniKartu();
            assert karta != null;

            if (karta.getPopis().equals("Chod do Vazenia")) {
                System.out.println(karta.getPopis());
                this.jail.vykonajAkciu(hrac);
            } else {
                System.out.println(karta.getPopis());
                hrac.ziskajKartu(karta);

            }
        }
    }

    private void zapisKariet() {
        this.pridajKartuDoZoznamu(new KartaSanca("Pokuta za rychlu jazdu, zaplatte 15$", 15, false, this.mapa));
        this.pridajKartuDoZoznamu(new KartaSanca("Zvolili vas predsedom predstavenstva, zaplatte 50$", 50, false, this.mapa));
        this.pridajKartuDoZoznamu(new KartaSanca("Skoncilo vam stavebne sporenie, ziskajte 150", 150, true, this.mapa));
        this.pridajKartuDoZoznamu(new KartaSanca("Posunte sa na policko zssk Petrzalka", 35, this.mapa));
        this.pridajKartuDoZoznamu(new KartaSanca("Posunte sa o tri policka spat", -3, this.mapa));
        this.pridajKartuDoZoznamu(new KartaSanca("Posunte sa na HLAVNE NAMESTIE", 39, this.mapa));
        this.pridajKartuDoZoznamu(new KartaSanca("Posunte sa na ulicu PALISADY", 24, this.mapa));
        this.pridajKartuDoZoznamu(new KartaSanca("Priepustka z vazenia", this.jail, this.mapa));
        this.pridajKartuDoZoznamu(new KartaSanca("Chod do Vazenia", this.jail, this.mapa));
        Collections.shuffle(this.zoznamKarticiekSanca);


    }

    private Karta vytiahniKartu() {

        if (this.zoznamKarticiekSanca.isEmpty()) {
            this.zapisKariet();
            this.pouziteKartickySanca.clear();
        }

        if (!this.zoznamKarticiekSanca.isEmpty()) {
            Karta karta = this.zoznamKarticiekSanca.remove(0);
            this.pouziteKartickySanca.add(karta);
            return karta;
        }

        return null;

    }
}

