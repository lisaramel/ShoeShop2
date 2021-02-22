import javafx.util.Pair;

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
    private List<Product> products;
    private List<Brand> brands;
    private List<Category> categories;


    public ShoeShop() throws InterruptedException{
        assembleProducts();
        assembleCategoryBelonging();
        /*
        for(Product p : products){
            System.out.println(p);
        }

         */

        Repository r = new Repository();
        String userName;
        String password;
        Scanner sc = new Scanner(System.in);

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
                System.out.println(r.signIn(userName, password));
                break;
            }
        }
    }

    public void assembleCategoryBelonging(){
        categories = r.getCategories();
        List<Pair> categoryBelongings = r.getCategoryBelongings();

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

    }


    public void assembleProducts(){
        products = r.getProducts();
        brands = r.getBrands();

        System.out.println(products.size());

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
