package guru.springframework.services;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImageServiceImplTest {
    @Mock
    RecipeRepository recipeRepository;

    ImageService imageService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    public void saveImageFile() throws IOException {
        //given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("imageFile", "testing.txt",
                "text/plain", "Błażej Knie Guru".getBytes());

        Recipe recipe = new Recipe();
        Long idValue = 1L;
        recipe.setId(idValue);

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        //when
        imageService.saveImageFile(idValue, mockMultipartFile);

        //then
        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(recipeRepository).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(mockMultipartFile.getBytes().length, savedRecipe.getImage().length);
    }

}