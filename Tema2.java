import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tema2 {

    public static ExecutorService productsExecutor;
    public static FileWriter ordersWriter;
    public static FileWriter productsWriter;
    public static String folder;

    public static void main(String[] args) {

        folder = args[0];
        File inputFile = new File(folder + "/orders.txt");

        Scanner scanner = null;
        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            ordersWriter = new FileWriter("orders_out.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            productsWriter = new FileWriter("order_products_out.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int P = Integer.parseInt(args[1]);
        productsExecutor = Executors.newFixedThreadPool(P);

        Thread[] orders = new Order[P];
        for(int i = 0 ; i < P; i++)
        {
            orders[i] = new Order(scanner);
            orders[i].start();
        }

        for (Thread order : orders) {
            try {
                order.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        scanner.close();
        try {
            ordersWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            productsWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
