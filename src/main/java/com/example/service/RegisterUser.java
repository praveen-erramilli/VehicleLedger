
package com.example.service;

import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric_ca.sdk.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.util.Properties;
import java.util.Set;

@Service
public class RegisterUser {
    @Value("${pem.path}")
    private String pemPath;
    public  void register(String userName, String affiliation,String org) throws Exception {

        // Create a CA client for interacting with the CA.
        Properties props = new Properties();
        props.put("pemFile", pemPath);
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

        // Check to see if we've already enrolled the user.
        if (wallet.get(userName) != null) {
            ;
            throw new IllegalArgumentException(String.format("An identity for the user %s already exists in the " +
                    "wallet", userName));
            //return "";
        }

        registerUser(userName, caClient, wallet, affiliation,org);
        //enrollIdentitytoWallet(caClient, wallet, admin, registrationRequest);
    }

    private static void registerUser(String user, HFCAClient caClient, Wallet wallet, String affiliation,String org) throws Exception {
        User admin = getAdmin(wallet);
        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest(user);
        registrationRequest.setAffiliation(affiliation);
        registrationRequest.setEnrollmentID(user);
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        enrollIdentitytoWallet(user, org, caClient, wallet, enrollmentSecret);
    }

    private static User getAdmin(Wallet wallet) throws IOException {
        X509Identity adminIdentity = (X509Identity) wallet.get("admin");
        if (adminIdentity == null) {
            throw new IllegalArgumentException("\"admin\" needs to be enrolled and added to the wallet first");
            //return "";
        }
        User admin = new User() {

            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(adminIdentity.getCertificate());
                    }
                };
            }

            @Override
            public String getMspId() {
                return "Org1MSP";
            }

        };
        return admin;
    }

    public static void enrollIdentitytoWallet(String userName, String org, HFCAClient caClient, Wallet wallet, String enrollmentSecret) throws RegistrationException, InvalidArgumentException, EnrollmentException, CertificateException, IOException {

        Enrollment enrollment = caClient.enroll(userName, enrollmentSecret);
        Identity user = Identities.newX509Identity(org, enrollment);
        wallet.put(userName, user);
        System.out.printf("Successfully enrolled user %s and imported it into the wallet%n", userName);
    }

}
