package com.rocketlearning.simcallingmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rocketlearning.simcallingmanagement.entity.SimData;
import com.rocketlearning.simcallingmanagement.repository.SimDataRepository;

@Service
public class SimDataService {

    @Autowired
    private SimDataRepository simDataRepository;

    public List<SimData> getAllSims() {

        return simDataRepository.findAll();

    }

    public void saveSim(SimData simData) {

        simDataRepository.save(simData);

    }

}