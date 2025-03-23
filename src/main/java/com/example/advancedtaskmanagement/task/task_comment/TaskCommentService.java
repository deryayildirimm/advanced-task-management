package com.example.advancedtaskmanagement.task.task_comment;

import com.example.advancedtaskmanagement.task.Task;
import com.example.advancedtaskmanagement.task.TaskRepository;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskCommentService {

    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskCommentMapper taskCommentMapper;


    public TaskCommentService(TaskCommentRepository taskCommentRepository, TaskRepository taskRepository, UserRepository userRepository, TaskCommentMapper taskCommentMapper) {
        this.taskCommentRepository = taskCommentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskCommentMapper = taskCommentMapper;
    }

    public TaskCommentResponseDto addComment(TaskCommentRequestDto request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskComment comment = TaskComment.builder()
                .task(task)
                .user(user)
                .content(request.getComment())
                .createdAt(new Date())
                .build();

        return taskCommentMapper.toDto(taskCommentRepository.save(comment));
    }

    public List<TaskCommentResponseDto> getByTaskId(Long taskId) {
        return taskCommentRepository.findByTaskIdAndIsDeletedFalse(taskId).stream()
                .map(taskCommentMapper::toDto)
                .collect(Collectors.toList());
    }


    public void deleteComment(Long id) {
        TaskComment comment = taskCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setDeleted(true);
        comment.setDeletedAt(new Date());
        comment.setDeletedBy(getCurrentUserId());
        taskCommentRepository.save(comment);
    }


    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user.getId();
        }
        throw new RuntimeException("User not authenticated");
    }


    public TaskCommentResponseDto updateComment(Long id, TaskCommentRequestDto request) {
        TaskComment comment = taskCommentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(request.getComment());
        return taskCommentMapper.toDto(taskCommentRepository.save(comment));
    }

}
