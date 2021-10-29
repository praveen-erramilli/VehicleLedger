package com.example.service;

import com.example.model.Vehicle;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.hyperledger.fabric.gateway.*;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class VehicleService {
    public static final String MY_CHANNEL = "mychannel";
    public static final String CONTRACT_NAME = "basic";

    private final EnrollAdmin enrollAdmin;
    private final RegisterUser registerUser;

    public String initLedger() throws Exception {
        try {
            enrollAdmin.enrollAdmin();
            registerUser.registerUser();
        } catch (Exception e) {
            System.err.println(e);
        }
        try(Gateway gateway = connect()) {
            Network network = gateway.getNetwork(MY_CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);

            System.out.println("Submit Transaction: InitLedger creates the initial set of assets on the ledger.");
            contract.submitTransaction("initLedger");
        }
        return "Success";
    }

    public Vehicle createVehicle(String vehicleID) throws Exception {
        try (Gateway gateway = connect()) {
            Network network = gateway.getNetwork(MY_CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);
            contract.submitTransaction("createVehicle", vehicleID);
        }
        return getVehicle(vehicleID);
    }

    public Vehicle getVehicle(String vehicleID) throws Exception {
        Vehicle vehicle = null;
        try (Gateway gateway = connect()) {
            Network network = gateway.getNetwork(MY_CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);
            byte[] readVehicles = contract.evaluateTransaction("readVehicle", vehicleID);
            ObjectMapper objectMapper = new ObjectMapper();
            vehicle = objectMapper.readValue(readVehicles, Vehicle.class);
        }
        return vehicle;
    }

    public Vehicle addOwner(String vehicleID, String owner) throws Exception{
        try (Gateway gateway = connect()) {
            Network network = gateway.getNetwork(MY_CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);
            contract.submitTransaction("addOwner", vehicleID, owner);
        }
        return getVehicle(vehicleID);
    }

    public Vehicle transferOwner(String vehicleID, String owner) throws Exception {
        try (Gateway gateway = connect()) {
            Network network = gateway.getNetwork(MY_CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);
            contract.submitTransaction("transferOwner", vehicleID, owner);
        }
        return getVehicle(vehicleID);
    }


    // helper function for getting connected to the gateway

    public static Gateway connect() throws Exception{
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        return builder.connect();
    }
}
