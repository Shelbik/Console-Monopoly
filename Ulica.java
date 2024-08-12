package monopoly.ulice;

public class Ulica extends Nehnutelnost {
    private final FarbyUlice farbaUlice;
    private int pocetDomov;
    private int pocetHotelov;

    private final int cenaZaJedenDom;
    private final int cenaZaJedenHotel;
    private final int hodnotaIpoteky;
    private final int cenaSplateniaHypoteky;


    public Ulica(String nazov, FarbyUlice farbaUlice, int najomne, int cena, int cenaZaJedenDom, boolean jeVykupena) {
        super(nazov, cena, najomne, jeVykupena);
        this.farbaUlice = farbaUlice;
        this.pocetDomov = 0;
        this.pocetHotelov = 0;
        this.hodnotaIpoteky = this.getCenaPriNakupe() / 2;
        this.cenaSplateniaHypoteky = (int)(this.hodnotaIpoteky + (this.hodnotaIpoteky * 0.1));
        this.cenaZaJedenDom = cenaZaJedenDom;
        this.cenaZaJedenHotel = this.cenaZaJedenDom * 5;

    }


    public String getFarbaUlice() {
        return this.farbaUlice.getFarba();
    }


    public String getPopis() {
        double cena = 5;
        String temp;
        StringBuilder sb = new StringBuilder();
        sb.append("Kupna zmluva: \n");
        sb.append("Ulica -> ").append(this.getNazov()).append("\nNajomne = ").append(this.getNajomne()).append("$\n").append("Najomne s farebnou skupinou = ").append(this.getNajomne() * 2).append("$\n");
        for (int i = 0; i < 4; i++) {
            temp = ((i + 1) + " domy davaju najomne vo vyske " + this.getNajomne() * 2 * (cena * (i + 1)) + "$\n");

            sb.append(temp);
        }
        sb.append("Cena jedneho domu ").append(this.cenaZaJedenDom).append("$\n");
        sb.append("Cena jedneho hotela ").append(this.cenaZaJedenHotel).append("$\n");

        sb.append("Najomne za jeden hotel = ").append(this.getNajomne() * 2 * (cena * 4) + this.getNajomne() * 2 * cena);
        return sb.toString();
    }


    public int getPocetDomov() {
        return this.pocetDomov;
    }

    public int getPocetHotelov() {
        return this.pocetHotelov;
    }

    public int getCenaZaJedenDom() {
        return this.cenaZaJedenDom;
    }

    public int getCenaZaJedenHotel() {
        return this.cenaZaJedenHotel;
    }

    public int getHodnotaIpoteky() {
        return this.hodnotaIpoteky;
    }

    public int getCenaSplateniaHypoteky() {
        return this.cenaSplateniaHypoteky;
    }


}


