package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.*;
import guru.springframework.domain.Recipe;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
@RunWith(SpringRunner.class)

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);

    }

    @Test
    public void getRecipes() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipesData);

        Set<Recipe> returnedRecipes = recipeService.getRecipes();

        assertEquals(1, returnedRecipes.size());
        verify(recipeRepository).findAll();
        verify(recipeRepository, never()).findById(anyLong());
    }

    @Test
    public void getRecipeById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> optionalRecipe = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);


        Recipe recipeById = recipeService.getRecipeById(1L);
        assertNotNull("Null recipe returned", recipeById);
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository, never()).findAll();

    }

    @Test(expected = NotFoundException.class)
    public void testGetRecipeById() {

        Optional<Recipe> optionalRecipe = Optional.empty();

        when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);

        Recipe recipeReturned = recipeService.getRecipeById(1L);
    }

    @Test
    public void testGetRecipeCommandById() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        //when
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        //then
        RecipeCommand commandById = recipeService.findCommandById(1L);

        assertNotNull("Null recipe returned!", commandById);
        assertEquals(recipe.getId(), commandById.getId());
        verify(recipeRepository).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void testDeleteById() {
        //given
        Long idValue = 2L;

        //when
        recipeService.deleteById(idValue);

        //then
        verify(recipeRepository).deleteById(anyLong());
    }

}