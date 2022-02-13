package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class ImageController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("/recipe/{recipeId}/image")
    public String getImageForm(@PathVariable String recipeId, Model model) {

        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

        return "recipe/imageuploadform";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String saveRecipeImage(@PathVariable String recipeId, @RequestParam("imagefile") MultipartFile file) {
        imageService.saveImageFile(Long.valueOf(recipeId), file);
        return "redirect:/recipe/" + recipeId + "/show";
    }

    @GetMapping("/recipe/{recipeId}/recipeimage")
    public void renderImageFromDB(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand command = recipeService.findCommandById(Long.valueOf(recipeId));

        byte[] bytesArray = new byte[command.getImage().length];

        int i = 0;
        for (Byte b : command.getImage()) {
            bytesArray[i++] = b;
        }

        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(bytesArray);
        IOUtils.copy(is, response.getOutputStream());
    }

}
