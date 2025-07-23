package com.departamentos.aplication.repositories;

import com.departamentos.aplication.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByNomeContainingIgnoreCase(String nome);
    Optional<Item> findByNomeIgnoreCase(String nome);
}
