package ru.practicum.publicUser.service.categories;

import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface PublicUserCatService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getOneCategory(Long catId);
}
