package com.github.cidarosa.ms_pagamentos.repository;

import com.github.cidarosa.ms_pagamentos.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
