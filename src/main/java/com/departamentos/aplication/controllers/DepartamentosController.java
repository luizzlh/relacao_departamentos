package com.departamentos.aplication.controllers;

import com.departamentos.aplication.models.Item;
import com.departamentos.aplication.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DepartamentosController {
    private ArrayList<Item> problema = new ArrayList<>();

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Item item){
        itemRepository.save(item);
        return "redirect:/admincadastro";
    }

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

    @GetMapping("/admincadastro")
    public String admin(Model model){
        List<Item> itens = itemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        model.addAttribute("itens", itens);
        model.addAttribute("item", new Item());
        return "admin";
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
}