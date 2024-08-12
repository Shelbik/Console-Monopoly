package monopoly.hrac;

import monopoly.hra.Kocka;
import monopoly.mysticcard.karty.Karta;
import monopoly.mysticcard.karty.KartaPokladna;
import monopoly.mysticcard.karty.KartaSanca;
import monopoly.ulice.FarbyUlice;
import monopoly.ulice.KomunalneSluzby;
import monopoly.ulice.Nehnutelnost;
import monopoly.ulice.Ulica;
import monopoly.ulice.Zssk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

public class Hrac {
    private final String meno;

    private final Penazenka penazenka;
    private List<Nehnutelnost> zoznamNehnutetelnosti;
    private Karta kartaPrePouzivnie;

    private final Postavicki postavicka;

    private boolean jeVoVazeni;

    private int pozicia;

    private int pocetStanic;
    private int pocetKomunalnych;
    private int kolkoKratPadloParneCislo;

    public Hrac(String meno, Postavicki postavicka) {
        this.meno = meno;
        this.penazenka = new Penazenka();
        this.zoznamNehnutetelnosti = new ArrayList<>();
        this.postavicka = postavicka;
        this.kartaPrePouzivnie = null;
        this.pocetStanic = 0;
        this.pocetKomunalnych = 0;
        this.kolkoKratPadloParneCislo = 0;
    }

    /**
     * Metóda slúži na vykonanie platby hráčom.
     * Ak hráč má dostatočný kredit na účte, platba sa vykoná.
     * Ak nie, hráč musí predať nehnuteľnosti na ipotéku alebo vybrať nehnuteľnosti na predaj.
     *
     * @param kolko suma na zaplatenie
     */
    public void zaplat(double kolko) {
        if (kolko <= this.getPenazenka().getKredit()) {
            this.penazenka.takeMoneyAway(kolko);
        } else {
            this.vyberNehnutelnostiNaPredaj();
            if (kolko <= this.getPenazenka().getKredit()) {
                this.zaplat(kolko);
            } else {
                System.out.println("Stale mate nedostatok kreditu na splatenie prenajmu");
                this.vyberNehnutelnostiNaPredaj();
            }
        }
    }
    /**
     * Metóda slúži na výber nehnuteľnosti na predaj.
     * Ak hráč nemá dostatok peňazí, vypýta si od hráča vstup a predá nehnuteľnosti.
     */
    private void vyberNehnutelnostiNaPredaj() {
        System.out.println("\nMate nedostatok peniazi");
        Scanner scanner = new Scanner(System.in);
        this.vypisatNehnutelnost();
        System.out.println("\n Napiste nazov nehnutelnosti ktoru chcete predat na Ipoteku");
        var nazov = scanner.next();
        this.predajNehnutelnostiNaIpoteku(nazov);
    }


    public String getMeno() {
        return this.meno;
    }
    /**
     * Metóda slúži na pohyb hráča na hracej ploche.
     *
     * @param kocka kocka pre vygenerovanie počtu krokov hráča
     */
    public void spravKrok(Kocka kocka) {
        if (kocka != null) {
            this.vypocetKrokov(kocka);
            if (kocka.padloParneCislo()) {
                if (this.kolkoKratPadloParneCislo != 3) {

                    System.out.println("\nPADLO VAM PARNE CISLO, IDETE ESTE RAZ");
                    this.kolkoKratPadloParneCislo++;
                } else {
                    System.out.println("\n3 KRAT VAM PADLO PARNE CISLA NA KOCKACH, IDETE DO VAZENIA");
                    this.setPozicia(10);
                    this.kolkoKratPadloParneCislo = 0;
                }
            } else {
                this.kolkoKratPadloParneCislo = 0;
            }

        }



    }

    private void vypocetKrokov(Kocka kocka) {
        kocka.hodKocku();
        int pocetKrokov = kocka.getSucetHodov();

        System.out.println("\nPri hode dvoma kockami, pocet krokov ktory spravite = " + pocetKrokov + " (" + kocka.getFirstRoll()
            + "," + kocka.getHonotaHoduDruhejKocky() + ")");

        if (this.pozicia + pocetKrokov >= 40) {
            int vysledok = (this.pozicia + pocetKrokov) - 40;
            if (vysledok == 0) {
                this.setPozicia(vysledok);
            } else {
                this.setPozicia(vysledok);
                this.penazenka.addMoney(200);
            }
        } else {
            this.pozicia += pocetKrokov;
        }
    }


