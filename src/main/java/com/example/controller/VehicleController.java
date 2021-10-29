package com.example.controller;

import com.example.model.Vehicle;
import com.example.service.VehicleService;
import lombok.AllArgsConstructor;
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

    @PostMapping("/vehicle/{vehicleID}")
    @ResponseBody
    public Vehicle createVehicle(@PathVariable String vehicleID) throws Exception {
        return vehicleService.createVehicle(vehicleID);
    }

    @GetMapping("/vehicle/{vehicleID}")
    @ResponseBody
    public Vehicle getVehicle(@PathVariable String vehicleID) throws Exception {
        return vehicleService.getVehicle(vehicleID);
    }

    @PostMapping("/vehicle/{vehicleID}/owner")
    @ResponseBody
    public Vehicle addOwner(@PathVariable String vehicleID, @RequestBody String owner) throws Exception {
        return vehicleService.addOwner(vehicleID, owner);
    }

    @PutMapping("/vehilce/{vehicleID}/currentOwner")
    @ResponseBody
    public Vehicle transferOwner(@PathVariable String vehicleID, @RequestBody String owner) throws Exception {
        return vehicleService.transferOwner(vehicleID, owner);
    }
}
