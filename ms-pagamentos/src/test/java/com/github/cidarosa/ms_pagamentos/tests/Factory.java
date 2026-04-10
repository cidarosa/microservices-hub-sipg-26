package com.github.cidarosa.ms_pagamentos.tests;

import com.github.cidarosa.ms_pagamentos.entities.Pagamento;
import com.github.cidarosa.ms_pagamentos.entities.Status;

import java.math.BigDecimal;

public class Factory {

    public static Pagamento createPagamento() {

        Pagamento pagamento = new Pagamento(
                1L, BigDecimal.valueOf(32.25),
                "Briene de Tarth", "3654785412365478",
                "07/05", "354", Status.CRIADO, 1l
        );

        return pagamento;
    }

    public static Pagamento createPagamentoSemId(){
        Pagamento pagamento = createPagamento();
        pagamento.setId(null);
        return pagamento;
    }
}
