package com.github.cidarosa.ms_pagamentos.service;

import com.github.cidarosa.ms_pagamentos.dto.PagamentoDTO;
import com.github.cidarosa.ms_pagamentos.entities.Pagamento;
import com.github.cidarosa.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.github.cidarosa.ms_pagamentos.repository.PagamentoRepository;
import com.github.cidarosa.ms_pagamentos.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Long existingId;
    private Long nonExistingId;

    private Pagamento pagamento;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = Long.MAX_VALUE;

        pagamento = Factory.createPagamento();
    }

    @Test
    void deletePagamentoByIdShouldDeleteWhenIdExists() {

        Mockito.when(pagamentoRepository.existsById(existingId)).thenReturn(true);

        pagamentoService.deletePagamento(existingId);

        Mockito.verify(pagamentoRepository).existsById(existingId);

        Mockito.verify(pagamentoRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    void deletePagamentoByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Mockito.when(pagamentoRepository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class,

                () -> {
                    pagamentoService.deletePagamento(nonExistingId);
                }
        );

        Mockito.verify(pagamentoRepository).existsById(nonExistingId);

        Mockito.verify(pagamentoRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    void findPagamentoByIdShouldReturnPagamentoDTOWhenIdExists() {

        Mockito.when(pagamentoRepository.findById(existingId))
                .thenReturn(Optional.of(pagamento));

        PagamentoDTO restul = pagamentoService.findPagamentoById(existingId);

        Assertions.assertNotNull(pagamento);
        Assertions.assertEquals(pagamento.getId(), restul.getId());
        Assertions.assertEquals(pagamento.getValor(), restul.getValor());

        Mockito.verify(pagamentoRepository).findById(existingId);
        Mockito.verifyNoMoreInteractions(pagamentoRepository);

    }

    @Test
    void findPagamentoByIdShoudThrowResourceNotFoundExceptionWhenIdNotExist() {

        Mockito.when(pagamentoRepository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pagamentoService.findPagamentoById(nonExistingId));

        Mockito.verify(pagamentoRepository).findById(nonExistingId);
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    void givenValidParamsAndIdIsNull_whenSave_thenShouldPersistPagamento() {

        Mockito.when(pagamentoRepository.save(any(Pagamento.class)))
                .thenReturn(pagamento);
        pagamento.setId(null);
        PagamentoDTO inputDTO = new PagamentoDTO(pagamento);

        PagamentoDTO result = pagamentoService.savePagamento(inputDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());

        Mockito.verify(pagamentoRepository).save(any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    void updatePagamentoShouldReturnPagamentoDTOWhenIdExists() {

        Long id = pagamento.getId();
        Mockito.when(pagamentoRepository.getReferenceById(existingId)).thenReturn(pagamento);
        Mockito.when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        PagamentoDTO result = pagamentoService.updatePagamento(id, new PagamentoDTO(pagamento));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());

        Mockito.verify(pagamentoRepository).getReferenceById(existingId);
        Mockito.verify(pagamentoRepository).save(any(Pagamento.class));

        Mockito.verifyNoMoreInteractions(pagamentoRepository);


    }

    @Test
    void updatePagamentoShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Mockito.when(pagamentoRepository.getReferenceById(nonExistingId))
                .thenThrow(EntityNotFoundException.class);

        PagamentoDTO inputDTO = new PagamentoDTO(pagamento);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> pagamentoService.updatePagamento(nonExistingId, inputDTO)
        );

        Mockito.verify(pagamentoRepository).getReferenceById(nonExistingId);
        Mockito.verify(pagamentoRepository, Mockito.never()).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

}
