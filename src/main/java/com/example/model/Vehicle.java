/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.example.model;

import lombok.Data;

import java.util.List;

@Data
public class Vehicle {
    private String vehicleId;
    private ServicingInfo servicingInfo;
    private String currentOwner;
    private List<String> owners;

}