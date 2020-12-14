package User;

import Admin.Product;

public class Purchase {
    private String Product;
    private double Price;
    private String Status;
    Purchase(){};
    Purchase(String Product, double Price, String Status)
    {
        this.Product = Product;
        this.Price = Price;
        this.Status = Status;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
