package com.paymentchain.billing;

import com.paymentchain.billing.common.InvoiceRequestMapper;
import com.paymentchain.billing.common.InvoiceResponseMapper;
import com.paymentchain.billing.controller.InvoiceRestController;
import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import com.paymentchain.billing.exception.BusinessRuleException;
import com.paymentchain.billing.repository.InvoiceRepository;
import java.util.List;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicApplicationTests {

    @Mock
    private InvoiceRepository billingRepository;

    @Mock
    private InvoiceRequestMapper irm;

    @Mock
    private InvoiceResponseMapper irspm;

    @InjectMocks
    private InvoiceRestController controller;

    @Test
    void listReturnsInvoicesWhenRepositoryHasData() throws BusinessRuleException {
        Invoice invoice = new Invoice(
                1L, 10L, "FAC-TEST-001", "Factura de prueba", 100.50
        );

        InvoiceResponse response = new InvoiceResponse();
        response.setInvoiceId(1L);
        response.setCustomer(10L);
        response.setNumber("FAC-TEST-001");
        response.setDetail("Factura de prueba");
        response.setAmount(100.50);

        when(billingRepository.findAll()).thenReturn(List.of(invoice));
        when(irspm.InvoiceListToInvoiceResposeList(List.of(invoice)))
                .thenReturn(List.of(response));

        List<InvoiceResponse> result = controller.list();

        assertEquals(1, result.size());
        assertEquals("FAC-TEST-001", result.get(0).getNumber());
        assertEquals(100.50, result.get(0).getAmount());

        verify(billingRepository).findAll();
    }

    @Test
    void listThrowsNotFoundWhenRepositoryIsEmpty() {
        when(billingRepository.findAll()).thenReturn(Collections.emptyList());

        BusinessRuleException exception = assertThrows(
                BusinessRuleException.class,
                () -> controller.list()
        );

        assertEquals("NO_FOUND", exception.getCode());
        assertEquals(org.springframework.http.HttpStatus.NOT_FOUND,
                exception.getHttpStatus());

        verify(billingRepository).findAll();
        verifyNoInteractions(irspm);
    }
}
