package com.example.taco_cloud.web;

import com.example.taco_cloud.model.Ingredient;
import com.example.taco_cloud.repository.IngredientRepository;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;
import com.example.taco_cloud.model.Ingredient.Type;

import java.util.HashMap;
import java.util.Map;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    private IngredientRepository ingredientRepo;

    public IngredientByIdConverter(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @Override
    public Ingredient convert(String id) {
        return ingredientRepo.findById(id).orElse(null);
    }

}
