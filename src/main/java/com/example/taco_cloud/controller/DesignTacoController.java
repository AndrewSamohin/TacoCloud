package com.example.taco_cloud.controller;

import com.example.taco_cloud.orders.TacoOrder;
import com.example.taco_cloud.model.Ingredient;
import com.example.taco_cloud.model.Taco;
import com.example.taco_cloud.model.Ingredient.Type;
import com.example.taco_cloud.repository.IngredientRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        Iterable<Ingredient> ingredients = ingredientRepo.findAll();
        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }
    
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }

    @PostMapping
    public String processTaco(@Valid Taco taco, Errors errors,
                              @RequestParam("ingredients") List<String> ingredientIds,
                              @ModelAttribute TacoOrder tacoOrder) {
        // Получаем все доступные ингредиенты
        List<Ingredient> allIngredients = getAllIngredients();

        // Преобразуем идентификаторы в объекты Ingredient
        List<Ingredient> selectedIngredients = allIngredients.stream()
                .filter(ingredient -> ingredientIds.contains(ingredient.getId()))
                .collect(Collectors.toList());

        // Устанавливаем список ингредиентов в объект Taco
        taco.setIngredients(selectedIngredients);

        if (errors.hasErrors()) {
            return "design";
        }
        // Добавляем тако в заказ
        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);

        // Перенаправляем на страницу текущего заказа
        return "redirect:/orders/current";
    }

    private List<Ingredient> getAllIngredients() {
        return Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );
    }

    private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type) {
        List<Ingredient> filtered = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getType().equals(type)) {
                filtered.add(ingredient);
            }
        }
        return filtered;
    }
}