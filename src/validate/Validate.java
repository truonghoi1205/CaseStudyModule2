package validate;

import input.Input;

public class Validate {


    public String inputValidName() {
        String name = "";
        while (name.isEmpty()) {
            System.out.println("Nhập tên mới");
            name = Input.getString();
        }
        return name;
    }

    public double inputValidPrice() {
        double price = 0;
        while (price <= 0) {
            System.out.println("Nhập giá mới");
            price = Input.getDou();
        }
        return price;
    }
}
