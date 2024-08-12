package monopoly.ulice;

public enum FarbyUlice {
    ZLTA("zlta"),
    ZELENA("zelena"),
    MODRA("modra"),
    FIALOVA("fialova"),
    ORANZOVA("oranzova"),
    SEDA("seda"),
    HNEDA("hneda"),
    CERVENA("cervena");

    private final String farba;

    FarbyUlice(String farba) {
        this.farba = farba;
    }

    public String getFarba() {
        return this.farba;
    }
}
