package monopoly;

public class NespravnyFormatException extends RuntimeException {

    public NespravnyFormatException(int cisloObjectu, int cisloRiadkuVTxt) {
        super("Nastala chyba pri vytvarani postavicky #" + cisloObjectu + "\nV vasom txt file skontrolujte riadok #" + cisloRiadkuVTxt);
    }

}
