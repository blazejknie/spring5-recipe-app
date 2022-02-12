package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientToIngredientCommand ingredientToIngredientCommand;

    IngredientCommandToIngredient ingredientCommandToIngredient;

    IngredientService ingredientService;

    public IngredientServiceImplTest() {
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ingredientService = new IngredientServiceImpl(recipeRepository, ingredientToIngredientCommand, ingredientCommandToIngredient, unitOfMeasureRepository);
    }

    @Test
    public void testFindByRecipeIdAndIngredientIdHappyPath() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(1L);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(3L);

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        Optional<Recipe> optionalRecipe = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);

        //when
        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);

        //then
        assertEquals(Long.valueOf(1), ingredientCommand.getRecipeId());
        assertEquals(Long.valueOf(3), ingredientCommand.getId());

        verify(recipeRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testSaveIngredientCommand() {
        //given
        IngredientCommand command = new IngredientCommand();
        command.setId(2L);
        command.setRecipeId(3L);

        Optional<Recipe> optionalRecipe = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId(2L);

        when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        //when
        IngredientCommand savedCommand = ingredientService
                .saveIngredientCommand(command);

        //then
        assertEquals(Long.valueOf(2), savedCommand.getId());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository).save(any());

    }
}