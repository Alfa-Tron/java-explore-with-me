package ru.practicum.publicUser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.publicUser.service.categories.PublicUserCatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/categories")
public class PublicUserCategoriesController {
    private final PublicUserCatService publicUserService;


    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return publicUserService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getOneCategory(@PathVariable Long catId) {
        return publicUserService.getOneCategory(catId);
    }

}
