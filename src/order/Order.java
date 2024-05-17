package order;

import line_item.LineItem;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String code;
    private String customerName;
    private final String date;
    private final List<LineItem> lineItems = new ArrayList<>();
    private double total;
    private String status = "unpaid";

    public Order() {
        this.code = "ĐH" + (int) (Math.random() * 10000);
        this.date = getCurrentDate();
    }


    public String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.now().format(formatter);
    }

    public String getStatus() {
        if (status.equals("unpaid")) {
            return "Chưa thanh toán";
        }
        if (status.equals("paid")) {
            return "Đã thanh toán";
        }
        return "Đã hủy";
    }

    public void setStatus() {
        this.status = "paid";
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LineItem getOrderItem(String sku) {
        for (LineItem lineItem : lineItems) {
            if (lineItem.getProductSku().equals(sku)) {
                return lineItem;
            }
        }
        return null;
    }

    public void addOrderItem(LineItem lineItem) {

        this.lineItems.add(lineItem);
    }

    public void deleteOrderItem(LineItem lineItem) {
        this.lineItems.remove(lineItem);
    }

    //Tính tổng giá trị đơn hàng
    public String updateTotal() {
        this.total = 0;
        for (LineItem lineItem : lineItems) {
            this.total += lineItem.getTotal();
        }
        return "Tổng: " + this.total;
    }

    public String getCode() {
        return code;
    }

    public String getLineItem() {
        StringBuilder str = new StringBuilder("'");
        for (LineItem item : lineItems) {
            str.append(item.getProductSku()).append(", SP: ").append(item.getProductName()).append(", SL: ").append(item.getQuantity()).append(", Giá: ").append(item.getPrice()).append("//");
        }
        str.append("'");
        return str.toString();
    }



    public String getCustomerName() {
        return customerName;
    }

    @Override
    public String toString() {
        StringBuilder orderItemsStr = new StringBuilder();
        if (lineItems.isEmpty()) {
            orderItemsStr.append("Chưa có sản phẩm nào!\n");
        } else {
            for (LineItem lineItem : lineItems) {
                orderItemsStr.append(lineItem.toString()).append("\n");
            }
        }
        return "\nĐƠN HÀNG " + code + "\n" +
                "=====================================\n" +
                "Khách hàng: " + customerName + "\n" +
                "Ngày tạo: " + date + "\n" +
                "-------------------------------------\n" +
                orderItemsStr +
                "-------------------------------------\n" +
                "Tổng tiền: " + total + "\n" +
                "Trạng thái: " + getStatus() + "\n";


    }

}
