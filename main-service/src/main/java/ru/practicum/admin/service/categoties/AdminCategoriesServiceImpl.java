package ru.practicum.admin.service.categoties;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.mapper.caregory.CategoryMapper;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.Category;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto created(Category category) {
        return categoryMapper.categoryToDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto update(Long catId, Category category) {
        Category categoryNew = categoryRepository.findById(catId).orElseThrow(()-> new ObjectNotFoundException(String.format("Category with id=%s was not found", catId)));

        categoryNew.setName(category.getName());
        return categoryMapper.categoryToDTO(categoryNew);
    }
}
