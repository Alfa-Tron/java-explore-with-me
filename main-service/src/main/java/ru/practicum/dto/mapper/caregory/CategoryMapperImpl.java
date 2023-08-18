package ru.practicum.dto.mapper.caregory;

import org.springframework.stereotype.Component;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.Category;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto) {
        return new Category(newCategoryDto.getName());
    }

    @Override
    public CategoryDto categoryToDTO(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
