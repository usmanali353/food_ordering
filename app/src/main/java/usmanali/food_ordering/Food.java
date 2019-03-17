package usmanali.food_ordering;

public class Food {
    String food_name,food_weight,food_image,food_catorgery;
    int food_price,food_quantity,food_id,id;

    public Food(String food_name, int food_price, int id, String food_image, int food_quantity, int food_id) {
        this.food_name = food_name;
        this.food_image = food_image;
        this.food_price = food_price;
        this.food_quantity = food_quantity;
        this.food_id = food_id;
        this.id = id;
    }
}
