package com.example.model;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;

@DataType()
public class ServicingInfo {
    @Property()
    private List<ServiceHistory> serviceHistories;

    public ServicingInfo(){

    }
    public ServicingInfo(List<ServiceHistory> serviceHistories){
        this.serviceHistories = serviceHistories;
    }
    public List<ServiceHistory> getServiceHistories() {
        return serviceHistories;
    }
}