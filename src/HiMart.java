import input.Input;
import order.OrderManager;
import product.ProductManager;

public class HiMart {

    private final ProductManager productManager = new ProductManager();
    private final OrderManager orderManager = new OrderManager(productManager);

    public void run() {
        while (true) {
            int choice;
            System.out.println("====== MENU CỬA HÀNG ======\n" +
                    "1. Quản lý sản phẩm\n" +
                    "2. Quản lý đơn hàng\n" +
                    "3. Thoát\n" +
                    "Chọn chức năng");
            choice = Input.getInt();
            switch (choice) {
                case 1:
                    productManager.menuProduct();
                    break;
                case 2:
                    orderManager.menuOrder();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Không có lựa chọn này");
                    break;
            }
        }
    }
}
