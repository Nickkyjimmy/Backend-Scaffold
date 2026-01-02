package com.rmit.generic.generic_service.common.config;

import com.rmit.generic.generic_service.generic.model.GenericModel;
import com.rmit.generic.generic_service.generic.repo.GenericRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Data Generator Configuration
 * Generates mock data on application startup when seed key is provided
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class DataGeneratorConfig implements CommandLineRunner {
    
    private final GenericRepo genericRepo;
    
    @Value("${seed-data.key:}")
    private String seedKey;
    
    @Value("${seed-data.enabled:false}")
    private boolean seedEnabled;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if seeding is enabled (either by flag or by seed key)
        boolean shouldSeed = seedEnabled || (seedKey != null && !seedKey.isEmpty() && seedKey.equals("seed_db"));
        
        if (!shouldSeed) {
            log.info("Data seeding is disabled. Skipping data generation...");
            return;
        }
        
        // Check if data already exists
        long existingCount = genericRepo.count();
        if (existingCount > 0) {
            log.info("Database already contains {} entities. Skipping data generation.", existingCount);
            return;
        }
        
        log.info("Starting data generation...");
        
        // Generate mock data
        List<GenericModel> mockData = generateMockData();
        
        // Save to database
        genericRepo.saveAll(mockData);
        
        log.info("Successfully generated and saved {} mock entities to database.", mockData.size());
    }
    
    /**
     * Generate mock GenericModel entities
     * Replace with your specific mock data generation logic
     */
    private List<GenericModel> generateMockData() {
        return Arrays.asList(
                GenericModel.builder()
                        .name("Sample Entity 1")
                        .value("Value 1")
                        .build(),
                GenericModel.builder()
                        .name("Sample Entity 2")
                        .value("Value 2")
                        .build(),
                GenericModel.builder()
                        .name("Sample Entity 3")
                        .value("Value 3")
                        .build(),
                GenericModel.builder()
                        .name("Sample Entity 4")
                        .value("Value 4")
                        .build(),
                GenericModel.builder()
                        .name("Sample Entity 5")
                        .value("Value 5")
                        .build()
        );
    }
}

