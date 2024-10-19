package com.example.md6projecthndn.service.booking.status;


import com.example.md6projecthndn.model.entity.property.Status;
import com.example.md6projecthndn.service.IGenerateService;

import java.util.Optional;

public interface IStatusService extends IGenerateService<Status> {

    Optional<Status> findByName(Status.PROPERTY_STATUS newStatus);
}
