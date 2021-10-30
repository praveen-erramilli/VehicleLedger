package com.example.model;

import lombok.Data;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@Data
public class ServiceHistory {
    private String serviceCenter;
    private Long timeStamp;
}
