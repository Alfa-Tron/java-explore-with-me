package ru.practicum.privateUser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentCreateDto;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.privateUser.service.comment.PrivateCommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
@Validated
public class PrivateCommentController {
    private final PrivateCommentService privateCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createComment(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @RequestBody @Valid CommentCreateDto requestCreateDto) {
        return privateCommentService.createComment(userId, eventId, requestCreateDto);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto patchComment(@PathVariable Long userId,
                                       @PathVariable Long eventId,
                                       @PathVariable Long commentId,
                                       @RequestBody @Valid CommentCreateDto commentCreateDto) {

        return privateCommentService.patchComment(userId, eventId, commentId, commentCreateDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long eventId,
                              @PathVariable Long commentId) {
        privateCommentService.deleteComment(userId, eventId, commentId);


    }

    @GetMapping("/{commentId}")
    public CommentFullDto getComment(@PathVariable Long userId,
                                     @PathVariable Long eventId,
                                     @PathVariable Long commentId) {

        return privateCommentService.getComment(userId, eventId, commentId);

    }

    @GetMapping
    public List<CommentFullDto> getCommentsByEvent(@PathVariable Long userId,
                                                   @PathVariable Long eventId,
                                                   @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(name = "size", defaultValue = "10") @Positive int size
    ) {

        return privateCommentService.getCommentsByEvent(userId, eventId, from, size);


    }


}
