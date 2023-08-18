package ru.practicum.admin.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.comments.AdminCommentsService;
import ru.practicum.dto.comment.CommentFullDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Validated
public class AdminCommentsController {
    private final AdminCommentsService adminCommentsService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        adminCommentsService.deleteComment(commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentFullDto> findAll(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {

        return adminCommentsService.findAll(users, from, size);
    }
}
