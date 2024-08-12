package monopoly.ulice;

public class Zssk extends Nehnutelnost {

    public Zssk(String nazov, int cena, boolean jeVykupena) {
        super(nazov, cena, 0, jeVykupena);
    }

    /**
     * Slúži na nastavenie výšky nájomného pre zssk na základe počtu staníc, ktoré má vlastník daného objektu.
     */
    public void nastavenieNajomnychPodlaPoctuStanic() {
        switch (this.getVlastnik().getPocetStanic()) {
            case 1 -> this.setNajomne(25);
            case 2 -> this.setNajomne(50);
            case 3 -> this.setNajomne(100);
            case 4 -> this.setNajomne(200);
            default -> this.setNajomne(0);
        }
    }


}
