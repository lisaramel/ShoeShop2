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

        System.out.println(showMeny());
        int userChoice = sc.nextInt();

        while (true){
            if(userChoice == 1){
                // visa beställningar
                showOrders();
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            } else if(userChoice == 2){
                shoppingLoop();
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            } else if(userChoice == 3){
                System.out.println(showTodaysOrder());
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            }
            else if (userChoice == 6){
                // logga ut
                break;
            } else if(userChoice == 4){
                rateProduct();
                showMeny();
                userChoice = sc.nextInt();
            } else if(userChoice == 5){
                showProductRatings();
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            }
            else{
                System.out.println("Fattar inte, försök igen\n");
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            }

        }
    }

    public String showMeny(){
        return "1. Visa gamla beställningar \n2. Visa produkter \n3. Visa aktuell beställning \n4. Betygsätt produkter \n5. Visa produkters medelvärde \n6. Logga ut.\n";

    }

    public void updateStock(){
        availableProducts = new ArrayList<>();
        for(Product p: products){
            if (p.getAmountInStock() > 0){
                availableProducts.add(p);
            }
        }
    }

    public String showTodaysOrder(){
        for(Order o : currentCustomer.getOrders()){
            if(o.getDate().equals(LocalDate.now())){
               return o.getOrderInfo();
            }
        } return "Du har ingen pågående beställning.";
    }


    public void showOrders(){
        currentCustomer.getOrders().stream().forEach(o -> System.out.println(o.getOrderInfo()));
    }

    public void shoppingLoop(){

        while (true){
            System.out.println("Vilken sko vill du lägga i din beställning? ");
            printEnumeratedProducts(availableProducts);
            int userChoice = sc.nextInt();

            if (userChoice == availableProducts.size()+1) {
                // tillbaka till huvudmenyn
                break;
            }
            Product chosenProduct = availableProducts.get(userChoice-1);
            int ordersId = -1;

            for(Order o : currentCustomer.getOrders()){
                if(o.getDate().equals(LocalDate.now())){
                    ordersId = o.getId();
                }
            }

            r.newOrder(currentCustomer.getID(), ordersId, chosenProduct.getId());

            System.out.println("Du har lagt till " + chosenProduct.shopperView() + " i din beställning");
            assembleOrders();
            updateStock();
        }
    }

    public void showProductRatings(){
        while(true){
            System.out.println("Vilken sko vill du se betyg och kommentarer för: ");
            printEnumeratedProducts(products);
            int userChoice = sc.nextInt();
            if (userChoice == products.size()+1) {
                // tillbaka till huvudmenyn
                break;
            }
            Product chosenProduct = products.get(userChoice-1);
            getProductRatings(chosenProduct);
        }

    }

    public void getProductRatings(Product product){
        System.out.println(product.shopperView());

        double rate = r.getAvgRating(product.getId());
        System.out.println("Medelbetyg: " + rate);

        List<String> reviews = r.getReviews(product.getId());
        System.out.println("Alla kommentarer: ");
        reviews.stream().forEach(e -> System.out.println(e));
    }

    public void rateProduct(){
        while (true){
            System.out.println("Vilken sko vill du betygsätta? ");
            printEnumeratedProducts(availableProducts);
            int userChoice = sc.nextInt();

            if (userChoice == availableProducts.size()+1) {
                // tillbaka till huvudmenyn
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
                text = "";
            } else {
                System.out.println("Fel inmatning? Försök igen.");
                System.out.println("1. Visa gamla beställningar \n2. Visa produkter \n3. Visa aktuell beställning \n4. Betygsätt produkter \n5. Visa produkters medelvärde \n6. Logga ut.\n");
                userChoice = sc.nextInt();
            }

            r.setRating(currentCustomer.getID(), chosenProduct.getId(), rate.getId(), text);

            System.out.println("Du har gett " + chosenProduct.shopperView() + " betyget " + rate.getName() + "\n");

        }
    }

    public void printEnumeratedProducts(List<Product> productList){
        int counter = 1;
        for(Product p: productList){
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

        for (Order o : currentCustomer.getOrders()){
            for(Integer id : o.getProductIds()){
                for(Product p : products){
                    if(id == p.getId()){
                        o.addProduct(p);
                    }
                }
            }
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
