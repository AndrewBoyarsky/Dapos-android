package com.example.dapos;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class LedgerEntry {
    long height;
      long  id;
      long fee;
      long amount;
      String sender;
      String recipient;
}
