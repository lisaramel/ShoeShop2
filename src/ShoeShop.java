//import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Lisa Ramel
 * Date: 2021-02-22
 * Time: 09:29
 * Project: ShoeShop2
 * Copywrite: MIT
 */
public class ShoeShop {

    private Repository r = new Repository();
    private Scanner sc = new Scanner(System.in);
    private List<Product> products;
    private List<Product> availableProducts;
    private List<Rating> ratings;
    private List<Brand> brands;
    private List<Category> categories;

    private Customer currentCustomer;


    public ShoeShop() throws InterruptedException{
        assembleProducts();
        updateStock();
        getRatingIds();
        //assembleCategoryBelonging();
        /*
        for(Product p : products){
            System.out.println(p);
        }

         */


        String userName;
        String password;


        while(true){
            System.out.println("Användarnamn: ");
            userName = sc.nextLine().trim();
            if (userName.equalsIgnoreCase("q")){
                System.exit(0);
            }
            System.out.println("Lösenord: ");
            password = sc.nextLine().trim();
            if (userName.equalsIgnoreCase("q")){
                System.exit(0);
            }

            if (r.signIn(userName, password) == null){
                System.out.println("Fel inloggningsuppgifter. Var god försök igen.");;
            } else {
                currentCustomer = r.signIn(userName, password);
                inloggedLoop();
                break;
            }
        }
    }

    public void inloggedLoop(){
        assembleOrders();

        System.out.println("\nVälkommen " + currentCustomer.getName() + "!\n");

        System.out.println("1. Visa gamla beställningar \n2. Visa produkter \n3. Visa aktuell beställning \n4. Betygsätt produkter \n5. Visa produkters medelvärde \n6. Logga ut.\n");
        int userChoice = sc.nextInt();

        while (true){
            if(userChoice == 1){
                // visa beställningar
                showOrders();
                System.out.println("1. Visa gamla beställningar \n2. Visa produkter \n3. Visa aktuell beställning \n4. Betygsätt produkter \n5. Visa produkters medelvärde \n6. Logga ut.\n");
                userChoice = sc.nextInt();
            } else if(userChoice == 2){
                shoppingLoop();
                System.out.println("1. Visa gamla beställningar \n2. Visa produkter \n3. Visa aktuell beställning \n4. Betygsätt produkter \n5. Visa produkters medelvärde \n6. Logga ut.\n");
                userChoice = sc.nextInt();
            } else if (userChoice == 3){
                // logga ut
                break;
            } else if(userChoice == 4){
                rateProduct();
                System.out.println("1. Visa gamla beställningar \n2. Visa produkter \n3. Visa aktuell beställning \n4. Betygsätt produkter \n5. Visa produkters medelvärde \n6. Logga ut.\n");
                userChoice = sc.nextInt();
            } else{
                System.out.println("Fattar inte, försök igen\n");
                System.out.println("1. Visa gamla beställningar \n2. Visa produkter \n3. Visa aktuell beställning \n4. Betygsätt produkter \n5. Visa produkters medelvärde \n6. Logga ut.\n");
                userChoice = sc.nextInt();
            }

        }
    }

    public void updateStock(){
        availableProducts = new ArrayList<>();
        for(Product p: products){
            if (p.getAmountInStock() > 0){
                availableProducts.add(p); // borde gå med en lamda?
            }
        }
    }

    public void showOrders(){
        // skriv ut alla orders
        currentCustomer.getOrders().stream().forEach(e -> System.out.println(e.getDate() + " " + e.getProductIds()));

    }

    public void shoppingLoop(){

        while (true){
            System.out.println("Vilken sko vill du lägga i din beställning? ");
            printEnumeratedStockedProductes();
            int userChoice = sc.nextInt();

            if (userChoice == availableProducts.size()+1) {
                break;
            }

            // här göra addToCart-anrop
            // alltså skaffa alla idn
            // kolla dagens datum, finns det ingen beställning med dagens datum skickas ordersId -1 in
            // för det borde ju inte finnas, och då kommer vi till skapa ny order

            Product chosenProduct = availableProducts.get(userChoice-1);
            int ordersId = -1;

            for(Order o : currentCustomer.getOrders()){
                if(o.getDate().equals(LocalDate.parse("2020-12-05"))){ // LocalDate.now()

                    ordersId = o.getId();
                }
            }

            r.newOrder(currentCustomer.getID(), ordersId, chosenProduct.getId());

            System.out.println("Du har lagt till " + chosenProduct.shopperView() + " i din beställning");
            updateStock();

        }
    }

