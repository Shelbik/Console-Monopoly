package monopoly.mysticcard;

import monopoly.hra.Policko;
import monopoly.hrac.Hrac;
import monopoly.mysticcard.karty.Karta;
import monopoly.mysticcard.karty.KartaPokladna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class PokladnaNahodnaKartaZoznam extends Policko implements AkciePreMysticCard {
    private ArrayList<Karta> zoznamKarticiekPokladna;
    private ArrayList<Karta> pouziteKartickyPokladna;

    public PokladnaNahodnaKartaZoznam() {
        super("Pokladna");
        this.zoznamKarticiekPokladna = new ArrayList<>();
        this.pouziteKartickyPokladna = new ArrayList<>();
        this.zapisKariet();
    }


    public boolean pridajKartuDoZoznamu(Karta karta) {
        if (Optional.ofNullable(karta).isEmpty()) {
            return false;
        } else {

            if (karta instanceof KartaPokladna) {
                if (this.zoznamKarticiekPokladna.isEmpty()) {
                    this.zoznamKarticiekPokladna.add(karta);
                    return true;
                } else {
                    if (this.zoznamKarticiekPokladna.contains(karta)) {
                        return false;
                    } else {
                        this.zoznamKarticiekPokladna.add(karta);
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public boolean vymazKartuZoZoznamu(Karta karta) {
        var popisKartyPokladna1 = Optional.ofNullable(karta);

        if (popisKartyPokladna1.isPresent()) {

            if (this.zoznamKarticiekPokladna.contains(karta)) {
                this.zoznamKarticiekPokladna.remove(karta);

                this.pouziteKartickyPokladna.add(karta);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }


    private Karta vytiahniKartu() {
        if (this.zoznamKarticiekPokladna.isEmpty()) {
            this.zapisKariet();
            this.pouziteKartickyPokladna.clear();
            this.vytiahniKartu();
        } else {
            Karta karta = this.zoznamKarticiekPokladna.get(0);
            this.vymazKartuZoZoznamu(karta);
            return karta;
        }
        return null;
    }

    @Override
    public void vykonajAkciu(Hrac hrac) {
        if (Optional.ofNullable(hrac).isPresent() && !this.zoznamKarticiekPokladna.isEmpty()) {
            Karta karta = this.vytiahniKartu();
            assert karta != null;
            System.out.println("\n" + karta.getPopis());
            hrac.ziskajKartu(karta);

        }
    }

    private void zapisKariet() {
        this.pridajKartuDoZoznamu(new KartaPokladna("Mate narodeniny, dostante 10$ ", 10, true));
        this.pridajKartuDoZoznamu(new KartaPokladna("Vyhrali ste sutaz v krase, dostante 10$ ", 10, true));
        this.pridajKartuDoZoznamu(new KartaPokladna("Bankovy omyl vo vas prospech, dostante 200$ ", 200, true));
        this.pridajKartuDoZoznamu(new KartaPokladna("Poplatky u doktora, musite zaplatit 50$ ", 50, false));
        this.pridajKartuDoZoznamu(new KartaPokladna("Predali ste akcie, dostante 50$ ", 50, true));
        this.pridajKartuDoZoznamu(new KartaPokladna("Vratenie dane z prijmu, dostante 20$ ", 20, true));
        this.pridajKartuDoZoznamu(new KartaPokladna("Ste zdedili, dostante 100$ ", 100, true));
        this.pridajKartuDoZoznamu(new KartaPokladna("Nemocnicne poplatky, musite zaplatit 100$ ", 100, false));
        this.pridajKartuDoZoznamu(new KartaPokladna("Nadisla splatnost vasho fondu, dostante 100$ ", 100, true));
        this.pridajKartuDoZoznamu(new KartaPokladna("Skonci vam zivotna poistka, dostante 100$ ", 100, true));
        this.pridajKartuDoZoznamu(new KartaPokladna("Skolske, musite zaplatit 100$ ", 100, false));
        this.pridajKartuDoZoznamu(new KartaPokladna("Vyberte si 25$ ako poplatok za konzultaciu ", 25, true));
        Collections.shuffle(this.zoznamKarticiekPokladna);
    }
}