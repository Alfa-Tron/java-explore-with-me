package ru.practicum.publicUser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.publicUser.service.categories.PublicUserCatService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@Validated
public class PublicUserCategoriesController {
    private final PublicUserCatService publicUserService;


    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from, @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        return publicUserService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getOneCategory(@PathVariable Long catId) {
        return publicUserService.getOneCategory(catId);
    }

}
