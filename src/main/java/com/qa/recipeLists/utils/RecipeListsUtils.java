package com.qa.recipelists.utils;

//--[ Imports ]---
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.qa.recipelists.dto.IngredientDTO;
import com.qa.recipelists.dto.RecipeDTO;
import com.qa.recipelists.dto.StepDTO;
import com.qa.recipelists.persistence.domain.Ingredient;
import com.qa.recipelists.persistence.domain.Recipe;
import com.qa.recipelists.persistence.domain.Step;

//---[ RecipeLists Utilities ]---
public class RecipeListsUtils {
	private static ModelMapper mapper;
	
	public static void mergeNotNull(Object source, Object target) {
		BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
	}
	
	private static String[] getNullPropertyNames(Object source) {
		final BeanWrapper wrappedSourceObject = new BeanWrapperImpl(source);
		
		Set<String> propertyNames = new HashSet<>();
		for (PropertyDescriptor propertyDescriptors : wrappedSourceObject.getPropertyDescriptors()) {
            if (wrappedSourceObject.getPropertyValue(propertyDescriptors.getName()) == null)
                propertyNames.add(propertyDescriptors.getName());
        }
        return propertyNames.toArray(new String[propertyNames.size()]);
	}
	//===[ INGREDIENT MAPPING ]===
	public static Ingredient mapIngredient(IngredientDTO dto, Recipe recipe) {
		Ingredient toReturn = new Ingredient();
		toReturn.setId(dto.getId());
		toReturn.setName(dto.getName());
		toReturn.setUnit(dto.getUnit());
		toReturn.setQuantity(dto.getQuantity());
		toReturn.setRecipe(recipe);
		return toReturn;
	}
	
	//===[ STEP MAPPING ]===
	public static Step mapStep(StepDTO dto, Recipe recipe) {
		Step toReturn = new Step();
		toReturn.setId(dto.getId());
		toReturn.setName(dto.getName());
		toReturn.setDescription(dto.getDescription());
		toReturn.setRecipe(recipe);
		return toReturn;
	}
	
	//===[ RECIPE MAPPING ]===
	public static Recipe mapRecipeFromDTO(RecipeDTO dto, Recipe toUpdate) {
		// Update Name
		toUpdate.setName(dto.getName());
		
		// Loop Through IngredientDTO's and convert to Ingredient's
		List<Ingredient> ingredients = new ArrayList<>();
		for(IngredientDTO i : dto.getIngredients()) {
			ingredients.add(mapIngredient(i, toUpdate));
		}
		
		List<Step> steps = new ArrayList<>();
		for(StepDTO s : dto.getSteps()) {
			steps.add(mapStep(s, toUpdate));
		}
		
		toUpdate.setIngredients(ingredients);
		toUpdate.setSteps(steps);
		
		return toUpdate;
	}
	
	public static RecipeDTO mapRecipeToDTO(Recipe recipe) {
		List<IngredientDTO> ingredients = new ArrayList<>();
		for(Ingredient i : recipe.getIngredients()) {
			ingredients.add(mapper.map(i, IngredientDTO.class));
		}
		
		List<StepDTO> steps = new ArrayList<>();
		for(Step s : recipe.getSteps()) {
			steps.add(mapper.map(s, StepDTO.class));
		}
		
		return new RecipeDTO(
				recipe.getId(), 
				recipe.getName(), 
				ingredients, steps);
	}
}
