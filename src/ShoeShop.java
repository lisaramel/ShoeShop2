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
    private List<Brand> brands;
    private List<Category> categories;

    private Customer currentCustomer;


    public ShoeShop() throws InterruptedException{
        assembleProducts();
        updateStock();
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

        //1gamla bestlällningar 2aktuell beställning 3ge betyg, kommentera 5 visa produkter medelvärde 6 logga ut
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
            } else{
                System.out.println("fattar inte, försök igen");
                System.out.println(showMeny());
                userChoice = sc.nextInt();
            }

        }
    }

    public String showMeny(){
        return "\n1, visa gamla beställningar, 2, visa produkter, 3, visa aktuell beställning, " +
                "\n4, betygsätt produkter, 5, visa produkters medelvärde, 6, logga ut.";
    }

    public void updateStock(){
        availableProducts = new ArrayList<>();
        for(Product p: products){
            if (p.getAmountInStock() > 0){
                availableProducts.add(p); // borde gå med en lamda?
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
        // skriv ut alla orders
        for (Order o : currentCustomer.getOrders()){
            System.out.println(o.getOrderInfo());
        }
        //currentCustomer.getOrders().stream().forEach(e -> System.out.println(e.getDate() + " " + e.getProductIds()));

    }

    public void shoppingLoop(){

        while (true){
            System.out.println("Vilken sko vill du lägga i din beställning? ");
            printEnumeratedStockedProductes();
            int userChoice = sc.nextInt();

            if (userChoice == availableProducts.size()+1) {
                // tillbaka till huvudmenyn
                break;
            }

            // här göra addToCart-anrop
            // alltså skaffa alla idn
            // kolla dagens datum, finns det ingen beställning med dagens datum skickas ordersId -1 in
            // för det borde ju inte finnas, och då kommer vi till skapa ny order

            Product chosenProduct = availableProducts.get(userChoice-1);
            int ordersId = -1;

            for(Order o : currentCustomer.getOrders()){
                if(o.getDate().equals(LocalDate.now())){ // , LocalDate.parse("2020-12-05")
                    ordersId = o.getId();
                }
            }

            r.newOrder(currentCustomer.getID(), ordersId, chosenProduct.getId());

            System.out.println("du har lagt " + chosenProduct.shopperView() + " i din beställning");
            assembleOrders();
            updateStock();


        }
    }

    public void printEnumeratedStockedProductes(){
        int counter = 1;
        for(Product p: availableProducts){
            System.out.println(counter + " " + p.shopperView());
            counter++;
        }
        System.out.println(counter + " jag vill tillbaka");
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
