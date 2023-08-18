package ru.practicum.admin.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.categoties.AdminCategoriesService;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.mapper.caregory.CategoryMapper;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCategoriesController {
    private final AdminCategoriesService categoryService;
    private final CategoryMapper mapperCategory;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto created(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("createCategory(Admin) NewCategoryDto: {}", categoryDto);
        return categoryService.created(mapperCategory.newCategoryDtoToCategory(categoryDto));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(1) Long catId) {
        log.info("DeleteCategory(Admin) catId: {}", catId);
        categoryService.delete(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable Long catId,
                              @Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("updateCategory(Admin) catId: {}, NewCategoryDto: {}", catId, categoryDto);
        return categoryService.update(catId, mapperCategory.newCategoryDtoToCategory(categoryDto));
    }
}
