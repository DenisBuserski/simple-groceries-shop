package shop.services;

import shop.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.repositories.ProductRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ProductsServiceImpl implements ProductsService {
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.txt";

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addProducts() throws IOException {
        Files.readAllLines(Path.of(PRODUCTS_FILE_PATH))
                .stream()
                .filter(s -> !s.isBlank())
                .map(s -> s.split("\\s+"))
                .map(product -> new Product(product[0], Double.parseDouble(product[1])))
                .forEach(productRepository::save);
    }

    @Override
    public Product getProduct(String name) {
        return this.productRepository.findByName(name);
    }
}
