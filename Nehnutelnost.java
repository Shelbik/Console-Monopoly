package monopoly.ulice;

import monopoly.hra.Policko;
import monopoly.hrac.Hrac;

import java.util.Optional;
import java.util.Scanner;

public abstract class Nehnutelnost extends Policko {

    private final int cenaPriNakupe;
    private boolean jeVykupena;
    private int najomne;

    private int cenaNaPredaj = 0;

    private Hrac vlastnik;

    public Nehnutelnost(String nazov, int cena, int najomne, boolean jeVykupena) {
        super(nazov);
        this.cenaPriNakupe = cena;
        this.najomne = najomne;
        this.jeVykupena = jeVykupena;
    }

    public int getCenaPriNakupe() {
        return this.cenaPriNakupe;
    }

    public boolean isJeVykupena() {
        return this.jeVykupena;
    }

    public int getCenaNaPredaj() {
        return this.cenaNaPredaj;
    }

    public void setCenaNaPredaj(int cenaNaPredaj) {
        this.cenaNaPredaj = cenaNaPredaj;
    }

    public void setJeVykupena(boolean jeVykupena) {
        this.jeVykupena = jeVykupena;
    }

    public Hrac getVlastnik() {
        return this.vlastnik;
    }

    public void setVlastnik(Hrac vlastnik) {
        this.vlastnik = vlastnik;
    }


    public int getNajomne() {
        return this.najomne;
    }

    public void setNajomne(int najomne) {
        this.najomne = najomne;
    }

    /**
     * Ak je nehnuteľnosť nevykúpená, hráč je vyzvaný na kúpu nehnuteľnosti.
     * V závislosti od typu nehnuteľnosti sa vypíše príslušná správa o možnosti kúpy a cene pri nákupe.
     * Ak odpoveď je "ano", hráč kupuje nehnuteľnosť pomocou metódy "kupNehnutetlnost()".
     * Následne sa vypíše správa o úspešnom nákupe s menom hráča a názvom nehnuteľnosti.
     * @param hrac
     */
    @Override
    public void vykonajAkciu(Hrac hrac) {
        if (Optional.ofNullable(hrac).isPresent()) {

            if (this.isJeVykupena()) {
                if (this.vlastnik != hrac) {
                    hrac.zaplat(this.getNajomne());
                    System.out.println(hrac.getMeno() + ", ZAPLATILI STE " + this.getNajomne() + "$ za prenajm");
                } else {
                    System.out.println("\n Neplatite nic, ste majitel daneho policka");
                }
            } else {
                Scanner scanner = new Scanner(System.in);
                switch (this.getClass().getSimpleName()) {
                    case "Ulica" ->
                            System.out.println("Chcete si kupit ulicu " + this.getNazov() + " za " + this.getCenaPriNakupe() + "$ ?");
                    case "Zssk" ->
                            System.out.println("Chcete si kupit zssk " + this.getNazov() + " za " + this.getCenaPriNakupe() + "$ ?");
                    case "KomunalneSluzby" ->
                            System.out.println("Chcete si kupit komunalnu sluzbu " + this.getNazov() + " za " + this.getCenaPriNakupe() + "$ ?");

                }
                String odpoved = scanner.nextLine();
                if (odpoved.equals("ano")) {
                    hrac.kupNehnutetlnost(this);
                    switch (this.getClass().getSimpleName()) {
                        case "Ulica" ->
                                System.out.println("\nGRATULUJEM " + hrac.getMeno() + ", USPESNE STE KUPILI ULICU " + this.getNazov());
                        case "Zssk" ->
                                System.out.println("\nGRATULUJEM " + hrac.getMeno() + ", USPESNE STE KUPILI ZSSK " + this.getNazov());
                        case "KomunalneSluzby" ->
                                System.out.println("\nGRATULUJEM " + hrac.getMeno() + ", USPESNE STE KUPILI KOMUNALNU SLUZBU " + this.getNazov());
                    }
                }
            }
        }
    }
}
