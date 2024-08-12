package monopoly.ulice;

import monopoly.hra.Kocka;

public class KomunalneSluzby extends Nehnutelnost {
    private final Kocka kocka;

    public KomunalneSluzby(String nazov, int cena, boolean jeVykupena) {
        super(nazov, cena, 0, jeVykupena);
        this.kocka = new Kocka();

    }

    /**
     * Slúži na nastavenie ceny nehnuteľnosti na základe počtu komunálnych služieb, ktoré vlastník danej nehnuteľnosti má.
     * V tele metódy sa najprv hodí herná kocka.
     * Následne sa získa súčet hodov z kocky pomocou metódy "getSucetHodov()".
     * Ak vlastník má 1 komunálnu službu, cena nehnuteľnosti sa nastaví na súčet hodov z kocky násobený 4.
     * Pre 2 komunálne služby je cena nastavená na súčet hodov násobený 10.
     * V prípade, že počet komunálnych služieb nie je ani jednou z týchto hodnôt, cena nehnuteľnosti sa nastaví na 0.
     */
    public void nastavenieCenyPodlaPoctuSluzieb() {
        this.kocka.hodKocku();
        var cena = this.kocka.getSucetHodov();
        switch (this.getVlastnik().getPocetKomunalnychSluzbi()) {
            case 1 -> this.setNajomne(cena * 4);
            case 2 -> this.setNajomne(cena * 10);
            default -> this.setNajomne(0);
        }
    }
}
