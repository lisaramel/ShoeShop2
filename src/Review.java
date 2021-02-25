import java.util.List;

/**
 * Created by Lisa Ramel
 * Date: 2021-02-22
 * Time: 09:23
 * Project: ShoeShop2
 * Copywrite: MIT
 */
public class Review {

    int ratingId;
    String text;
    int productId;
    int orderId;

    public Review(int ratingId, String text, int productId, int orderId) {
        this.ratingId = ratingId;
        this.text = text;
        this.productId = productId;
        this.orderId = orderId;
    }
}
