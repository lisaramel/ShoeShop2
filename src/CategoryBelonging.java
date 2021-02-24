/**
 * Created by Vilma Couturier Kaijser
 * Date: 2021-02-24
 * Project: ShoeShop2
 * Copyright: MIT
 */
public class CategoryBelonging {

    private int id;
    private int productId;
    private int categoryId;


    public CategoryBelonging(int id, int productId, int categoryId) {
        this.id = id;
        this.productId = productId;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
