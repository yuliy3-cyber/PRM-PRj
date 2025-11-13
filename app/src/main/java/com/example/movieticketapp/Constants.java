package com.example.movieticketapp;

/**
 * Constants class for application-wide configuration values
 */
public class Constants {
    // Wallet configuration
    public static final long DEFAULT_WALLET_AMOUNT = 1000000; // 1 triệu VNĐ - Số tiền mặc định khi tạo user mới
    
    // Admin configuration
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    
    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

