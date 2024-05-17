package line_item;

public class LineItem {
    private final String productSku;
    private final String productName;
    private int quantity;
    private final double price;
    private double total;

    public LineItem(String productSku, String productName, int quantity, double price, double total) {
        this.productSku = productSku;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public String getProductSku() {
        return productSku;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("Sku = %s, Tên sản phẩm = %s , Số lượng = %d, Giá = %s", productSku, productName, quantity, price + "\n");
    }
}
