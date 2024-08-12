package monopoly.hra;

import monopoly.hrac.Hrac;
import monopoly.NespravnyFormatException;
import monopoly.mysticcard.PokladnaNahodnaKartaZoznam;
import monopoly.mysticcard.SancaNahodnaKartaZoznam;
import monopoly.ulice.Casino;
import monopoly.ulice.CestaDoVazena;
import monopoly.ulice.Dan;
import monopoly.ulice.FarbyUlice;
import monopoly.ulice.Jail;
import monopoly.ulice.KomunalneSluzby;
import monopoly.ulice.Start;
import monopoly.ulice.Ulica;
import monopoly.ulice.Zssk;

import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

/**
 * Trieda Mapa predstavuje hernú mapu Monopoly.
 * Obsahuje polia pre jednotlivé políčka na mape, ako aj objekty reprezentujúce herné prvky, ako sú väzenie, kocka,
 * zoznam náhodných kariet a ďalšie herné prvky.
 */

public class Mapa {
    private final Policko[] mapa = new Policko[40];

    private final Jail jail;
    private final PokladnaNahodnaKartaZoznam pokladnaNahodnaKartaZoznam;
    private final SancaNahodnaKartaZoznam sancaNahodnaKartaZoznam;
    private final Kocka kocka;



    /**
     * Konštruktor triedy Mapa.
     *
     * @param kocka kocka používaná na hody
     */
    public Mapa(Kocka kocka) {
        this.kocka = kocka;
        this.jail = new Jail(this.kocka);

        this.pokladnaNahodnaKartaZoznam = new PokladnaNahodnaKartaZoznam();
        this.sancaNahodnaKartaZoznam = new SancaNahodnaKartaZoznam(this.jail, this);
        this.vytvorenieMapy();

    }

    /**
     * Vytvorí hernú mapu na základe súboru Ulice.txt.
     */
    private void vytvorenieMapy() {

        int cisloObjectu = 0;
        var subor = ClassLoader.getSystemResourceAsStream("Ulice.txt");
        Scanner scanner = new Scanner(Objects.requireNonNull(subor));
        boolean jeVupena = false;
        int riadokVTxt = 0;
        this.vytvaranieMapyPodlaScanner(cisloObjectu, scanner, jeVupena, riadokVTxt);
    }

    /**
     * Vykoná akciu pre daného hráča na aktuálnom políčku.
     *
     * @param hrac hráč, ktorému sa vykonáva akcia
     */
    public void vykonaj(Hrac hrac) {
        if (Optional.ofNullable(hrac).isPresent()) {
            this.mapa[hrac.getPozicia()].vykonajAkciu(hrac);
        }
    }

    /**
     * Vráti políčko na mape na základe jeho indexu.
     *
     * @param index index políčka
     * @return políčko na základe indexu
     */

    public Policko getPolickoPodlaIndexu(int index) {
        return this.mapa[index];
    }

    /**
     * Vytvára hernú mapu na základe vstupu zo scanneru.
     *
     * @param cisloObjectu  číslo objektu
     * @param scanner       scanner na čítanie zo vstupu
     * @param jeVupena      indikátor, či je políčko vypené
     * @param riadokVTxt    riadok vo vstupnom súbore
     */
    private void vytvaranieMapyPodlaScanner(int cisloObjectu, Scanner scanner, boolean jeVupena, int riadokVTxt) {
        String farbaUlice;
        int cenaZaJedenDom;
        String nazov;
        int cena;
        int najomne;
        while (scanner.hasNextLine() && cisloObjectu < this.mapa.length) {
            var slovo = scanner.nextLine();
            try {


                switch (slovo) {
                    case "start" -> {
                        riadokVTxt++;
                        this.mapa[cisloObjectu] = new Start();
                        cisloObjectu++;
                    }
                    case "ulica" -> {
                        riadokVTxt++;
                        nazov = scanner.nextLine();
                        riadokVTxt++;
                        farbaUlice = scanner.nextLine();
                        riadokVTxt++;
                        najomne = scanner.nextInt();
                        riadokVTxt++;
                        cena = scanner.nextInt();
                        riadokVTxt++;
                        cenaZaJedenDom = scanner.nextInt();
                        riadokVTxt++;
                        this.mapa[cisloObjectu] = new Ulica(nazov, FarbyUlice.valueOf(farbaUlice.toUpperCase()), najomne, cena, cenaZaJedenDom, jeVupena);
                        cisloObjectu++;
                    }
                    case "jail" -> {
                        riadokVTxt++;
                        this.mapa[cisloObjectu] = this.jail;
                        cisloObjectu++;
                    }
                    case "dan" -> {
                        nazov = scanner.nextLine();
                        riadokVTxt++;
                        var kolkoZaplatit = scanner.nextDouble();
                        riadokVTxt++;
                        this.mapa[cisloObjectu] = new Dan(nazov, kolkoZaplatit);
                        cisloObjectu++;

                    }
                    case "pokladna" -> {
                        riadokVTxt++;
                        this.mapa[cisloObjectu] = this.pokladnaNahodnaKartaZoznam;
                        cisloObjectu++;
                    }
                    case "sanca" -> {
                        riadokVTxt++;
                        this.mapa[cisloObjectu] = this.sancaNahodnaKartaZoznam;
                        cisloObjectu++;
                    }
                    case "zssk" -> {
                        nazov = scanner.nextLine();
                        riadokVTxt++;
                        cena = scanner.nextInt();
                        riadokVTxt++;
                        this.mapa[cisloObjectu] = new Zssk(nazov, cena, jeVupena);
                        cisloObjectu++;
                    }
                    case "komunalne" -> {
                        nazov = scanner.nextLine();
                        riadokVTxt++;
                        cena = scanner.nextInt();
                        riadokVTxt++;
                        this.mapa[cisloObjectu] = new KomunalneSluzby(nazov, cena, jeVupena);
                        cisloObjectu++;
                    }
                    case "casino" -> {
                        riadokVTxt++;
                        this.mapa[cisloObjectu] = new Casino();
                        cisloObjectu++;
                    }
                    case "cesta" -> {
                        riadokVTxt++;
                        this.mapa[cisloObjectu] = new CestaDoVazena(this.jail);
                        cisloObjectu++;
                    }
                }
            } catch (Exception e) {
                throw new NespravnyFormatException(cisloObjectu, riadokVTxt);
            }

        }
    }
}


