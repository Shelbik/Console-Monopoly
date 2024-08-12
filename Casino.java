package monopoly.ulice;

import monopoly.hra.Kocka;
import monopoly.hra.Policko;
import monopoly.hrac.Hrac;

import java.util.Optional;
import java.util.Scanner;

public class Casino extends Policko {
    private final Kocka kocka;

    public Casino() {
        super("Casino");
        this.kocka = new Kocka();

    }

    /**
     * V tejto metóde sa najskôr skontroluje, či je hráč rôzny od null.
     * Ak je hráč prítomný, vypíše sa vítanie hráča v kasíne.
     * Následne sa vytvorí inštancia triedy Scanner na načítanie odpovede hráča.
     * <p>
     * Po načítaní odpovede sa spustí metóda "zobrazenieMenuPreCasino", ktorá zobrazuje menu pre hráča v kasíne.
     * Táto metóda dostáva ako parametre hráča, inštanciu Scanner a odpoveď hráča.
     * Týmto spôsobom je hráčovi umožnené interagovať s kasínom a vykonávať príslušné akcie.
     */
    @Override
    public void vykonajAkciu(Hrac hrac) {
        if (Optional.ofNullable(hrac).isPresent()) {
            String casinoVytanie = "\nVytame Vas v Casino." +
                    " Mate zaujem zahrat si ?";

            System.out.println(casinoVytanie);
            Scanner scanner = new Scanner(System.in);
            String odpoved = scanner.next();
            odpoved.toLowerCase();

            this.zobrazenieMenuPreCasino(hrac, scanner, odpoved);
        }
    }

    private void zobrazenieMenuPreCasino(Hrac hrac, Scanner scanner, String odpoved) {
        if (odpoved.equals("ano")) {
            System.out.println("\nNapiste cislo ktore si myslite ze nahodne vypadne od 1 az 6");
            int cislo = scanner.nextInt();
            System.out.println("Napiste vasu stavku");
            int stavka = scanner.nextInt();
            this.kocka.hodKocku();

            if (this.kocka.getFirstRoll() == cislo) {
                System.out.println("\nGRATULUJEM, VYHRALI STE");
                hrac.getPenazenka().addMoney(stavka * 2);

            } else {
                hrac.zaplat(stavka);
                System.out.println("PREHRALI STE, CISLO KTORE PADLO = " + this.kocka.getFirstRoll());
            }

        }
    }

}



