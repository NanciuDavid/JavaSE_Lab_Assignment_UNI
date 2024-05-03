package seminar3;

public class Car extends Vehicles {
    private String color;

    public Car() {
        this.color = "Black";
    }

    public Car(String color, String producer, float price) {
        super(producer, price);
        this.color = color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor(){
        return this.color;
    }


}
