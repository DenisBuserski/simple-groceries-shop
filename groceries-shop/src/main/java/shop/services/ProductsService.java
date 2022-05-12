package shop.services;

import shop.entities.Product;

import java.io.IOException;

public interface ProductsService {

    void addProducts() throws IOException;

    Product getProduct(String name);
}