    public int getPozicia() {
        return this.pozicia;
    }

    public Penazenka getPenazenka() {
        return this.penazenka;
    }

    /**
     * Nakúpi nehnuteľnosť hráča, ak je dostatočný kredit v peňaženke.
     * Ak hráč už nehnuteľnosť vlastní, alebo ak je nehnuteľnosť vlastnená iným hráčom
     * Ak hráč nemá dostatok kreditu na nákup, vypíše sa príslušná správa.
     * @param nehnutelnost
     */
    public void kupNehnutetlnost(Nehnutelnost nehnutelnost) {
        if (Optional.ofNullable(nehnutelnost).isPresent()) {
            var cena = nehnutelnost.getCenaPriNakupe();

            if (!this.zoznamNehnutetelnosti.contains(nehnutelnost) && nehnutelnost.getVlastnik() == null) {
                if (this.penazenka.getKredit() >= nehnutelnost.getCenaPriNakupe()) {
                    this.zaplat(cena);
                    this.zoznamNehnutetelnosti.add(nehnutelnost);
                    nehnutelnost.setJeVykupena(true);
                    nehnutelnost.setVlastnik(this);
                    this.nastavenieAkutalnejCenyPreZsskASluzby(nehnutelnost);
                } else {
                    System.out.println("\nMate nedostatok kreditu pre tuto operaciu");
                }

            }
        }
    }

    /**
     * Hráč získa kartu a vykoná príslušnú akciu na základe typu karty.
     * @param karta
     */
    public void ziskajKartu(Karta karta) {
        if (Optional.ofNullable(karta).isPresent()) {

            if (karta instanceof KartaSanca) {
                if (!this.maKartuNaVypustenieZVazenia() && karta.getPopis().equals("Priepustka z vazenia")) {
                    this.kartaPrePouzivnie = karta;
                } else {
                    karta.vykonaj(this);
                }

            } else {
                if (karta instanceof KartaPokladna) {
                    karta.vykonaj(this);
                }
            }

        }
    }

    /**
     * Kontroluje, či hráč má kartu na vypustenie z väzenia.
     * @return
     */

    public boolean maKartuNaVypustenieZVazenia() {
        return this.kartaPrePouzivnie != null && this.kartaPrePouzivnie.getPopis().equals("priepustka z vazenia");
    }

    /**
     * Táto metóda slúži na použitie karty na vypustenie z väzenia hráčom.
     * Ak hráč má kartu na vypustenie, nastaví premennú kartaPrePouzivnie na null, čím označí, že kartu použil.
     */
    public void pouzilKartuNaVypustenieZVazenia() {
        if (this.kartaPrePouzivnie != null) {
            if (this.maKartuNaVypustenieZVazenia()) {
                this.kartaPrePouzivnie = null;
            }
        }
    }

    public void setPozicia(int pozicia) {
        if (pozicia > 39) {
            this.pozicia = pozicia - 39;
        } else {
            this.pozicia = pozicia;
        }

    }

    public int getPocetStanic() {
        return this.pocetStanic;
    }

    public int getPocetKomunalnychSluzbi() {
        return this.pocetKomunalnych;
    }

    /**
     * Metóda slúži na predaj hráčovej nehnuteľnosti inému hráčovi.
     * Metóda volá pomocný algoritmus algNaPredajNehnutetelnosti(), ktorý vykoná samotný predaj.
     * @param hrac
     * @param nazovNehnutelnosti
     */
    public void nakupInymHracomMojejNehnutelnosti(Hrac hrac, String nazovNehnutelnosti) {
        this.algNaPredajNehnutetelnosti(nazovNehnutelnosti, hrac);

    }

