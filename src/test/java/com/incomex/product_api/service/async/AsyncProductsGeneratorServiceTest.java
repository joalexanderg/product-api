package com.incomex.product_api.service.async;

import com.incomex.product_api.model.JobStatus;
import com.incomex.product_api.model.entity.Products;
import com.incomex.product_api.repository.ProductsRepository;
import com.incomex.product_api.service.job.JobRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class AsyncProductsGeneratorServiceTest {

    @Mock
    private ProductsRepository repository;

    @Mock
    private JobRegistry jobRegistry;

    @InjectMocks
    private AsyncProductsGeneratorService service;

    @Captor
    private ArgumentCaptor<List<Products>> productBatchCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateRandomProducts_shouldGenerateAndSaveProductsInBatches() {
        UUID jobId = UUID.randomUUID();
        int count = 1500; // para forzar al menos dos lotes

        service.generateRandomProducts(jobId, count);

        // Verifica que se actualizó el estado del job
        verify(jobRegistry).updateStatus(jobId, JobStatus.RUNNING);
        verify(jobRegistry).updateStatus(jobId, JobStatus.COMPLETED);

        // Verifica que saveAll fue llamado varias veces
        verify(repository, atLeast(1)).saveAll(productBatchCaptor.capture());
        verify(repository, atLeast(1)).flush();

        // Asegura que al menos un lote contiene productos
        List<List<Products>> allBatches = productBatchCaptor.getAllValues();
        int totalSaved = allBatches.stream().mapToInt(List::size).sum();
        assert totalSaved == count;
    }

    @Test
    void generateRandomProducts_shouldHandleExceptionAndSetStatusToFailed() {
        UUID jobId = UUID.randomUUID();
        int count = 10;

        // Simular excepción en saveAll
        doThrow(new RuntimeException("DB Error")).when(repository).saveAll(any());

        service.generateRandomProducts(jobId, count);

        verify(jobRegistry).updateStatus(jobId, JobStatus.RUNNING);
        verify(jobRegistry).updateStatus(jobId, JobStatus.FAILED);
    }
}

