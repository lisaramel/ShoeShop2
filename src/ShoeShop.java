import java.util.Scanner;

/**
 * Created by Lisa Ramel
 * Date: 2021-02-22
 * Time: 09:29
 * Project: ShoeShop2
 * Copywrite: MIT
 */
public class ShoeShop {

    public ShoeShop() throws InterruptedException{
        Repository r = new Repository();
        String choose;
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.println("");
            choose = sc.nextLine().trim();
            if (choose.equalsIgnoreCase("q")){
                System.exit(0);
            }
            //System.out.println();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ShoeShop shop = new ShoeShop();
    }

}