    /**
     * Metóda slúži na zistenie ceny určitej ulice od konkrétneho hráča.
     * Ak je cena nehnuteľnosti väčšia ako 0, vypíše sa informácia o cene, inak sa vypíše hlásenie, že hráč ešte nechcel nehnuteľnosť predať.
     * @param hrac
     * @param nazovNehnutelnosti
     */
    public void zistenieCenyKonretnejUliceKonretnehoHraca(Hrac hrac, String nazovNehnutelnosti) {
        var nehnutelnost = this.algNaHladanieNehnutelnostiPodlaNazvu(nazovNehnutelnosti);
        if (nehnutelnost != null && nehnutelnost.getCenaNaPredaj() > 0) {
            System.out.println("Hrac " + hrac.getMeno() + " je pripraveny predat " + nehnutelnost.getClass().getSimpleName() + " "
                    + nehnutelnost.getNazov() + "za " + nehnutelnost.getCenaNaPredaj() + "$");
        } else {
            System.out.println("Hrac " + hrac.getMeno() + "este neni pripraveny predat danu ulicu");
        }

    }

    /**
     * Metóda slúži na nastavenie ceny pre konkrétnu nehnuteľnosť na základe jej názvu a ceny.
     * @param nazov
     * @param cena
     */

    public void nastavenieCenyPreKonretnuNehnutelnost(String nazov, int cena) {
        Objects.requireNonNull(this.algNaHladanieNehnutelnostiPodlaNazvu(nazov)).setCenaNaPredaj(cena);
    }

    /**
     * Metóda slúži na predaj nehnuteľnosti v dražbe.
     * Vypočíta sa suma peňazí, ktorú hráč získa za predaj, a nehnuteľnosť sa odstráni zo zoznamu nehnuteľností hráča.
     * @param nazovNehnutetelnosti
     */
    public void predajNehnutelnostiNaIpoteku(String nazovNehnutetelnosti) {
        int peniaze = Objects.requireNonNull(this.algNaHladanieNehnutelnostiPodlaNazvu(nazovNehnutetelnosti)).getCenaPriNakupe() / 2;
        var nehnutelnost = this.algNaHladanieNehnutelnostiPodlaNazvu(nazovNehnutetelnosti);
        assert nehnutelnost != null;
        nehnutelnost.setVlastnik(null);
        nehnutelnost.setNajomne(0);
        nehnutelnost.setJeVykupena(false);
        this.zoznamNehnutetelnosti.remove(this.algNaHladanieNehnutelnostiPodlaNazvu(nazovNehnutetelnosti));
        this.penazenka.addMoney(peniaze);
    }

    public boolean jeVoVazeni() {
        return this.jeVoVazeni;
    }

    public void setJeVoVazeni(boolean jeVoVazeni) {
        this.jeVoVazeni = jeVoVazeni;
    }

    /**
     * Metóda slúži na vzdanie sa hráča.
     * Všetky jeho nehnuteľnosti sa vrátia do voľného stavu.
     */
    public void vzdatSa() {
        for (Nehnutelnost nehnutelnost : this.zoznamNehnutetelnosti) {
            nehnutelnost.setJeVykupena(false);
            nehnutelnost.setVlastnik(null);
            nehnutelnost.setNajomne(0);
            nehnutelnost.setCenaNaPredaj(0);
            this.zoznamNehnutetelnosti.remove(nehnutelnost);
        }
    }

    /**
     * Metóda vypíše zoznam nehnuteľností, ktoré hráč vlastní.
     */
    public void vypisatNehnutelnost() {
        StringBuilder sb = new StringBuilder();
        if (this.zoznamNehnutetelnosti.isEmpty()) {
            sb.append("Zatial nemate ziadnu nehnutelnost");
        } else {
            sb.append("Zoznam vasej nehnutelnosti: ");
            for (Nehnutelnost nehnutelnost : this.zoznamNehnutetelnosti) {
                if (this.zoznamNehnutetelnosti.indexOf(nehnutelnost) != this.zoznamNehnutetelnosti.size() - 1) {
                    sb.append(nehnutelnost.getClass().getSimpleName()).append(" ").append(nehnutelnost.getNazov()).append(", ");
                } else {
                    sb.append(nehnutelnost.getClass().getSimpleName()).append(" ").append(nehnutelnost.getNazov()).append(".");
                }
            }
        }
        System.out.println(sb);
    }


