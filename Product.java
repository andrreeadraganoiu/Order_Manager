import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Product implements Runnable {
    private String orderID;
    private int rank;
    private AtomicInteger counter;
    private AtomicInteger inQueueP;
    private Boolean existLastProduct;
    private int productsNo;

    public Product(String orderID, int rank, AtomicInteger counter, AtomicInteger inQueueP,
                   Boolean existLastProduct, int productsNo) {
        this.orderID = orderID;
        this.rank = rank;
        this.counter = counter;
        this.inQueueP = inQueueP;
        this.existLastProduct = existLastProduct;
        this.productsNo = productsNo;
    }

    @Override
    public void run() {

        File productsFile = new File(Tema2.folder + "/order_products.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(productsFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int rank = 0;
        /* la inceput presupunem ca nu avem ultimul produs */
        boolean lastProduct = false;

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(",");
            String orderID = split[0];

            if (this.orderID.equals(orderID)) {

                /* s-a gasit al n - lea produs */
                if (this.rank == rank) {

                    if(this.rank == productsNo - 1)
                    {
                        lastProduct = true;
                    }
                    counter.incrementAndGet();

                    synchronized (scanner) {
                        try {
                            Tema2.productsWriter.write(line + ",shipped\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    int left = inQueueP.decrementAndGet();

                    synchronized (Order.scanner) {
                        if (left == 0 && !Order.scanner.hasNextLine()) {
                            Tema2.productsExecutor.shutdown();
                        }
                    }
                    break;
                }
                rank++;
            }
        }
        /* nu s-a gasit ultimul produs */
        if(!lastProduct && this.rank == productsNo - 1)
        {
            existLastProduct = false;
        }
    }
}
