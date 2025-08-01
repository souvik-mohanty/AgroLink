package com.AgroLink.order.config;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class NanoIdGenerator {

    private static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int ID_LENGTH = 20;

    public String generateOrderId() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        String nano = NanoIdUtils.randomNanoId(RANDOM, ALPHABET, ID_LENGTH);
        return  dateTime + "-" + nano;
    }
}
