package com.example.model;

import lombok.Data;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;


@Data
public class ServicingInfo {
    private List<ServiceHistory> serviceHistories;
}