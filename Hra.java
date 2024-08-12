package monopoly.hra;

import monopoly.hrac.Hrac;
import monopoly.hrac.Postavicki;
import monopoly.ulice.Jail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Hra {
    private int pocetHracov;
    private List<Hrac> zoznamHracov;
    private final Kocka kocka = new Kocka();
    private final Mapa mapa;

    private int indexAktualnehoHraca = 0;
    private Scanner scanner;

    public Hra(int pocetHracov) {

        this.pocetHracov = pocetHracov;
        this.zoznamHracov = new ArrayList<>();
        this.mapa = new Mapa(this.kocka);
        this.scanner = new Scanner(System.in);

        this.inicializaciaHracov();
        this.zobrazitMenuPreHru(this.zoznamHracov);


    }
    /**
     * Zobrazí menu pre konkrétneho hráča.
     *
     * @param hrac Hráč, pre ktorého sa zobrazuje menu.
     */
    private void menuPreKonretnehoHraca(Hrac hrac) {


        if (Optional.ofNullable(hrac).isPresent()) {
            System.out.println("\nTeraz je na rade hrac " + hrac.getMeno());

            int vyber;
            if (hrac.jeVoVazeni()) {
                this.vypisMenuPreHracaKtorzJeVoVazeni(hrac);

            }
            if (!hrac.jeVoVazeni()) {


                String sb = "\nConsole menu pre hraca " + hrac.getMeno() +
                        "\n 1 - Spravit Krok" +
                        "\n 2 - Vypis o aktualnom kredite" +
                        "\n 3 - Nakup nehnutelnosti od konretneho hraca " +
                        "\n 4 - Nastavenie ceny pre konretnu nehnutelnost" +
                        "\n 5 - Vypis nazov policka na ktorom aktualne sa nachadzam" +
                        "\n 6 - Vypisat zoznam vsetkej mojej nehnutelnosti" +
                        "\n 7 - Predat nehnutelnost na ipoteku" +
                        "\n 8 - Zistit predajnu cenu nejakej ulice od konretneho hraca" +
                        "\n 9 - Ukoncenie hry";

                System.out.println(sb);
                while (!hrac.jeVoVazeni()) {

                    vyber = this.scanner.nextInt();

                    switch (vyber) {
                        case 1 -> this.hracRobiKrok(hrac);
                        case 2 -> {
                            System.out.println(hrac.getMeno() + " Vas aktualny kredit -> " + hrac.getPenazenka().getKredit());
                            this.menuPreKonretnehoHraca(hrac);
                        }

                        //nakup nehnutelnosti
                        case 3 -> {
                            System.out.println("Napiste meno hraca od ktoreho chcete kupit nehnutelnost");
                            var menoHracaPrePredaj = this.scanner.next();
                            System.out.println("Napiste nazov nehnutetelnosti ktoru chcete kupit");
                            var nazovNehnutel = this.scanner.next();
                            var predajcaNehnutelnost = this.hladanieHracaPodlaMena(menoHracaPrePredaj);
                            assert predajcaNehnutelnost != null;
                            predajcaNehnutelnost.nakupInymHracomMojejNehnutelnosti(hrac, nazovNehnutel);
                            this.menuPreKonretnehoHraca(hrac);
                        }
                        // nastavenie ceny pre nehnutelnost
                        case 4 -> {
                            System.out.println("Napiste nazov nehnutelnosti ktorej chcete nastavit cenu");
                            var nazovNehnutelnosti = this.scanner.next();
                            System.out.println("Napiste cenu nehnutetelnosti za ktoru ste ochotny predat danu ulicu");
                            var cenaNehnutelnosti = this.scanner.nextInt();
                            hrac.nastavenieCenyPreKonretnuNehnutelnost(nazovNehnutelnosti, cenaNehnutelnosti);
                            this.menuPreKonretnehoHraca(hrac);
                        }
                        case 5 -> {
                            this.vypisAkutalnehoPolickaPreHraca(hrac);
                            this.menuPreKonretnehoHraca(hrac);
                        }
                        case 6 -> {
                            hrac.vypisatNehnutelnost();
                            this.menuPreKonretnehoHraca(hrac);
                        }
                        case 7 -> {
                            System.out.println("Napiste nazov nehnutelnosti ktoru chcete predat na ipoteku");
                            var nazovNehnutelnosti = this.scanner.next();
                            hrac.predajNehnutelnostiNaIpoteku(nazovNehnutelnosti);

                        }
                        case 8 -> {
                            System.out.println("Napiste meno hraca ktory vlastni ulicu");
                            var menoHracaPrePredaj = this.scanner.next();
                            System.out.println("Napiste nazov nehnutetelnosti ktoru chcete kupit");
                            var nazovNehnutel = this.scanner.next();
                            var predajcaNehnutelnost = this.hladanieHracaPodlaMena(menoHracaPrePredaj);
                            assert predajcaNehnutelnost != null;

                            hrac.zistenieCenyKonretnejUliceKonretnehoHraca(predajcaNehnutelnost, nazovNehnutel);

                        }


                        case 9 -> {
                            hrac.vzdatSa();
                            this.zoznamHracov.remove(hrac);
                            this.zobrazitMenuPreHru(this.zoznamHracov);


                        }
                        default -> {
                            System.out.println("Taka moznost neexistuje, skuste este raz");
                            this.menuPreKonretnehoHraca(hrac);
                        }
                    }
                }
            }
        }
    }
    /**
     * Hráč vykoná krok v hre.
     *
     * @param hrac Hráč, ktorý vykonáva krok.
     */
    private void hracRobiKrok(Hrac hrac) {
        boolean mozeDalsiKrok = true;

        while (mozeDalsiKrok) {
            hrac.spravKrok(this.kocka);
            this.mapa.vykonaj(hrac);
            this.vypisAkutalnehoPolickaPreHraca(hrac);

            if (hrac.getKolkoKratPadloParneCislo() > 0) {
                this.menuPreKonretnehoHraca(hrac);
            } else {
                if (this.indexAktualnehoHraca < this.zoznamHracov.size() - 1 ) {
                    this.indexAktualnehoHraca++;

                } else {
                    this.indexAktualnehoHraca = 0;

                }
                this.zobrazitMenuPreHru(this.zoznamHracov);
            }

            mozeDalsiKrok = hrac.getKolkoKratPadloParneCislo() > 0;
        }

     //   this.zobrazitMenuPreHru(this.zoznamHracov);
    }
    /**
     * Zobrazí menu pre konkrétneho hráča ktorý je vo vazeni.
     *
     * @param hrac Hráč, pre ktorého sa zobrazuje menu.
     */
    private void vypisMenuPreHracaKtorzJeVoVazeni(Hrac hrac) {
        int vyber;
        System.out.println("\n" + hrac.getMeno() +  " STE VO VAZENI");
        String vazeniMenu = "\n 1 - Pokus vyjst z vazena zadarmo" +
                "\n 2 - Zaplatit vypustneie z vazenia";

        System.out.println(vazeniMenu);
        vyber = this.scanner.nextInt();
        var jail = (Jail)this.mapa.getPolickoPodlaIndexu(10);
        switch (vyber) {
            case 1 -> {
                jail.vypustiHracaZadarmo(hrac);
                if (this.indexAktualnehoHraca < this.zoznamHracov.size() - 1) {
                    this.indexAktualnehoHraca++;
                } else {
                    this.indexAktualnehoHraca = 0;
                }
                this.zobrazitMenuPreHru(this.zoznamHracov);
            }
            case 2 -> jail.hracPlatiZaVypustnie(hrac);
        }
    }
    /**
     * Vypíše aktuálne políčko, na ktorom sa hráč nachádza.
     *
     * @param hrac Hráč, ktorého aktuálne políčko sa vypisuje.
     */
    private void vypisAkutalnehoPolickaPreHraca(Hrac hrac) {
        System.out.println("Aktualne ste na policku " + this.mapa.getPolickoPodlaIndexu(hrac.getPozicia()).getNazov());
    }
    /**
     * Zobrazí menu pre hru, v ktorom je zahrnuté menu pre konkrétnych hráčov.
     *
     * @param zoznam Zoznam hráčov v hre.
     */
    private void zobrazitMenuPreHru(List<Hrac> zoznam) {
        boolean isRunning = true;
        while (isRunning) {
            //vratit na jedna
            if (zoznam.size() == 1) {
                isRunning = false;
                this.scanner.close();
                System.out.println("VYHRAL HRAC " + zoznam.get(this.indexAktualnehoHraca).getMeno());
                System.out.println("GRATULUJEM");

            } else {
                this.menuPreKonretnehoHraca(zoznam.get(this.indexAktualnehoHraca));
            }
        }
    }

    /**
     * Hľadá hráča v zozname podľa mena.
     *
     * @param meno Meno hráča, ktorého hľadáme.
     * @return Nájdený hráč alebo null, ak sa nenájde.
     */
    private Hrac hladanieHracaPodlaMena(String meno) {
        for (Hrac hrac : this.zoznamHracov) {
            if (hrac.getMeno().equals(meno)) {
                return hrac;
            }
        }
        return null;
    }

    /**
     * Inicializuje hráčov podľa počtu hráčov zadaného pri vytváraní hry.
     */
    private void inicializaciaHracov() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nV zozname su nasledujuce postavicki:").append("BATTLESHIP,").append(" CAR,").append(" THIMBLE,").append(" BOOT,").append(" TOP_HAT.");

        for (int i = 0; i < this.pocetHracov; i++) {
            Scanner sc = new Scanner(System.in);
            System.out.println("\nNapiste meno ako sa bude volat vas hrac");
            var menoHraca = sc.next();
            System.out.println(sb);
            System.out.println("Napiste nazov postavicki ktore su uvedene vyssie");
            var postavicka = sc.next();
            int prvyIndex = sb.indexOf(postavicka.toUpperCase());

            int druhyIndex = prvyIndex + (postavicka.length() + 2);

            sb.delete(prvyIndex, druhyIndex);

            Hrac hrac = new Hrac(menoHraca, Postavicki.valueOf(postavicka.toUpperCase()));

            this.zoznamHracov.add(hrac);


        }
    }
}
