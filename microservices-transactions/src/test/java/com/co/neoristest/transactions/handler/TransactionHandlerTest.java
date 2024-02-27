package com.co.neoristest.transactions.handler;

import com.co.neoristest.common.utils.ParseDate;
import com.co.neoristest.transactions.domain.dto.TransactionResponseDto;
import com.co.neoristest.transactions.routing.RouterFunctionConfig;
import com.co.neoristest.transactions.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterFunctionConfig.class, TransactionHandler.class})
@WebFluxTest
class TransactionHandlerTest {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private TransactionService transactionService;

    private WebTestClient webTestClient;

    private static final String TRANSACTION_ID1 = "65de43558e12e17e9976b5ac";
    private static final String TRANSACTION_ID2 = "65de43558e12e17e9976b6ac";

    private Flux<TransactionResponseDto> transactionResponseDtoFlux;

    private TransactionResponseDto transactionResponseDto;

    private final String BASE_URL = "/api/v1/transacciones";

    @BeforeEach
    public void setUp() throws Exception {

        webTestClient = WebTestClient.bindToApplicationContext(context).build();

        this.transactionResponseDto = new TransactionResponseDto(TRANSACTION_ID1,
                ParseDate.formattDateTimeToParam("25/02/2024 15:17"),
                new BigDecimal("100"),
                "40985777321",
                new BigDecimal("1456255.00"),
                "82954998996",
                new BigDecimal("14655.00"),
                true
        );

        List<TransactionResponseDto> transactionResponseDtosList = List.of(transactionResponseDto,
                new TransactionResponseDto(TRANSACTION_ID2,
                        ParseDate.formattDateTimeToParam("25/02/2024 09:17"),
                        new BigDecimal("200"),
                        "40985777321",
                        new BigDecimal("1456255.00"),
                        "82954998996",
                        new BigDecimal("14655.00"),
                        true
                ));

        this.transactionResponseDtoFlux = Flux.fromIterable(transactionResponseDtosList);

    }

    @Test
    void testRequestTransacionHanlderGetAllTransactions() {

        when(this.transactionService.findAll()).thenReturn(this.transactionResponseDtoFlux);

        webTestClient.get()
                .uri(BASE_URL.concat("/"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(TransactionResponseDto.class)
                .consumeWith(response -> {
                    List<TransactionResponseDto> responseDTOS = response.getResponseBody();
                    assert responseDTOS != null;
                    Assertions.assertTrue(responseDTOS.size() > 0);
                });

    }

    @Test
    void testRequestTransacionHanlderFindById() {

        Mono<TransactionResponseDto> transactionResponseDtoMono = Mono.just(this.transactionResponseDto);

        when(this.transactionService.findById(TRANSACTION_ID1)).thenReturn(transactionResponseDtoMono);

        webTestClient.get()
                .uri(BASE_URL.concat("/{id}"), Collections.singletonMap("id", TRANSACTION_ID1))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isNotEmpty();
    }

}
