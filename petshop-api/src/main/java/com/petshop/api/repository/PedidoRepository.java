package com.petshop.api.repository;

import com.petshop.api.entity.Pedido;
import com.petshop.api.entity.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);

    List<Pedido> findByStatus(StatusPedido status);

    Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);
}
