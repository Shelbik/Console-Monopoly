package monopoly.hra;

public class Kocka {

    private int firstRoll;
    private int secondRoll;

    public void hodKocku() {
        this.firstRoll = (int)(Math.random() * 6 + 1);
        this.secondRoll = (int)(Math.random() * 6 + 1);

    }

    /**
     * Vráti súčet hodov oboch kociek.
     *
     * @return súčet hodov
     */
    public int getSucetHodov() {
        return this.firstRoll + this.secondRoll;
    }
    /**
     * Skontroluje, či padlo párne číslo.
     *
     * @return true, ak padlo párne číslo, false inak
     */
    public boolean padloParneCislo() {
        return this.firstRoll == this.secondRoll;
    }
    /**
     * Vráti hodnotu  hodu druhej kocky.
     *
     * @return hodnota  hodu druhej kocky
     */
    public int getHonotaHoduDruhejKocky() {
        return this.secondRoll;
    }

    /**
     * Vráti hodnotu  hodu prvej kocky.
     *
     * @return hodnota  hodu prvej kocky
     */
    public int getFirstRoll() {
        return this.firstRoll;
    }



}