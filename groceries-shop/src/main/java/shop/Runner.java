package shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import shop.entities.Product;
import shop.services.ProductsService;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Runner implements CommandLineRunner {

    private final ProductsService productService;

    @Autowired
    public Runner(ProductsService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.productService.addProducts();

        System.out.println("Please select the products you want to buy");
        System.out.println("Press 0 when finish selecting");
        System.out.println("Press 1 for Apple");
        System.out.println("Press 2 for Banana");
        System.out.println("Press 3 for Tomato");
        System.out.println("Press 4 for Potato");
        System.out.println("Press 5 for Watermelon");

        List<Product> shoppingCart = new ArrayList<>();
        selectProducts(shoppingCart);

        if (shoppingCart.size() <= 2) {
            System.out.println("You have bought very few products and you cannot get any discount!");
            printReceipt(shoppingCart, 0, 0);
        } else if (shoppingCart.size() <= 4) {
            System.out.println("You can use our \"2 for 3\" discount!");
            double discount2For3 = discount2For3(shoppingCart);
            printReceipt(shoppingCart, discount2For3, 0);
        }  else if (shoppingCart.size() > 4){
            System.out.println("You can use \"2 for 3\" discount and \"Buy 1 get the other in half price\" discount");
            List<Product> disc1Products = shoppingCart.stream().limit(3).collect(Collectors.toList());
            List<Product> disc2Products = shoppingCart.subList(3, shoppingCart.size());

            double discount2For3 = discount2For3(disc1Products);
            double discount50Percent = discount50Percent(disc2Products);
            printReceipt(shoppingCart, discount2For3, discount50Percent);
        }

    }

    private double discount50Percent(List<Product> disc2Products) {
        double res = 0;
        Collections.sort(disc2Products, Comparator.comparing(Product::getPrice));
        if (disc2Products.get(0).getName().equals(disc2Products.get(1).getName())) {
            res = disc2Products.get(0).getPrice() / 2;
        }
        return res;
    }

    private double discount2For3(List<Product> list) {
        return list.stream().map(Product::getPrice).mapToDouble(Double::doubleValue).min().getAsDouble();
    }

    private void printReceipt(List<Product> shoppingCart, double discount2For3, double discount50Percent) {
        String boughtProducts = shoppingCart.stream().map(Product::getName).collect(Collectors.joining(", "));
        System.out.println("You have bought " + boughtProducts);
        double totalSumInClouds = shoppingCart.stream().mapToDouble(Product::getPrice).sum() - discount2For3 - discount50Percent;
        // 1aws = 100c
        double totalSumInAWS = AWSConverter(totalSumInClouds);
        System.out.printf("Your total sum is %.2f aws\n", totalSumInAWS);
    }

    private double AWSConverter(double totalSumInClouds) {
        double totalSumInAWS = 0;
        while (totalSumInClouds >= 100) {
            totalSumInAWS++;
            totalSumInClouds -= 100;
        }
        totalSumInAWS += totalSumInClouds / 100;
        return totalSumInAWS;
    }

    private void selectProducts(List<Product> shoppingCart) {
        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().split("\\s*");
        for (String in : input) {
            int productNumber = Integer.parseInt(in);
            if (productNumber == 0) {
                break;
            }
                switch (productNumber) {
                    case 1 -> shoppingCart.add(this.productService.getProduct("apple"));
                    case 2 -> shoppingCart.add(this.productService.getProduct("banana"));
                    case 3 -> shoppingCart.add(this.productService.getProduct("tomato"));
                    case 4 -> shoppingCart.add(this.productService.getProduct("potato"));
                    case 5 -> shoppingCart.add(this.productService.getProduct("watermelon"));
            }
        }
    }
}
