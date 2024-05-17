package product;

import input.Input;
import validate.Validate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductManager {

    public List<Product> products;
    Validate validate = new Validate();
    private static final String PATH_PRODUCT = "src/product.csv";
    File file = new File(PATH_PRODUCT);

    public ProductManager() {
        products = new ArrayList<>();
        readFileInProduct();
    }

    public void menuProduct() {
        int choice;
        while (true) {
            System.out.println("======== MENU SẢN PHẨM ========\n" +
                    "1. Hiển thị sản phẩm\n" +
                    "2. Thêm mới\n" +
                    "3. Cập nhật\n" +
                    "4. Xóa\n" +
                    "5. Sắp xếp\n" +
                    "6. Quay lại\n" +
                    "Chọn chức năng: ");
            choice = Input.getInt();
            switch (choice) {
                case 1:
                    showProduct();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    editProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    sortProductByPrice();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Không có lựa chọn này");
                    break;
            }
        }
    }

    public void showProduct() {
        if (products.isEmpty()) {
            System.out.println("Không có sản phẩm nào");
            return;
        }
        for (Product product : products) {
            System.out.println(product);
        }
    }

    public void addProduct() {
        String sku = "MSP-" + (int) (Math.random() * 10000);
        String name = validate.inputValidName();
        double price = validate.inputValidPrice();
        Product product = new Product(sku, name, price);
        products.add(product);
        System.out.println("===> Thêm thành công");
        writeFileInProduct();
    }


    public Product findProductBySku(String sku) {
        for (Product product : products) {
            if (Objects.equals(product.getSku(), sku)) {
                return product;
            }
        }
        return null;
    }

    public void deleteProduct() {
        if (products.isEmpty()) {
            System.out.println("Không có sản phẩm nào để xóa, vui lòng thêm sản phẩm");
            return;
        }
        Product product = null;
        String sku;
        while (product == null) {
            System.out.println("Nhập mã sản phẩm bạn muốn xóa");
            sku = Input.getString();
            if (sku.isEmpty()) {
                return;
            }
            product = findProductBySku(sku);
        }
        System.out.println("Nhập 'Y' để xóa");
        String enterY = Input.getString();
        if (enterY.equalsIgnoreCase("Y")) {
            products.remove(product);
            System.out.println("=======> Xóa thành công");
            writeFileInProduct();
        } else {
            menuProduct();
        }
    }


    public void editName(Product product) {
        String newName = Input.getString();
        product.setName(newName);
        System.out.println("===> Sửa thành công");
    }

    public void editPrice(Product product) {
        double newPrice = Input.getDou();
        product.setPrice(newPrice);
        System.out.println("===> Sửa thành công");
    }


    public void showEditMenu() {
        System.out.println("1. Sửa tên\n" +
                "2. Sửa giá\n" +
                "3. Quay lại\n" +
                "Chọn chức năng");
    }

    public void editProduct() {
        if (products.isEmpty()) {
            System.out.println("Không có sản phẩm nào để sửa, vui lòng thêm sản phẩm");
            return;
        }
        Product product = null;
        System.out.println("Nhập mã sản phẩm bạn muốn sửa: ");
        while (product == null) {
            String sku = Input.getString();

            if (sku.isEmpty()) {
                return;
            }

            product = findProductBySku(sku);
        }
        showEditMenu();
        System.out.println(product);
        int choice = Input.getInt();
        switch (choice) {
            case 1:
                editName(product);
                break;
            case 2:
                editPrice(product);
                break;
            case 3:
                menuProduct();
                break;
            default:
                System.out.println("Không có lựa chọn này");
                break;
        }
    }

    public void sortProductByPrice() {
        products.sort(new ProductComparator());
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println("===> Sắp xếp thành công");
        writeFileInProduct();
    }

    public void readFileInProduct() {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();// skip fisrt line;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                Product product = new Product(data[0], data[1], Double.parseDouble(data[2]));
                products.add(product);
            }
        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        }
    }

    public void writeFileInProduct() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Mã sản phẩm, Tên sản phẩm, Giá");
            for (Product product : products) {
                String line = product.getSku() + "," + product.getName() + "," + product.getPrice();
                bufferedWriter.newLine();
                bufferedWriter.write(line);
            }
            bufferedWriter.close();
        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        }
    }
}
