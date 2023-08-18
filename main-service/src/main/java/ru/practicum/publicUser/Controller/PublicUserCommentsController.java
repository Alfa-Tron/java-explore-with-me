package ru.practicum.publicUser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.publicUser.service.comments.PublicUserCommentsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
@Validated
public class PublicUserCommentsController {
    private final PublicUserCommentsService commentsService;

    @GetMapping("/{eventId}")
    public List<CommentDto> getComments(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                        @RequestParam(name = "size", defaultValue = "10") @Positive int size,
                                        @PathVariable Long eventId) {
        return commentsService.getComments(eventId, from, size);
    }


}
