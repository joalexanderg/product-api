package com.incomex.product_api.integrationtest;

import com.incomex.product_api.model.JobStatus;
import com.incomex.product_api.model.entity.Categories;
import com.incomex.product_api.model.entity.Products;
import com.incomex.product_api.model.entity.Suppliers;
import com.incomex.product_api.repository.CategoriesRepository;
import com.incomex.product_api.repository.ProductsRepository;
import com.incomex.product_api.repository.SuppliersRepository;
import com.incomex.product_api.service.async.AsyncProductsGeneratorService;
import com.incomex.product_api.service.job.JobRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class AsyncProductsGeneratorIntegrationTest {

    @Autowired
    private AsyncProductsGeneratorService generatorService;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private SuppliersRepository suppliersRepository;

    @Autowired
    private JobRegistry jobRegistry;

    @BeforeEach
    void setUp() {
        // Limpiar toda la base
        productsRepository.deleteAll();
        categoriesRepository.deleteAll();

        // Insertar 2 categorías
        Categories c1 = new Categories();
        c1.setCategoryName("SERVIDORES");
        c1.setDescription("Infraestructura");
        c1.setPicture("https://img.com/1");

        Categories c2 = new Categories();
        c2.setCategoryName("CLOUD");
        c2.setDescription("Nube");
        c2.setPicture("https://img.com/2");

        categoriesRepository.save(c1);
        categoriesRepository.save(c2);

    }
    @Test
    void testGenerateRandomProducts_async() {
        int count = 10;

        // Registrar job y obtener el UUID
        UUID jobId = jobRegistry.registerJob();

        // Ejecutar el proceso asincrónico
        generatorService.generateRandomProducts(jobId, count);

        // Esperar hasta que el estado del Job sea COMPLETED
        await()
                .atMost(20, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> jobRegistry.getStatus(jobId) == JobStatus.COMPLETED);

        // Verificar cantidad de productos insertados
        long actual = productsRepository.count();
        assertEquals(count, actual, "Debe haberse insertado exactamente " + count + " productos.");
    }
}