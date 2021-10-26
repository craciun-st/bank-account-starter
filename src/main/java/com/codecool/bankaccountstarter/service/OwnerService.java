package com.codecool.bankaccountstarter.service;

import com.codecool.bankaccountstarter.model.OwnerInfo;
import com.codecool.bankaccountstarter.repository.OwnerInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;

@Service
public class OwnerService {

    private OwnerInfoRepository ownerInfoRepo;

    @Autowired
    public OwnerService(OwnerInfoRepository ownerInfoRepo) {
        this.ownerInfoRepo = ownerInfoRepo;
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = {IllegalArgumentException.class, IOException.class}
    )
    public OwnerInfo saveOwnerInfo(OwnerInfo ownerInfo) {
        return ownerInfoRepo.save(ownerInfo);
    }
}
