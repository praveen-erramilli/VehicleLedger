package com.example.model;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public class ServiceHistory {
    @Property()
    private String serviceCenter;

    @Property()
    private Long timeStamp;

    public ServiceHistory(String serviceCenter, Long timeStamp){
        this.serviceCenter = serviceCenter;
        this.timeStamp = timeStamp;
    }
}
