package ru.practicum.publicUser.service.categories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.CategoryRepository;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.mapper.caregory.CategoryMapper;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicUserCatServiceImpl implements PublicUserCatService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);

        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::categoryToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getOneCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new ObjectNotFoundException(String.format("Category with id=%s was not found", catId)));

        return categoryMapper.categoryToDTO(category);
    }
}
