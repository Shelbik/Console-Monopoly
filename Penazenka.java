package monopoly.hrac;

public class Penazenka {
    private double kredit;

    public Penazenka() {
        this.kredit = 1500;
    }

    /**
     * Metóda addMoney(double value) slúži na pridanie peňazí (kreditu) k existujúcemu kreditu hráča.
     *
     * @param value
     * @return
     */
    public boolean addMoney(double value) {
        if (value > 0) {
            this.kredit += value;
            return true;
        }
        return false;
    }

    /**
     * Metóda takeMoneyAway(double value) slúži na odobratie peňazí (kreditu) z existujúceho kreditu hráča.
     * @param value
     * @return
     */
    public boolean takeMoneyAway(double value) {
        if (value <= this.kredit) {
            this.kredit -= value;
            return true;
        } else {
            System.out.println("Nemate tolko peniazi");
            return false;
        }

    }

    public double getKredit() {
        return this.kredit;
    }
}
