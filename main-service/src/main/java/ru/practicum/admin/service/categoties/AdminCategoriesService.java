package ru.practicum.admin.service.categoties;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.model.Category;

public interface AdminCategoriesService {
    CategoryDto created(Category category);

    void delete(Long catId);

    CategoryDto update(Long catId, Category category);
}
