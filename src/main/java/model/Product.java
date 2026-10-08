package model;

public record Product(int id, String name, String description, double price,
                      int stock, String image, int categoryId, String category) { }