    public void rateProduct(){
        while (true){
            System.out.println("Vilken sko vill du betygsätta? ");
            printEnumeratedStockedProductes();
            int userChoice = sc.nextInt();

            if (userChoice == availableProducts.size()+1) {
                break;
            }

            Product chosenProduct = availableProducts.get(userChoice-1);

            System.out.println("Vad vill du ge den för betyg?");
            System.out.println("1. Missnöjd \n2. Ganska nöjd \n3. Nöjd \n4. Mycket nöjd");
            userChoice = sc.nextInt();

            if (userChoice == 1) {
                System.out.println("Du gav produkten betyget \"Missnöjd\"");
            } else if (userChoice == 2) {
                System.out.println("Du gav produkten betyget \"Ganska nöjd\"");
            } else if (userChoice == 3) {
                System.out.println("Du gav produkten betyget \"Nöjd\"");
            } else if (userChoice == 4) {
                System.out.println("Du gav produkten betyget \"Mycket nöjd\"");
            } else {
                System.out.println("Fel inmatning? Försök igen.");
                System.out.println("1. Visa gamla beställningar \n2. Visa produkter \n3. Visa aktuell beställning \n4. Betygsätt produkter \n5. Visa produkters medelvärde \n6. Logga ut.\n");
                userChoice = sc.nextInt();
            }

            Rating rate = ratings.get(userChoice-1);

            String text = "";

            System.out.println("Vill du lägga till en kommentar?");
            System.out.println("1. Ja \n2. Nej");
            userChoice = sc.nextInt();

            if (userChoice == 1) {
                System.out.println("Kommentar: ");
                text = sc.next();
            } else if (userChoice == 2) {
                text = null;
            } else {
                System.out.println("Fel inmatning? Försök igen.");
                System.out.println("1. Visa gamla beställningar \n2. Visa produkter \n3. Visa aktuell beställning \n4. Betygsätt produkter \n5. Visa produkters medelvärde \n6. Logga ut.\n");
                userChoice = sc.nextInt();
            }

            r.setRating(currentCustomer.getID(), chosenProduct.getId(), rate.getId(), text);

            System.out.println("Du har gett " + chosenProduct.shopperView() + " betyget " + rate.getName() + "\n");


        }
    }

    public void printEnumeratedStockedProductes(){
        int counter = 1;
        for(Product p: availableProducts){
            System.out.println(counter + ". " + p.shopperView());
            counter++;
        }
        System.out.println(counter + ". Jag vill tillbaka");
    }

    public void assembleCategoryBelonging(){
        categories = r.getCategories();
        //List<Pair> categoryBelongings = r.getCategoryBelongings();
/*
        for (Pair p : categoryBelongings){

            System.out.println(p.getKey());
        }

        for(Product p : products){
            System.out.println(p.getColour() + p.getSize());
            for(Category c : categories){
                System.out.println(" p " + p.getId() + "   c " + c.getId());
                if(p.getId() == c.getId()){

                    c.addProducts(p);
                }
            }
        }

        for(Category c: categories){
            System.out.println(c.getName());
            System.out.println(c.getProducts().size());
        }

 */

    }



    public void assembleOrders(){
        currentCustomer.ordersSetup(r.getOrders(currentCustomer.getID()));
        // och carts som hör till detta
        for(Order o : currentCustomer.getOrders()){
            o.setProductIds(r.getCarts(o.getId()));
        }
    }

    //getCurrentCustomerID

    public void getRatingIds(){
        ratings = r.getRatings();
        for(Rating r : ratings){
            r.getId();
        }
    }

    public void assembleProducts(){
        products = r.getProducts();
        brands = r.getBrands();
        for(Product p : products){
            Brand b = brandById(p.getBrandId());
            p.setBrand(b);
        }
    }


    public Brand brandById(int lookupID){
        for (Brand b : brands){
            if (b.getId() == lookupID){
                return b;
            }
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        ShoeShop shop = new ShoeShop();
    }

}
