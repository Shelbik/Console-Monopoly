package monopoly.hrac;

public enum Postavicki {
    BATTLESHIP("F:\\Semestralka_INF2\\images\\BATTLESHIP.png"),
    CAR("F:\\Semestralka_INF2\\images\\car.png"),
    THIMBLE("F:\\Semestralka_INF2\\images\\THIMBLE.png"),
    BOOT("F:\\Semestralka_INF2\\images\\pngegg.png"),
    TOP_HAT("F:\\Semestralka_INF2\\images\\icons8-top-hat-48.png");


    private final String image;
    Postavicki(String image) {
        this.image = image;
    }

    public String getObrazok() {
        return this.image;
    }
}
