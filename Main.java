package monopoly;

import monopoly.hra.Hra;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        System.out.println("Zadajte pocet hracov");
        Scanner scanner = new Scanner(System.in);
        var pocetHracov = scanner.nextInt();


        Hra hra = new Hra(pocetHracov);




    }
}