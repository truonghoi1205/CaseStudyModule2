package order;

import input.Input;
import line_item.LineItem;
import product.Product;
import product.ProductManager;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class OrderManager {
    private final ProductManager productManager;
    public final List<Order> list;
    private static final String PATH_CODE = "src/order.csv";
    File file = new File(PATH_CODE);

    public OrderManager(ProductManager productManager) {
        this.productManager = productManager;
        list = new ArrayList<>();
    }


    public void menuOrder() {
        while (true) {
            int select;
            System.out.println("====== MENU ĐƠN HÀNG ======\n" +
                    "1. Tìm kiếm\n" +
                    "2. Hiển thị tất cả đơn hàng\n" +
                    "3. Tạo đơn\n" +
                    "4. Chỉnh sửa\n" +
                    "5. Quay lại\n" +
                    "Chọn chức năng:");
            select = Input.getInt();
            switch (select) {
                case 1:
                    searchOrder();
                    break;
                case 2:
                    showAll();
                    break;
                case 3:
                    newOrder();
                    break;
                case 4:
                    showOrder();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Không có lựa chọn này");
            }
        }
    }

    public void showAll() {
        readFileInOrder();
    }

    public void searchOrder() {
        while (true) {
            System.out.println("Nhập từ khóa tìm kiếm (số đơn hàng).");
            System.out.println("Nếu kết quả tìm kiếm chỉ có một đơn hàng sẽ tự động mở chỉnh sửa.");
            System.out.println("Tối thiểu 3 ký tự (nhập q để thoát):");
            String select = Input.getString();

            if (select.equalsIgnoreCase("q")) {
                return;
            }
            if (select.length() < 3) {
                System.err.println("Vui lòng nhập đúng mã đơn hàng!!");
                continue;
            }
            searchOrder(select);
        }
    }

    public void searchOrder(String key) {
        List<Order> result = new ArrayList<>();
        for (Order order : list) {
            if (order.getCustomerName().contains(key) || order.getCode().contains(key)) {
                result.add(order);
            }
        }
        if (result.isEmpty()) {
            System.err.println("Không tìm thấy đơn hàng!");
            return;
        }
        if (result.size() > 1) {
            System.out.println("Đã tìm thấy " + result.size() + " đơn hàng!");
            return;
        }
        showOrder(result.get(0));
    }

    public void newOrder() {
        Order order = new Order();
        System.out.println("Nhập tên khách hàng:");
        String customerName = Input.getString();
        order.setCustomerName(customerName);
        list.add(order);
        System.out.println("Tạo đơn hàng thành công!");
        showOrder(order);
        writeFileInOrder();
    }

    public Order findOrderByNumber(String code) {
        for (Order order : list) {
            if (order.getCode().equals(code)) {
                return order;
            }
        }
        return null;
    }

    private void showOrder() {
        while (true) {
            System.out.println("Vui lòng nhập số đơn hàng muốn xem:");
            String number = Input.getString();
            if (number.isEmpty()) {
                break;
            }

            Order order = findOrderByNumber(number);
            if (order == null) {
                System.err.println("Không tìm thấy đơn hàng với số này!");
                continue;
            }
            showOrder(order);
            break;
        }

    }


    private void showOrder(Order order) {
        while (true) {
            System.out.println(order);
            System.out.println("Vui lòng nhập lệnh của bạn (nhập h để xem gợi ý, nhập q để thoát):");
            String select = Input.getString();
            if (select.equalsIgnoreCase("q")) {
                return;
            }
            if (select.equalsIgnoreCase("h")) {
                showHelp();
            }
            if (select.equalsIgnoreCase("pay")) {
                order.setStatus();
            }
            String[] command = select.split(" ");
            if (command.length == 3) {
                int quantity;
                try {
                    quantity = Integer.parseInt(command[2]);
                    if (quantity <= 0) {
                        System.err.println("Số lượng phải lớn hơn 0!");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Số lượng không hợp lệ!");
                    continue;
                }

                Product product = productManager.findProductBySku(command[1]);
                if (product == null) {
                    System.err.println("Không tìm thấy sản phẩm với sku này!");
                    continue;
                }

                switch (command[0]) {
                    case "add":
                        addOrderItem(order, product, quantity);
                        writeFileInOrder();
                        break;
                    case "update":
                        updateOrderItem(order, product, quantity);
                        writeFileInOrder();
                        break;
                }
            }

            if (command.length == 2) {
                if (command[0].equals("delete")) {
                    Product product = productManager.findProductBySku(command[1]);
                    if (product == null) {
                        System.err.println("Không tìm thấy sản phẩm với sku này!");
                        continue;
                    }
                    deleteOrderItem(order, product);
                }
            }

        }
    }


    public void deleteOrderItem(Order order, Product product) {
        LineItem orderItem = order.getOrderItem(product.getSku());
        if (orderItem == null) {
            System.err.println("Đơn hàng không có sản phẩm với sku này!");
            return;
        }
        order.deleteOrderItem(orderItem);
        order.updateTotal();
    }

    public void updateOrderItem(Order order, Product product, int quantity) {
        LineItem orderItem = order.getOrderItem(product.getSku());
        if (orderItem == null) {
            System.err.println("Đơn hàng không có sản phẩm với sku này!");
            return;
        }
        orderItem.setQuantity(quantity);
        orderItem.setTotal(product.getPrice() * orderItem.getQuantity());
        order.updateTotal();
    }

    public void addOrderItem(Order order, Product product, int quantity) {
        double total = product.getPrice() * quantity;
        LineItem orderItem;
        orderItem = order.getOrderItem(product.getSku());
        if (orderItem == null) {
            orderItem = new LineItem(product.getSku(), product.getName(), quantity, product.getPrice(), total);
            order.addOrderItem(orderItem);
        } else {
            orderItem.addQuantity(quantity);
            orderItem.setTotal(product.getPrice() * orderItem.getQuantity());
        }
        order.updateTotal();
    }

    private void showHelp() {
        System.out.println("Các lệnh hỗ trợ:");
        System.out.println("add sku quantity: thêm sản phẩm vào đơn hàng (add SKU01 30)");
        System.out.println("delete sku: xóa sản phẩm khỏi đơn hàng (delete SKU01)");
        System.out.println("update sku quantity: cập nhật số lượng sản phẩm trong đơn hàng (update SKU01 30)");
        System.out.println("pay: thanh toán đơn hàng");
        System.out.println("cancel: hủy đơn hàng");
        System.out.println("h: hiển thị lệnh hỗ trợ");
        System.out.println("q: thoát");
        System.out.println("Enter để tiếp tục...");
        Input.getString();
    }

    public void writeFileInOrder() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Mã đơn hàng,Tên khách hàng,Sản phẩm mua,Ngày đặt,Tổng bill");
            for (Order order : list) {
                String line = order.getCode() + "," + order.getCustomerName() + "," + order.getLineItem() + "," + order.getCurrentDate() + "," + order.updateTotal();
                bufferedWriter.newLine();
                bufferedWriter.write(line);
            }
            bufferedWriter.close();
        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        }
    }

    public void readFileInOrder() {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();// skip fisrt line;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                StringBuilder orderItemsStr = new StringBuilder();

                String s = "\nĐƠN HÀNG " + data[0] + "\n" +
                        "=====================================\n" +
                        "Khách hàng: " + data[1] + "\n" +
                        "Ngày tạo: " + data[data.length - 2] + "\n"
                        + "-------------------------------------";
                System.out.println(s);
                for (int i = 2; i < data.length - 2; i++) {
                    orderItemsStr.append(data[i]);
                }
                System.out.print(orderItemsStr);
                String t = "\n-------------------------------------\n"
                        + "Tổng tiền: " + data[data.length - 1] + "\n";
                System.out.println(t);
            }

        } catch (Exception e) {
            System.err.println("Chưa có đơn hàng nào!!");
        }
    }

}