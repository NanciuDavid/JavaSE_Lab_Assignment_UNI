package seminar3;

public class Vehicles {
    private String producer;
    private float price;

    public Vehicles() {
        this.producer="";
        this.price = 0 ;
    }

    public Vehicles(String producer, float price) {
        this.producer = producer;
        this.price = price;
    }

    public String getProducer() {
        return producer;
    }

    public float getPrice() {
        return price;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
