package monopoly.ulice;

import monopoly.hra.Kocka;
import monopoly.hra.Policko;
import monopoly.hrac.Hrac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Jail extends Policko {
    private List<HashMap<Hrac, Integer>> zoznamHracov;
    private final Kocka kocka;


    public Jail(Kocka kocka) {
        super("Vazen");
        this.kocka = kocka;
        this.zoznamHracov = new ArrayList<>();
    }

    /**
     * Táto metóda slúži na umiestnenie hráča do väzenia, ak nie je už v ňom.
     * Ak zoznam hráčov neobsahuje hráča, ktorý má ísť do väzenia, pridá sa hráč do HashMap s hodnotou 0.
     * Dána hodnota reprezentuje, koľko kôl je daný hráč vo väzení.
     * @param hrac
     */

    @Override
    public void vykonajAkciu(Hrac hrac) {
        var optionalHrac = Optional.ofNullable(hrac);
        HashMap<Hrac, Integer> hracIduceDoVazenia = new HashMap<>();
        if (optionalHrac.isPresent()) {
            if (this.zoznamHracov.contains(hracIduceDoVazenia)) {
                System.out.println(hrac.getMeno() + " uz je vo vazeni");
            } else {
                hracIduceDoVazenia.put(hrac, 0);
                this.zoznamHracov.add(hracIduceDoVazenia);
                hrac.setJeVoVazeni(true);
                System.out.println("Hraca " + hrac.getMeno() + " dali sme do vazena");
            }

        }
    }

    /**
     * Metóda "funkciaPreVypustenieHraca" slúži na vykonanie konkrétnej akcie pre daného hráča súvisiacej s vypustením z väzenia.
     * Ak sa hráč nachádza v zozname a zároveň má kartu na vypustenie z väzenia, vykoná sa nasledujúce:
     * -hráč použije kartu na vypustenie
     * -hráč sa odstráni zo zoznamu hráčov ktoré sú vo väzeni
     * -hodnota atribútu "jeVoVazeni" hráča sa nastaví na false
     *
     * Ak hráč nie je prítomný v zozname alebo nemá kartu na vypustenie, nasleduje ďalšia logika:
     * -vykoná sa hod kockou
     * -ak padlo párne číslo, hráč sa odstráni zo zoznamu hráčov a jeho atribút "jeVoVazeni" sa nastaví na false.
     *
     * Ak nepadlo párne číslo, vykoná sa nasledujúce:
     * -hráčovi sa pridá hodnota 1 k počtu kolies vo väzení.
     * -ak hráč dosiahol hodnotu 3 vo väzení, hráč zaplatí sumu 50
     * -odstráni sa zo zoznamu hráčov a jeho atribút "jeVoVazeni" sa nastaví na false.
     * @param hrac
     */
    public void vypustiHracaZadarmo(Hrac hrac) {
        this.funkciaPreVypusteniaHraca(hrac);
    }

    /**
     * Metóda "hracPlatiZaVypustenie" slúži na spracovanie platby hráča za vypustenie z väzenia.
     * V metóde sa najskôr skontroluje, či je hráč prítomný (nie je null)
     * Ak nie je null hráč zaplatí sumu 50
     * Hráč sa odstráni zo zoznamu hráčov.
     * Atribút "jeVoVazeni" hráča sa nastaví na false.
     * Hráč vykoná krok na hracej doske.
     * @param hrac
     */
    public void hracPlatiZaVypustnie(Hrac hrac) {
        if (Optional.ofNullable(hrac).isPresent()) {
            hrac.zaplat(50);
            this.zoznamHracov.remove(this.hladanieHracaVZozname(hrac));
            hrac.setJeVoVazeni(false);


        }
    }


    private void funkciaPreVypusteniaHraca(Hrac hrac) {


        HashMap<Hrac, Integer> hladanyHrac = this.hladanieHracaVZozname(hrac);
        if (this.zoznamHracov.contains(this.hladanieHracaVZozname(hrac)) && hrac.maKartuNaVypustenieZVazenia()) {
            hrac.pouzilKartuNaVypustenieZVazenia();
            this.zoznamHracov.remove(this.hladanieHracaVZozname(hrac));
            hrac.setJeVoVazeni(false);
            System.out.println("\nGratulujem " + hrac.getMeno() + " ste uspešne zadarmo vysli z Vazenie pomocou Priepustky z VAZENIA");
        } else {
            this.kocka.hodKocku();
            if (this.kocka.padloParneCislo()) {
                this.zoznamHracov.remove(this.hladanieHracaVZozname(hrac));
                hrac.setJeVoVazeni(false);
                System.out.println("\nGratulujem " + hrac.getMeno() + " ste uspešne zadarmo vysli z Vazenie lebo vam padlo parne cislo");
            } else {

                int index = this.zoznamHracov.indexOf(hladanyHrac);
                var count = this.zoznamHracov.get(index).values().iterator().next();
                System.out.println(hrac.getMeno() + " nepadlo vam parne cislo, zostavate vo vazeni");

                // hrac nemoze byt vo vazeni viac ako 3
                this.zoznamHracov.get(index).put(hrac, count + 1);
                if (this.zoznamHracov.get(index).containsValue(3)) {
                    hrac.zaplat(50);
                    this.zoznamHracov.remove(this.hladanieHracaVZozname(hrac));
                    hrac.setJeVoVazeni(false);
                    System.out.println("\nGratulujem " + hrac.getMeno() + " ste uspešne vysli z Vazenie, zaplatili ste za to 50$");
                }


            }
        }
    }


    private HashMap<Hrac, Integer> hladanieHracaVZozname(Hrac hrac) {
        for (HashMap<Hrac, Integer> hraci : this.zoznamHracov) {
            if (hraci.containsKey(hrac)) {
                return hraci;
            }
        }
        return null;
    }
}
