package ru.practicum.admin.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.categoties.AdminCategoriesService;
import ru.practicum.admin.service.compilations.AdminServiceCompilation;
import ru.practicum.admin.service.events.AdminEventsService;
import ru.practicum.admin.service.users.AdminServiceUsers;
import ru.practicum.dto.Requests.UpdateCompilationRequest;
import ru.practicum.dto.User.NewUserRequest;
import ru.practicum.dto.User.UserDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.mapper.caregory.CategoryMapper;
import ru.practicum.dto.mapper.user.UserMapper;
import ru.practicum.model.EventState;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminController {
    private final AdminCategoriesService categoryService;
    private final CategoryMapper mapperCategory;
    private final AdminEventsService adminEventsService;
    private final AdminServiceUsers userService;
    private final UserMapper userMapper;
    private final AdminServiceCompilation adminServiceCompilation;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto created(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("createCategory(Admin) NewCategoryDto: {}", categoryDto);
        return categoryService.created(mapperCategory.newCategoryDtoToCategory(categoryDto));
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(1) Long catId) {
        log.info("DeleteCategory(Admin) catId: {}", catId);
        categoryService.delete(catId);
    }

    @PatchMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable Long catId,
                              @Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("updateCategory(Admin) catId: {}, NewCategoryDto: {}", catId, categoryDto);
        return categoryService.update(catId, mapperCategory.newCategoryDtoToCategory(categoryDto));
    }

    @GetMapping("/events")
    public List<EventFullDto> findEvents(@RequestParam(name = "users", required = false) List<Long> userIds,
                                         @RequestParam(name = "states", required = false) List<EventState> states,
                                         @RequestParam(name = "categories", required = false) List<Long> categoryIds,
                                         @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(name = "from", defaultValue = "0") int from,
                                         @RequestParam(name = "size", defaultValue = "10") int size
    ) {

        return adminEventsService.findEvents(userIds, states, categoryIds, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return adminEventsService.updateEvent(eventId, updateEventAdminRequest);
    }


    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return userService.createUser(userMapper.newUserRequestToUser(newUserRequest));
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                  @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return adminServiceCompilation.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComp(@PathVariable Long compId) {
        adminServiceCompilation.deleteComp(compId);

    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateComp(@PathVariable Long compId, @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        return adminServiceCompilation.updateComp(compId, updateCompilationRequest);
    }


}