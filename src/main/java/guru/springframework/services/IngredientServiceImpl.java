package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(RecipeRepository recipeRepository, IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        Recipe recipe = optionalRecipe.orElseThrow(() -> {
            log.error("recipe id not found. Id : " + recipeId);
            //todo impl err handling
            return new RuntimeException("recipe id not found. Id : " + recipeId);
        });

        Optional<IngredientCommand> optionalIngredientCommand = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert)
                .findFirst();

        return optionalIngredientCommand.orElseThrow(() -> {
            log.error("ingredient id not found. id : " + ingredientId);
            //todo impl err handling
            return new RuntimeException("ingredient id not found. id : " + ingredientId);
        });
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {
        Recipe recipe = recipeRepository.findById(ingredientCommand.getRecipeId())
                .orElseThrow(() -> {
                    log.error("Recipe for id = %d not exists!");
                    return new RuntimeException(String.format("Recipe for id = %d not exists!",
                            ingredientCommand.getRecipeId()));
                });

        Optional<Ingredient> optionalIngredient = recipe.getIngredients().stream()
                .filter(i -> i.getId().equals(ingredientCommand.getId()))
                .findFirst();

        if (optionalIngredient.isPresent()) {
            Ingredient ingredient = optionalIngredient.get();
            ingredient.setDescription(ingredientCommand.getDescription());
            ingredient.setAmount(ingredientCommand.getAmount());
            ingredient.setUnitOfMeasure(unitOfMeasureRepository.findById(ingredientCommand.getUnitOfMeasure().getId())
                    .orElseThrow(() -> new RuntimeException("UOM not found!")));

        } else {
            recipe.addIngredient(ingredientCommandToIngredient.convert(ingredientCommand));
        }

        Recipe savedRecipe = recipeRepository.save(recipe);

//      todo check for fail
        return ingredientToIngredientCommand.convert(savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst()
                .get());
    }

}
