package com.razz.day2;

import java.util.HashMap;
import java.util.Map;

public class VendingMachine {
    private Map<Integer, Integer> floatCoins;
    private Map<String, Product> inventory;

    public VendingMachine(Map<Integer, Integer> floatCoins, Map<String, Product> inventory) {
        this.floatCoins = floatCoins;
        this.inventory = inventory;
    }

    public void displayInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Product> entry : inventory.entrySet()) {
            Product product = entry.getValue();
            System.out.println(product.getQuantity() + "x" + entry.getKey() + " each " + product.getPrice() + "p");
        }
    }

    public void updateInventory(String productName, int quantity, int price) {
        if (inventory.containsKey(productName)) {
            Product product = inventory.get(productName);
            product.setQuantity(product.getQuantity() + quantity);
            product.setPrice(price);
        } else {
            inventory.put(productName, new Product(quantity, price));
        }
    }

    public void updateFloat(Map<Integer, Integer> coins) {
        coins.forEach((coin, count) -> floatCoins.merge(coin, count, Integer::sum));
    }

    public String buyProduct(String productName, int quantity, Map<Integer, Integer> coinsInserted) {
        if (!inventory.containsKey(productName) || inventory.get(productName).getQuantity() < quantity) {
            return "Error: Insufficient quantity of " + productName + " in stock.";
        }

        int totalPrice = inventory.get(productName).getPrice() * quantity;
        int totalInserted = coinsInserted.entrySet().stream()
                .mapToInt(entry -> entry.getKey() * entry.getValue())
                .sum();

        if (totalInserted < totalPrice) {
            return "Error: Insufficient funds.";
        }

        int change = totalInserted - totalPrice;

        if (change > 0) {
            calculateChange(change);
        }

        inventory.get(productName).setQuantity(inventory.get(productName).getQuantity() - quantity);

        return "Successfully purchased " + quantity + "x" + productName + ". Change: " + change + "p";
    }

    private void calculateChange(int change) {
        int[] coinsAvailable = {100, 50, 25, 10, 5, 1};

        for (int coin : coinsAvailable) {
            while (change >= coin && floatCoins.getOrDefault(coin, 0) > 0) {
                floatCoins.put(coin, floatCoins.get(coin) - 1);
                change -= coin;
            }
        }
    }

    public static void main(String[] args) {
        Map<Integer, Integer> initialFloat = new HashMap<>();
        initialFloat.put(1, 10);
        initialFloat.put(5, 10);
        initialFloat.put(10, 10);
        initialFloat.put(25, 10);
        initialFloat.put(50, 10);
        initialFloat.put(100, 10);

        Map<String, Product> initialInventory = new HashMap<>();
        initialInventory.put("Coke", new Product(10, 25));
        initialInventory.put("Pepsi", new Product(15, 35));
        initialInventory.put("Sprite", new Product(12, 45));

        VendingMachine vendingMachine = new VendingMachine(initialFloat, initialInventory);
        vendingMachine.displayInventory();

        vendingMachine.updateInventory("Fanta", 8, 40);
        vendingMachine.displayInventory();

        Map<Integer, Integer> coinsInserted = new HashMap<>();
        coinsInserted.put(1, 5);
        coinsInserted.put(25, 2);

        System.out.println(vendingMachine.buyProduct("Coke", 2, coinsInserted));
        vendingMachine.displayInventory();
    }
}
//
//class Product {
//    private int quantity;
//    private int price;
//
//    public Product(int quantity, int price) {
//        this.quantity = quantity;
//        this.price = price;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }
