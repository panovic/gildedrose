package com.example.gildedrose.controller;

import com.example.gildedrose.exceptions.UserNotFoundException;
import com.example.gildedrose.model.PurchaseRequest;
import com.example.gildedrose.service.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc(addFilters=false)
public class PurchaseControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PurchaseService purchaseService;

    private String requestJson;

    @BeforeEach
    public void setup() throws Exception {
        PurchaseRequest purchaseReq = new PurchaseRequest(1L, 10);
        requestJson =  new ObjectMapper().writeValueAsString(purchaseReq);
    }

    @Test
    public void whenPurchaseItem_thenSuccess() throws Exception {
        doNothing().when(purchaseService).purchaseItem(any(PurchaseRequest.class), anyString());

        mvc.perform(post("/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .header("x-apikey", "ABC123"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPurchaseItemWithInvalidApiKey_thenReturnUnauthorized() throws Exception {
        doThrow(new UserNotFoundException()).when(purchaseService).purchaseItem(any(PurchaseRequest.class), anyString());

        mvc.perform(post("/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .header("x-apikey", "abc123"))
                .andExpect(status().isUnauthorized());
    }
}
