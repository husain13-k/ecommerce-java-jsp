package model;

import java.sql.Timestamp;

public record Order(int id, double total, String status, String address,
                    Timestamp date, String customer) { }