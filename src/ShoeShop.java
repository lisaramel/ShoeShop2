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
    private List<Product> availableProducts = new ArrayList<>();
    private List<Rating> ratings;
    private List<Brand> brands;
    private List<Category> categories;

    private Customer currentCustomer;


    public ShoeShop() throws InterruptedException{
        assembleProducts();
        updateStock();
        getRatingIds();
        assembleCategoryBelonging();

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
                System.out.println("\nVälkommen " + currentCustomer.getName());
                Thread.sleep(1000);
                inloggedLoop();
                break;
            }
        }
    }

    public void inloggedLoop() throws InterruptedException {
        assembleOrders();

        System.out.println(showMeny());
        int userChoice = sc.nextInt();

        while (true){
            if(userChoice == 1){
                showOrders();
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            } else if(userChoice == 2){
                shoppingLoop(true, 0);
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            }else if(userChoice == 3){
                showCategories();
                int categoryNum = sc.nextInt();
                shoppingLoop(false, categoryNum-1);
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            }

            else if(userChoice == 4){
                System.out.println(showTodaysOrder());
                Thread.sleep(1000);
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            } else if(userChoice == 5){
                rateProduct();
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            }
            else if(userChoice == 6){
                showProductRatings();
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            }
            else if (userChoice == 7){
                // logga ut
                break;
            }
            else{
                System.out.println("Fattar inte, försök igen\n");
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            }

        }
    }

    public String showMeny(){
        return "1. Visa gamla beställningar \n2. Visa alla produkter i lager  \n3. Visa produkter enligt en kategori" +
                "\n4. Visa aktuell beställning \n5. Betygsätt produkter " +
                "\n6. Visa produkters medelvärde \n7. Logga ut.\n";

    }

    public void updateStock(){
        availableProducts.clear();
        for(Product p: products){
            if (p.getAmountInStock() > 0){
                availableProducts.add(p);
            }
        }
    }

    public String showTodaysOrder() throws InterruptedException {
        for(Order o : currentCustomer.getOrders()){
            if(o.getDate().equals(LocalDate.now())){
               return o.getOrderInfo();
            }
        } return "Du har ingen pågående beställning.";
    }


    public void showOrders() throws InterruptedException {
        currentCustomer.getOrders().stream().forEach(o -> System.out.println(o.getOrderInfo()));
        Thread.sleep(1000);
    }

    public void showCategories(){
        int counter = 1;
        for (Category c : categories){
            System.out.println(counter + ". " + c.getName());
            counter++;
        }
        System.out.println(counter + ". " + "Jag vill tillbaka.");
    }

    public void shoppingLoop(boolean allProducts, int categoryNumber) throws InterruptedException {

        while (true){
            System.out.println("Vilken sko vill du lägga i din beställning? ");
            Thread.sleep(1000);
            Product chosenProduct;
            if(allProducts){
                printEnumeratedProducts(availableProducts);
                int userChoice = sc.nextInt();

                if (userChoice == availableProducts.size()+1) {
                    // tillbaka till huvudmenyn
                    break;
                }
                chosenProduct = availableProducts.get(userChoice-1);
            } else {
                Category c = categories.get(categoryNumber);
                printEnumeratedProducts(c.getProducts());
                int userChoice = sc.nextInt();
                if (userChoice == c.getProducts().size()+1) {
                    // tillbaka till huvudmenyn
                    break;
                }
                chosenProduct = c.getProducts().get(userChoice-1);

            }

            int ordersId = -1;

            for(Order o : currentCustomer.getOrders()){
                if(o.getDate().equals(LocalDate.now())){
                    ordersId = o.getId();
                }
            }

            r.newOrder(currentCustomer.getID(), ordersId, chosenProduct.getId());

            System.out.println("Du har lagt till " + chosenProduct.shopperView() + " i din beställning\n");
            Thread.sleep(700);
            chosenProduct.lowerStock();
            assembleOrders();
            updateStock();
        }
    }

    public void showProductRatings() throws InterruptedException {
        while(true){
            System.out.println("Vilken sko vill du se betyg och kommentarer för: \n");
            Thread.sleep(1000);
            printEnumeratedProducts(products);
            int userChoice = sc.nextInt();
            if (userChoice == products.size()+1) {
                // tillbaka till huvudmenyn
                break;
            }
            Product chosenProduct = products.get(userChoice-1);
            getProductRatings(chosenProduct);
            Thread.sleep(2000);
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

    public void rateProduct() throws InterruptedException {
        while (true){
            System.out.println("Vilken sko vill du betygsätta?\n");
            Thread.sleep(700);
            printEnumeratedProducts(products);
            System.out.println("Vilken sko vill du betygsätta? ");
            printEnumeratedProducts(availableProducts);
            int userChoice = sc.nextInt();

            if (userChoice == availableProducts.size()+1) {
                // tillbaka till huvudmenyn
                break;
            }

            Product chosenProduct = availableProducts.get(userChoice-1);

            if (getCustomersOrderedProducts().contains(chosenProduct)) {
                System.out.println("Vad vill du ge den för betyg?");
                System.out.println("1. Missnöjd \n2. Ganska nöjd \n3. Nöjd \n4. Mycket nöjd");
                userChoice = sc.nextInt();

                if (userChoice > 4 || userChoice < 0) {
                    System.out.println("Fattar inte, försök igen\n");
                    System.out.println("Vad vill du ge den för betyg?");
                    System.out.println("1. Missnöjd \n2. Ganska nöjd \n3. Nöjd \n4. Mycket nöjd");
                    userChoice = sc.nextInt();
                }

                Rating rate = ratings.get(userChoice - 1);     System.out.println("Du har lagt till " + chosenProduct.shopperView()      System.out.println("Du har lagt till " + chosenProduct.shopperView() + " i din beställning");+ " i din beställning");

                String text = "";

                System.out.println("Vill du lägga till en kommentar?");
                System.out.println("1. Ja \n2. Nej");
                userChoice = sc.nextInt();

                if (userChoice == 1) {
                    System.out.println("Kommentar: ");
                    sc.nextLine();
                    text = sc.nextLine();
                } else if (userChoice == 2) {
                    text = "";
                } else {
                    System.out.println("Fel inmatning? Försök igen.");
                    System.out.println(showMeny());
                    userChoice = sc.nextInt();
                }     System.out.println("Du har lagt till " + chosenProduct.shopperView() + " i din beställning");

                r.setRating(currentCustomer.getID(), chosenProduct.getId(), rate.getId(), text);

                System.out.println("Du har gett " + chosenProduct.shopperView() + " betyget: " + "\"" + rate.getName() + "\"" + "\n");
                Thread.sleep(1000);
                break;
            } else
                System.out.println("Du har inte köpt den här varan tidigare och kan därför inte betygsätta den.\n");
                Thread.sleep(700);
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

    public void assembleCategoryBelonging() {
        categories = r.getCategories();
        List<CategoryBelonging> categoryBelongings = r.getCategoryBelongings();

        for (CategoryBelonging cb : categoryBelongings) {
            int cId = cb.getCategoryId();
            int pId = cb.getProductId();
            for (Category c : categories) {
                if (c.getId() == cId) {
                    for (Product p : products) {
                        if (p.getId() == pId) {
                            c.addProducts(p);
                        }
                    }
                }
            }
        } //categories.stream().forEach(e -> System.out.println(e.getName() + ": " + e.getProducts().size()));
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

    public List<Product> getCustomersOrderedProducts(){
        List<Product> customersProducts = new ArrayList<>();
        currentCustomer.ordersSetup(r.getOrders(currentCustomer.getID()));
        // och carts som hör till detta
        for(Order o : currentCustomer.getOrders()){
            o.setProductIds(r.getCarts(o.getId()));
        }

        for (Order o : currentCustomer.getOrders()){
            for(Integer id : o.getProductIds()){
                for(Product p : products){
                    if(id == p.getId()){
                        customersProducts.add(p);
                    }
                }
            }
        }
        return customersProducts;

    }

    //getCurrentCustomerID

    public void getRatingIds(){
        ratings = r.getRatings();
        for(Rating rate : ratings){
            rate.getId();
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
