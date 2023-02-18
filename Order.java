import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Order extends Thread {
    public static Scanner scanner;
    private String orderID;
    private int productNo;
    private AtomicInteger counter;
    private AtomicInteger inQueueP;
    private Boolean existLastProduct;


    public Order(Scanner scanner) {
        this.scanner = scanner;
        this.counter =  new AtomicInteger(0);
        this.inQueueP = new AtomicInteger(0);
        this.existLastProduct = true;
    }

    @Override
    public void run() {

        String orderLine = null;
        boolean newLine = true;

        /* fiecare thread citeste o comanda */
        while(newLine) {

            synchronized (scanner) {
                newLine = scanner.hasNextLine();
                if(!newLine)
                {
                    break;
                }
                orderLine = scanner.nextLine();
            }

            String[] split = null;
            split = orderLine.split(",");

            /* numarul de comanda */
            orderID = split[0];

            /* numarul de produse */
            productNo = Integer.parseInt(split[1]);

            /* numarul de produse gasite o comanda */
            counter = new AtomicInteger(0);

            for(int i = 0; i < productNo; i++) {
                inQueueP.incrementAndGet();
                Tema2.productsExecutor.submit(new Product(orderID, i, counter, inQueueP,
                                                          existLastProduct, productNo));
            }

            /* cat timp exista sansa ca ultimul produs sa existe */
            while(existLastProduct && productNo > 0) {

                /* daca s-a gasit numarul de produse */
                if(counter.get() == productNo && productNo > 0) {
                    synchronized (scanner) {
                        try {
                            Tema2.ordersWriter.write(orderLine + ",shipped\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
            synchronized (scanner)
            {
                newLine = scanner.hasNextLine();
            }
        }

    }
}
