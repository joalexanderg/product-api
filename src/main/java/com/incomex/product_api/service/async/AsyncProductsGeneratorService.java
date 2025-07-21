package com.incomex.product_api.service.async;

import com.incomex.product_api.model.entity.Categories;
import com.incomex.product_api.model.entity.Products;
import com.incomex.product_api.model.entity.Suppliers;
import com.incomex.product_api.model.JobStatus;
import com.incomex.product_api.repository.ProductsRepository;
import com.incomex.product_api.service.job.JobRegistry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AsyncProductsGeneratorService {

    private final ProductsRepository repository;
    private final JobRegistry jobRegistry;
    private final Logger logger = LoggerFactory.getLogger(AsyncProductsGeneratorService.class);

    @Async
    @Transactional
    public void generateRandomProducts(UUID jobId, int count) {
        jobRegistry.updateStatus(jobId, JobStatus.RUNNING);

        try {
            final int batchSize = 1000;
            Random random = new Random();

            for (int i = 0; i < count; i += batchSize) {
                List<Products> batch = new ArrayList<>(batchSize);
                for (int j = 0; j < batchSize && (i + j) < count; j++) {
                    Products p = new Products();
                    p.setProductName("Producto " + UUID.randomUUID());
                    p.setQuantityPerUnit((random.nextInt(10) + 1) + " unidades");
                    p.setUnitPrice(BigDecimal.valueOf(10 + random.nextDouble() * 990));
                    p.setUnitsInStock(random.nextInt(100));
                    p.setUnitsOnOrder(random.nextInt(50));
                    p.setReorderLevel(random.nextInt(20));
                    p.setDiscontinued(random.nextBoolean());
                    p.setSupplierID(new Suppliers(1L + random.nextInt(2)));
                    p.setCategoryID(new Categories(1L + random.nextInt(2)));
                    batch.add(p);
                }

                repository.saveAll(batch);
                repository.flush();
                logger.info("✅ Lote {} insertado", (i / batchSize) + 1);
            }

            jobRegistry.updateStatus(jobId, JobStatus.COMPLETED);
        } catch (Exception e) {
            logger.error("❌ Error en la carga de productos", e);
            jobRegistry.updateStatus(jobId, JobStatus.FAILED);
        }
    }
}