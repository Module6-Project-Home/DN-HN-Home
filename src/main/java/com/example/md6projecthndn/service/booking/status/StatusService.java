package com.example.md6projecthndn.service.booking.status;


import com.example.md6projecthndn.model.entity.booking.Status;
import com.example.md6projecthndn.repository.booking.IStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusService implements IStatusService {
    @Autowired
    private IStatusRepository statusRepository;
    @Override
    public Iterable<Status> findAll() {
        return statusRepository.findAll();
    }

    @Override
    public Status findById(Long id) {
        return statusRepository.findById(id).orElse(null);
    }
    @Override
    public void save(Status status) {
        statusRepository.save(status);
    }

    @Override
    public void delete(Long id) {
        statusRepository.deleteById(id);
    }



}
