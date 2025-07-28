package com.departamentos.aplication.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.departamentos.aplication.models.*;
import com.departamentos.aplication.repositories.ItemRepository;

import jakarta.validation.Valid;

@Controller
public class DepartamentosController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/")
    public String lista(@RequestParam(required = false)String nome, Model model){
        List<Item> itens;

        if (nome != null && !nome.isEmpty()){
            itens = itemRepository.findByNomeContainingIgnoreCase(nome);
        }else{
            itens = itemRepository.findAll();
        }

        model.addAttribute("itens", itens);
        model.addAttribute("nome", nome);
        return "departamentos";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid @ModelAttribute Item item,
                            BindingResult result,
                            Model model) {

        if (result.hasErrors()) {
            return "admin";
        }

        Optional<Item> itemExistente = itemRepository.findByNomeIgnoreCase(item.getNome());

        if (itemExistente.isPresent()) {
            model.addAttribute("erro", "Item já cadastrado!");
            return "admin";
        }

        itemRepository.save(item);
        return "redirect:/admincadastro";
    }

    @GetMapping("/editar/{id}")
    public String editarItem(@PathVariable int id, Model model) {
        Item item = itemRepository.findById(id).orElseThrow();
        if(item.getId() == id){
            model.addAttribute("item", item);
            return "admin";
        }
        return "redirect:/admincadastro";
    }

    @GetMapping("/deletar/{id}")
    public String deletarItem(@PathVariable int id){
        itemRepository.deleteById(id);
        return "redirect:/admincadastro";
    }

    @GetMapping("/admincadastro")
    public String admin(Model model){
        List<Item> itens = itemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        model.addAttribute("itens", itens);
        model.addAttribute("item", new Item());
        return "admin";
    }
}