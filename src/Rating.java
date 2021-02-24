import java.time.LocalDate;
import java.util.List;

/**
 * Created by Lisa Ramel
 * Date: 2021-02-23
 * Time: 15:15
 * Project: ShoeShop2
 * Copywrite: MIT
 */
public class Rating {

    private int id;
    private String name;
    private int number;
    private LocalDate date;

    public Rating(int id, String name, int number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }
}
