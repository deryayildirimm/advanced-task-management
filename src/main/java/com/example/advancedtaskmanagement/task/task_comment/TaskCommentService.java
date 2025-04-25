package com.example.advancedtaskmanagement.task.task_comment;

import com.example.advancedtaskmanagement.common.ErrorMessages;
import com.example.advancedtaskmanagement.exception.ResourceNotFoundException;
import com.example.advancedtaskmanagement.exception.UserNotAuthenticatedException;
import com.example.advancedtaskmanagement.task.Task;
import com.example.advancedtaskmanagement.task.TaskService;
import com.example.advancedtaskmanagement.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskCommentService  {

    private final TaskCommentRepository taskCommentRepository;
    private final TaskService taskService;


    public TaskCommentService(
            TaskCommentRepository taskCommentRepository,
            TaskService taskService) {
        this.taskCommentRepository = taskCommentRepository;
        this.taskService = taskService;
    }

    protected TaskComment findTaskByCommentId(Long id){
        return taskCommentRepository.findByTaskId(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public TaskCommentResponseDto addComment(TaskCommentRequestDto request) {
        Task task = taskService.findById(request.taskId());

       User currentUser = getCurrentAuthenticatedUser();

        TaskComment comment = TaskComment.builder()
                .task(task)
                .user(currentUser)
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .build();

        TaskComment savedComment = taskCommentRepository.save(comment);

        return new TaskCommentResponseDto(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getUser().getName()
        );
    }

    private User getCurrentAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }
      throw new UserNotAuthenticatedException(ErrorMessages.USER_NOT_AUTHENTICATED);

    }

    public List<TaskCommentResponseDto> getCommentsByTaskId(Long taskId) {
        return taskCommentRepository.findByTaskId(taskId).stream()
                .map(comment -> new TaskCommentResponseDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUser().getName()
                ))
                .toList();
    }


    public void deleteComment(Long id) {
        TaskComment comment = taskCommentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.TASK_COMMENT_NOT_FOUND));

        taskCommentRepository.delete(comment);
    }


    public TaskCommentResponseDto updateComment(Long id, TaskCommentRequestDto request) {
        TaskComment comment = taskCommentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.TASK_COMMENT_NOT_FOUND));

        comment.setContent(request.content());
        TaskComment savedComment = taskCommentRepository.save(comment);

        return new TaskCommentResponseDto(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getUser().getName()
        );
    }

}
