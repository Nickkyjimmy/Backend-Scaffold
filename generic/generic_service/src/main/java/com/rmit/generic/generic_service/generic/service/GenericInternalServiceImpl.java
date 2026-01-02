package com.rmit.generic.generic_service.generic.service;

import com.rmit.generic.generic_api.internal.dto.CreateGenericDto;
import com.rmit.generic.generic_api.internal.dto.GetGenericDto;
import com.rmit.generic.generic_api.internal.dto.ListGenericDto;
import com.rmit.generic.generic_api.internal.dto.UpdateGenericDto;
import com.rmit.generic.generic_api.internal.service.GenericInternalService;
import com.rmit.generic.generic_service.generic.model.GenericModel;
import com.rmit.generic.generic_service.generic.repo.GenericRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of GenericInternalService
 * Provides full CRUD operations for Generic entity
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GenericInternalServiceImpl implements GenericInternalService {
    
    private final GenericRepo genericRepo;
    
    @Override
    @Transactional
    public UUID createGeneric(CreateGenericDto req) {
        log.info("Creating new Generic entity with name: {}", req.getName());
        
        GenericModel model = GenericModel.builder()
                .name(req.getName())
                .value(req.getValue())
                .build();
        
        GenericModel saved = genericRepo.save(model);
        log.info("Created Generic entity with id: {}", saved.getId());
        
        return saved.getId();
    }
    
    @Override
    public GetGenericDto getGeneric(UUID id) {
        log.debug("Fetching Generic entity with id: {}", id);
        
        GenericModel model = genericRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Generic entity not found with id: " + id));
        
        return GetGenericDto.builder()
                .id(model.getId())
                .name(model.getName())
                .value(model.getValue())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }
    
    @Override
    public List<ListGenericDto.ListGenericRes> listGenerics() {
        log.debug("Listing all Generic entities");
        
        List<GenericModel> models = genericRepo.findAll();
        
        return models.stream()
                .map(model -> ListGenericDto.ListGenericRes.builder()
                        .id(model.getId())
                        .name(model.getName())
                        .value(model.getValue())
                        .createdAt(model.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void updateGeneric(UUID id, UpdateGenericDto req) {
        log.info("Updating Generic entity with id: {}", id);
        
        GenericModel model = genericRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Generic entity not found with id: " + id));
        
        // Update only non-null fields
        if (req.getName() != null) {
            model.setName(req.getName());
        }
        if (req.getValue() != null) {
            model.setValue(req.getValue());
        }
        
        genericRepo.save(model);
        log.info("Updated Generic entity with id: {}", id);
    }
    
    @Override
    @Transactional
    public void deleteGeneric(UUID id) {
        log.info("Deleting Generic entity with id: {}", id);
        
        if (!genericRepo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Generic entity not found with id: " + id);
        }
        
        genericRepo.deleteById(id);
        log.info("Deleted Generic entity with id: {}", id);
    }
}

