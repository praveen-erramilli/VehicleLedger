package com.example.controller;

import com.example.dto.OwnerDTO;
import com.example.dto.TransferOwnerDTO;
import com.example.model.Vehicle;
import com.example.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping("/initLedger")
    @ResponseBody
    public String init() throws Exception {
        return vehicleService.initLedger();
    }

    @PostMapping("/vehicle")
    @ResponseBody
    public ResponseEntity<Void> createVehicle(@RequestBody Vehicle vehicle) throws Exception {
         vehicleService.createVehicle(vehicle);
         return ResponseEntity.accepted().build();
    }

    @GetMapping("/vehicle/{vehicleID}")
    @ResponseBody
    public Vehicle getVehicle(@PathVariable String vehicleID) throws Exception {
        return vehicleService.getVehicle(vehicleID);
    }

    @PutMapping("/vehicle/{vehicleID}/owner")
    @ResponseBody
    public Vehicle addOwner(@PathVariable String vehicleID, @RequestBody OwnerDTO owner) throws Exception {
        return vehicleService.addOwner(vehicleID, owner.getOwner());
    }

    @PutMapping("/vehicle/{vehicleID}/transfer")
    @ResponseBody
    public Vehicle transferOwner(@PathVariable String vehicleID, @RequestBody TransferOwnerDTO owner) throws Exception {
        return vehicleService.transferOwner(vehicleID, owner.getTo());
    }
}
