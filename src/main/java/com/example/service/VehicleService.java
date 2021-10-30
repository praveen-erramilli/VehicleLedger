package com.example.service;

import com.example.model.Vehicle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlike.genson.Genson;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class VehicleService {
    public static final String MY_CHANNEL = "mychannel";
    public static final String CONTRACT_NAME = "vehicle";
    @Value("${default.name}")
    private String user;
    @Value("${default.network-config-path}")
    private String netPath;
    private final EnrollAdmin enrollAdmin;
    private final Genson genson = new Genson();

    public String initLedger() throws Exception {
        try {
            // enrollAdmin.enrollAdmin();
        } catch (Exception e) {
            System.err.println(e);
        }
        try (Gateway gateway = connect(getUser("default"))) {
            Network network = gateway.getNetwork(MY_CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);
            System.out.println("Submit Transaction: InitLedger creates the initial set of assets on the ledger.");
            contract.submitTransaction("initLedger");
        }
        return "Success";
    }

    private String getUser(String configName) {
        // return userProperties;
        return user;

    }

    @Async
    public void createVehicle(Vehicle vehicle) throws Exception {
        try (Gateway gateway = connect(getUser("default"))) {
            Network network = gateway.getNetwork(MY_CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);
            ObjectMapper objectMapper = new ObjectMapper();
            String vehicleJson = objectMapper.writeValueAsString(vehicle);
            contract.submitTransaction("createVehicle", vehicleJson);
        }
    }

    @Cacheable("getVehicle")
    public Vehicle getVehicle(String vehicleID) throws Exception {
        Vehicle vehicle = null;
        try (Gateway gateway = connect(getUser("default"))) {
            Network network = gateway.getNetwork(MY_CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);
            byte[] readVehicles = contract.evaluateTransaction("readVehicle", vehicleID);
            ObjectMapper objectMapper = new ObjectMapper();
            vehicle = objectMapper.readValue(readVehicles, Vehicle.class);
        }
        return vehicle;
    }

    public Vehicle addOwner(String vehicleID, String owner) throws Exception {
        try (Gateway gateway = connect(getUser("default"))) {
            Network network = gateway.getNetwork(MY_CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);
            contract.submitTransaction("addOwner", vehicleID, owner);
        }
        return getVehicle(vehicleID);
    }

    public Vehicle transferOwner(String vehicleID, String owner) throws Exception {
        try (Gateway gateway = connect(getUser("default"))) {
            Network network = gateway.getNetwork(MY_CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);
            contract.submitTransaction("transferOwner", vehicleID, owner);
        }
        return getVehicle(vehicleID);
    }


    // helper function for getting connected to the gateway

    public Gateway connect(String userName) throws Exception {
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get(netPath);

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userName).networkConfig(networkConfigPath).discovery(true);
        return builder.connect();
    }
}