    private void algNaPredajNehnutetelnosti(String nazovHladanejNehnutelnosti, Hrac hrac) {
        if (Optional.ofNullable(hrac).isPresent()) {
            var nehnutelnost = this.algNaHladanieNehnutelnostiPodlaNazvu(nazovHladanejNehnutelnosti);
            var cenaNehnutelnosti = Objects.requireNonNull(nehnutelnost).getCenaNaPredaj();
            if (cenaNehnutelnosti > 0 && hrac.getPenazenka().getKredit() >= cenaNehnutelnosti) {
                nehnutelnost.setVlastnik(null);
                nehnutelnost.setJeVykupena(false);
                this.zoznamNehnutetelnosti.remove(nehnutelnost);
                this.penazenka.addMoney(cenaNehnutelnosti);
                hrac.penazenka.takeMoneyAway(cenaNehnutelnosti);
                hrac.penazenka.addMoney(nehnutelnost.getCenaPriNakupe());
                hrac.kupNehnutetlnost(nehnutelnost);
                System.out.println("\n GRATULUJEM, STE USPENE NAKUPILI " + nehnutelnost.getClass().getSimpleName() + " " + nehnutelnost.getNazov()
                        + " od hraca " + this.getMeno() + " za " + nehnutelnost.getCenaNaPredaj() + "$");
            } else {
                System.out.println("\nDany hrac este nenastavil nejaku cenu na predaj tejto ulice, skuste neskor");
            }

        }
    }


    private void nastavenieAkutalnejCenyPreZsskASluzby(Nehnutelnost nehnutelnost) {
        if (nehnutelnost instanceof Zssk) {
            this.pocetStanic++;
            for (Nehnutelnost stanice : this.zoznamNehnutetelnosti) {
                if (stanice instanceof Zssk) {
                    var zssk = (Zssk)this.zoznamNehnutetelnosti.get(this.zoznamNehnutetelnosti.indexOf(stanice));
                    zssk.nastavenieNajomnychPodlaPoctuStanic();
                }
            }

        } else if (nehnutelnost instanceof KomunalneSluzby) {
            this.pocetKomunalnych++;
            for (Nehnutelnost sluzby : this.zoznamNehnutetelnosti) {
                if (sluzby instanceof KomunalneSluzby) {
                    var komunalneSluzby = (KomunalneSluzby)this.zoznamNehnutetelnosti.get(this.zoznamNehnutetelnosti.indexOf(sluzby));
                    komunalneSluzby.nastavenieCenyPodlaPoctuSluzieb();
                }
            }

        } else if (nehnutelnost instanceof Ulica) {

            this.zvysenieNajomnychPreUliceAkHracVlastniVsetkyRovnakejFarby();
        }
    }

    private void zvysenieNajomnychPreUliceAkHracVlastniVsetkyRovnakejFarby() {


        Map<FarbyUlice, Integer> pocetUlicPodlaFarieb = new HashMap<>();

        // Подсчитываем количество улиц каждого цвета, которые игрок владеет
        for (Nehnutelnost property : this.zoznamNehnutetelnosti) {
            if (property instanceof Ulica ulica) {
                FarbyUlice farbaUlice = FarbyUlice.valueOf(ulica.getFarbaUlice().toUpperCase());
                pocetUlicPodlaFarieb.put(farbaUlice, pocetUlicPodlaFarieb.getOrDefault(farbaUlice, 0) + 1);
            }
        }

        // Перебираем все цвета улиц и проверяем, если игрок владеет всеми улицами одного цвета,
        // то увеличиваем арендную плату на удвоенное значение
        for (FarbyUlice farba : FarbyUlice.values()) {
            int pocetUlic = pocetUlicPodlaFarieb.getOrDefault(farba, 0);
            if (pocetUlic == 3) {
                for (Nehnutelnost property : this.zoznamNehnutetelnosti) {
                    if (property instanceof Ulica ulica) {
                        if (ulica.getFarbaUlice().equals(farba.getFarba())) {
                            ulica.setNajomne(ulica.getNajomne() * 2);
                        }
                    }
                }
            }
        }
    }



    private Nehnutelnost algNaHladanieNehnutelnostiPodlaNazvu(String nazovNehnutetelnosti) {
        for (Nehnutelnost nehnutelnost : this.zoznamNehnutetelnosti) {
            if (nehnutelnost.getNazov().equals(nazovNehnutetelnosti)) {
                return nehnutelnost;
            }
        }
        return null;
    }

    public int getKolkoKratPadloParneCislo() {
        return this.kolkoKratPadloParneCislo;
    }
}
