package com.example.advancedtaskmanagement.task;

import com.example.advancedtaskmanagement.common.ErrorMessages;
import com.example.advancedtaskmanagement.common.Messages;
import com.example.advancedtaskmanagement.department.Department;
import com.example.advancedtaskmanagement.exception.BusinessException;
import com.example.advancedtaskmanagement.exception.ResourceNotFoundException;
import com.example.advancedtaskmanagement.project.Project;
import com.example.advancedtaskmanagement.project.ProjectService;
import com.example.advancedtaskmanagement.project.ProjectStatus;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignment;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignmentRepository;
import com.example.advancedtaskmanagement.security.AuthService;
import com.example.advancedtaskmanagement.task.task_progress.TaskProgressService;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private TaskProgressService taskProgressService;
    @Mock private UserRepository userRepository;
    @Mock private ProjectUserAssignmentRepository projectUserAssignmentRepository;
    @Mock private ProjectService projectService;
    @Mock private TaskMapper taskMapper;


    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User user;
    private Project project;
    private TaskRequestDto requestDto;

    @BeforeEach
    public void setUp() {
         user = User.builder().id(1L).name("Test User").build();
        Department department = Department.builder()
                .name("Test Department")
                .build();
        department.setId(1L);

         project = Project.builder()
                .title("Test Project")
                .description("Test Description")
                .department(department)
                .status(ProjectStatus.IN_PROGRESS)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(3))
                .tasks(new ArrayList<>())
                .build();
        project.setId(10L);

         task = Task.builder()
                .acceptanceCriteria("Test Acceptance Criteria")
                .description("Test Task Description")
                .status(TaskStatus.COMPLETED)
                .title("Test Task Title")
                .priority(TaskPriority.MEDIUM)
                .project(project)
                .assignedUser(user)
                .attachments(new ArrayList<>())
                .comments(new ArrayList<>())
                .progressHistory(new ArrayList<>())
                .build();
        task.setId(15L);


    }

    @DisplayName("createTask: should create task and return DTO when input is valid")
    @Test
    void whenCreateTaskWithValidInput_thenReturnTaskResponseDto() {

        TaskRequestDto requestDto = new TaskRequestDto(
                "Test Task",
                TaskPriority.HIGH,
                TaskStatus.IN_ANALYSIS,
                "Some Description Title",
                "Acceptance Criteria",
                user.getId()
        );

        task = Task.builder()
                .title(requestDto.title())
                .description(requestDto.description())
                .acceptanceCriteria(requestDto.acceptanceCriteria())
                .priority(requestDto.priority())
                .status(requestDto.status())
                .assignedUser(user)
                .project(project)
                .build();

        Task savedTask = Task.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .acceptanceCriteria(task.getAcceptanceCriteria())
                .priority(task.getPriority())
                .status(task.getStatus())
                .assignedUser(user)
                .project(project)
                .attachments(new ArrayList<>())
                .build();
        savedTask.setId(100L);


        TaskResponseDto expectedResponse = new TaskResponseDto(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getAcceptanceCriteria(),
                savedTask.getPriority(),
                savedTask.getStatus(),
                user.getName(),
                project.getTitle(),
                List.of()
        );

        Mockito.when(projectService.findById(project.getId())).thenReturn(project);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(projectUserAssignmentRepository.findByProjectIdAndUserId(project.getId(), user.getId()))
                .thenReturn(Optional.of(new ProjectUserAssignment())); // dummy obje, sadece .isPresent() için yeterli
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(savedTask);
        Mockito.when(taskMapper.toDto(Mockito.any(Task.class))).thenReturn(expectedResponse);

        TaskResponseDto actualResponse = taskService.createTask(project.getId(), requestDto);

        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(expectedResponse.title(), actualResponse.title());
        Assertions.assertEquals(expectedResponse.status(), actualResponse.status());
        Mockito.verify(taskProgressService).logProgress(Mockito.any(Task.class), Mockito.eq(TaskStatus.IN_ANALYSIS), Mockito.eq(Messages.TASK_CREATED));
        Mockito.verify(projectUserAssignmentRepository).findByProjectIdAndUserId(project.getId(), user.getId());
        Mockito.verify(taskRepository).save(Mockito.any(Task.class));
        Mockito.verify(taskMapper).toDto(Mockito.any(Task.class));

    }

    @Test
    void whenUserNotAssignedToProject_thenThrowException() {
        TaskRequestDto requestDto = new TaskRequestDto(
                "Test Task",
                TaskPriority.HIGH,
                TaskStatus.IN_ANALYSIS,
                "Some Description",
                "Acceptance Criteria",
                user.getId()
        );

        Mockito.when(projectService.findById(project.getId())).thenReturn(project);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        //  Kullanıcı projeye atanmamış gibi davran
        Mockito.when(projectUserAssignmentRepository.findByProjectIdAndUserId(project.getId(), user.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                taskService.createTask(project.getId(), requestDto)
        );
    }

    @DisplayName("createTask: should throw exception when user not found")
    @Test
    void whenUserNotFound_thenThrowException() {
        // Arrange
        TaskRequestDto requestDto = new TaskRequestDto(
                "Test Task",
                TaskPriority.HIGH,
                TaskStatus.IN_ANALYSIS,
                "Some Description",
                "Acceptance Criteria",
                99L // bilinmeyen bir user ID
        );

        Mockito.when(projectService.findById(project.getId())).thenReturn(project);
        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty()); //  kullanıcı bulunamıyor

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                taskService.createTask(project.getId(), requestDto)
        );
    }

    @DisplayName("createTask: should throw exception when project not found")
    @Test
    void whenProjectNotFound_thenThrowException() {
        // Arrange
        TaskRequestDto requestDto = new TaskRequestDto(
                "Test Task",
                TaskPriority.HIGH,
                TaskStatus.IN_ANALYSIS,
                "Some Description",
                "Acceptance Criteria",
                user.getId()
        );

        Long invalidProjectId = 999L;

        Mockito.when(projectService.findById(invalidProjectId))
                .thenThrow(new ResourceNotFoundException(ErrorMessages.PROJECT_NOT_FOUND)); //  proje yok

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                taskService.createTask(invalidProjectId, requestDto)
        );
    }

    @DisplayName("getAll: should return all active tasks")
    @Test
    void whenGetAllTasks_thenReturnTaskResponseDtoList() {
        // Arrange
        Task task1 = Task.builder()
                .title("Task 1")
                .description("Description 1")
                .acceptanceCriteria("Criteria 1")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.IN_PROGRESS)
                .assignedUser(user)
                .project(project)
                .attachments(new ArrayList<>())
                .build();
        task1.setId(1L);

        Task task2 = Task.builder()
                .title("Task 2")
                .description("Description 2")
                .acceptanceCriteria("Criteria 2")
                .priority(TaskPriority.LOW)
                .status(TaskStatus.IN_ANALYSIS)
                .assignedUser(user)
                .project(project)
                .attachments(new ArrayList<>())
                .build();
        task2.setId(2L);

        List<Task> taskList = List.of(task1, task2);

        TaskResponseDto dto1 = new TaskResponseDto(
                task1.getId(), task1.getTitle(), task1.getDescription(),
                task1.getAcceptanceCriteria(), task1.getPriority(), task1.getStatus(),
                user.getName(), project.getTitle(), List.of()
        );

        TaskResponseDto dto2 = new TaskResponseDto(
                task2.getId(), task2.getTitle(), task2.getDescription(),
                task2.getAcceptanceCriteria(), task2.getPriority(), task2.getStatus(),
                user.getName(), project.getTitle(), List.of()
        );

        Mockito.when(taskRepository.findByIsDeletedFalse()).thenReturn(taskList);
        Mockito.when(taskMapper.toDto(task1)).thenReturn(dto1);
        Mockito.when(taskMapper.toDto(task2)).thenReturn(dto2);

        // Act
        List<TaskResponseDto> result = taskService.getAll();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Task 1", result.get(0).title());
        Assertions.assertEquals("Task 2", result.get(1).title());
    }

    @DisplayName("getByProjectId: should return tasks for given project id")
    @Test
    void whenGetTasksByProjectId_thenReturnTaskResponseDtoList() {
        // Arrange
        Task task1 = Task.builder()
                .title("Project Task 1")
                .description("Description 1")
                .acceptanceCriteria("Criteria 1")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.IN_PROGRESS)
                .assignedUser(user)
                .project(project)
                .attachments(new ArrayList<>())
                .build();
        task1.setId(1L);

        List<Task> taskList = List.of(task1);

        TaskResponseDto dto1 = new TaskResponseDto(
                task1.getId(), task1.getTitle(), task1.getDescription(),
                task1.getAcceptanceCriteria(), task1.getPriority(), task1.getStatus(),
                user.getName(), project.getTitle(), List.of()
        );

        Mockito.when(taskRepository.findByProjectIdAndIsDeletedFalse(project.getId())).thenReturn(taskList);
        Mockito.when(taskMapper.toDto(task1)).thenReturn(dto1);

        // Act
        List<TaskResponseDto> result = taskService.getByProjectId(project.getId());

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Project Task 1", result.get(0).title());
    }

    @DisplayName("getByAssigneeId: should return tasks assigned to given user")
    @Test
    void whenGetTasksByAssigneeId_thenReturnTaskResponseDtoList() {
        // Arrange
        Task task1 = Task.builder()
                .title("Assigned Task")
                .description("Desc")
                .acceptanceCriteria("Accept")
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.IN_PROGRESS)
                .assignedUser(user)
                .project(project)
                .attachments(new ArrayList<>())
                .build();
        task1.setId(2L);

        List<Task> taskList = List.of(task1);

        TaskResponseDto dto1 = new TaskResponseDto(
                task1.getId(), task1.getTitle(), task1.getDescription(),
                task1.getAcceptanceCriteria(), task1.getPriority(), task1.getStatus(),
                user.getName(), project.getTitle(), List.of()
        );

        Mockito.when(taskRepository.findByAssignedUserIdAndIsDeletedFalse(user.getId())).thenReturn(taskList);
        Mockito.when(taskMapper.toDto(task1)).thenReturn(dto1);

        // Act
        List<TaskResponseDto> result = taskService.getByAssigneeId(user.getId());

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Assigned Task", result.get(0).title());
    }

    @DisplayName("updateTaskStatus: should update task status successfully")
    @Test
    void whenValidStatusUpdate_thenReturnUpdatedTaskDto() {
        TaskStatusRequest request = new TaskStatusRequest(TaskStatus.IN_PROGRESS, "Progress started");
        task.setStatus(TaskStatus.IN_ANALYSIS); // mevcut durum

        Task updatedTask = Task.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .acceptanceCriteria(task.getAcceptanceCriteria())
                .priority(task.getPriority())
                .status(TaskStatus.IN_PROGRESS)
                .assignedUser(task.getAssignedUser())
                .project(task.getProject())
                .attachments(task.getAttachments())
                .build();
        updatedTask.setId(task.getId());

        TaskResponseDto expectedDto = new TaskResponseDto(
                updatedTask.getId(), updatedTask.getTitle(), updatedTask.getDescription(),
                updatedTask.getAcceptanceCriteria(), updatedTask.getPriority(), updatedTask.getStatus(),
                user.getName(), project.getTitle(), List.of()
        );

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(updatedTask);
        Mockito.when(taskMapper.toDto(updatedTask)).thenReturn(expectedDto);

        TaskResponseDto result = taskService.updateTaskStatus(task.getId(), request);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, result.status());
        Mockito.verify(taskProgressService).logProgress(Mockito.eq(task), Mockito.eq(TaskStatus.IN_PROGRESS), Mockito.eq("Progress started"));
    }

    @DisplayName("updateTaskStatus: should throw exception when status is same as current")
    @Test
    void whenStatusIsSameAsCurrent_thenThrowBusinessException() {

        task.setStatus(TaskStatus.IN_ANALYSIS);

        TaskStatusRequest request = new TaskStatusRequest(TaskStatus.IN_ANALYSIS, "Trying to set same status");

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () ->
                taskService.updateTaskStatus(task.getId(), request)
        );

        Assertions.assertEquals(ErrorMessages.STATUS_SAME_AS_CURRENT, exception.getMessage());
    }

    @DisplayName("updateTaskStatus: should throw exception when status is COMPLETED")
    @Test
    void whenStatusIsCompleted_thenThrowBusinessException() {
        task.setStatus(TaskStatus.COMPLETED); // Zaten tamamlanmış
        TaskStatusRequest request = new TaskStatusRequest(TaskStatus.BLOCKED, "Blocked after completion");

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () ->
                taskService.updateTaskStatus(task.getId(), request)
        );

        Assertions.assertEquals(ErrorMessages.STATUS_ALREADY_COMPLETED, exception.getMessage());
    }

    @DisplayName("updateTaskStatus: should throw exception on invalid status transition")
    @Test
    void whenInvalidStatusTransition_thenThrowBusinessException() {
        task.setStatus(TaskStatus.BLOCKED); // Geçiş yapılamaz durum
        TaskStatusRequest request = new TaskStatusRequest(TaskStatus.CANCELLED, "Cancelling after blocked");

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () ->
                taskService.updateTaskStatus(task.getId(), request)
        );

        Assertions.assertEquals(ErrorMessages.STATUS_TRANSITION_INVALID, exception.getMessage());
    }

    @DisplayName("updateTaskStatus: should throw exception when CANCELLED or BLOCKED with empty reason")
    @Test
    void whenCancelledOrBlockedWithoutReason_thenThrowBusinessException() {
        task.setStatus(TaskStatus.IN_PROGRESS); // Geçerli bir geçiş gibi duruyor ama reason yok
        TaskStatusRequest request = new TaskStatusRequest(TaskStatus.CANCELLED, "");

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        BusinessException exception = Assertions.assertThrows(BusinessException.class, () ->
                taskService.updateTaskStatus(task.getId(), request)
        );

        Assertions.assertEquals(ErrorMessages.STATUS_REASON_REQUIRED, exception.getMessage());
    }


    @Test
    void whenValidId_thenReturnTaskResponseDto() {

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        TaskResponseDto expectedDto = new TaskResponseDto(
                1L, "Test Task", "Test Desc", "Criteria",
                TaskPriority.HIGH, TaskStatus.IN_PROGRESS,
                "Test User", "Test Project", List.of()
        );
        Mockito.when(taskMapper.toDto(task)).thenReturn(expectedDto);

        // Act
        TaskResponseDto actualDto = taskService.getById(task.getId());

        // Assert
        Assertions.assertEquals(expectedDto.title(), actualDto.title());
        Assertions.assertEquals(expectedDto.status(), actualDto.status());
        Assertions.assertEquals(expectedDto.assignedUserName(), actualDto.assignedUserName());


    }

    @Test
    void whenInvalidId_thenReturnTaskResponseDto() {

        // Arrange
        Long invalidId = 99L;
        Mockito.when(taskRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> taskService.getById(invalidId));
    }

    @Test
    void whenUpdateTaskWithValidInput_thenReturnUpdatedTaskResponseDto() {

        TaskRequestDto requestDto = new TaskRequestDto(
                "Updated Task",
                TaskPriority.MEDIUM,
                TaskStatus.IN_PROGRESS,
                "Updated Description",
                "Updated Criteria",
                user.getId()
        );

        Task updatedTask = Task.builder()
                .title(requestDto.title())
                .description(requestDto.description())
                .priority(requestDto.priority())
                .acceptanceCriteria(requestDto.acceptanceCriteria())
                .status(task.getStatus())
                .assignedUser(user)
                .project(project)
                .build();
        updatedTask.setId(task.getId());

        TaskResponseDto expectedDto = new TaskResponseDto(
                task.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getAcceptanceCriteria(),
                updatedTask.getPriority(),
                updatedTask.getStatus(),
                user.getName(),
                project.getTitle(),
                List.of()
        );

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(updatedTask);
        Mockito.when(taskMapper.toDto(Mockito.any(Task.class))).thenReturn(expectedDto);

        TaskResponseDto actualDto = taskService.updateTask(task.getId(), requestDto);

        Assertions.assertEquals(expectedDto.title(), actualDto.title());
        Assertions.assertEquals(expectedDto.description(), actualDto.description());
        Assertions.assertEquals(expectedDto.priority(), actualDto.priority());
        Assertions.assertEquals(expectedDto.acceptanceCriteria(), actualDto.acceptanceCriteria());

        Mockito.verify(taskRepository).save(Mockito.any(Task.class));
        Mockito.verify(taskMapper).toDto(updatedTask);
    }

    @Test
    void whenAssignedUserNotFound_thenThrowResourceNotFoundException() {
        Long taskId = task.getId();
        Long invalidUserId = 99L;

        TaskRequestDto requestDto = new TaskRequestDto(
                "Test",
                TaskPriority.HIGH,
                TaskStatus.IN_PROGRESS,
                "Desc",
                "Criteria",
                invalidUserId
        );


        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.updateTask(taskId, requestDto));

        Assertions.assertEquals(ErrorMessages.USER_NOT_FOUND, ex.getMessage());
    }

    @Test
    void whenAssignedUserIsNotInProject_thenThrowBusinessException() {
        Long taskId = task.getId();
        Long newUserId = 2L;

        User newUser = User.builder().id(newUserId).name("Another User").build();

        TaskRequestDto requestDto = new TaskRequestDto(
                "Test",
                TaskPriority.HIGH,
                TaskStatus.IN_PROGRESS,
                "Desc",
                "Criteria",
                newUserId
        );

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(userRepository.findById(newUserId)).thenReturn(Optional.of(newUser));
        Mockito.when(projectUserAssignmentRepository.findByProjectIdAndUserId(project.getId(), newUserId))
                .thenReturn(Optional.empty());

        BusinessException ex = Assertions.assertThrows(BusinessException.class,
                () -> taskService.updateTask(taskId, requestDto));

        Assertions.assertEquals(ErrorMessages.USER_NOT_ASSIGNED, ex.getMessage());
    }


    @Test
    void whenDeleteTask_thenSoftDeleteIsCalled() {
        Long taskId = task.getId();
        User mockUser = User.builder().id(99L).username("Mock Auth User").build();

        // 🔥 SecurityContext içine mock kullanıcıyı ekle
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(mockUser, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        taskService.delete(taskId);

        Assertions.assertTrue(task.isDeleted());
        Assertions.assertNotNull(task.getDeletedAt());
        Mockito.verify(taskRepository).save(task);

        SecurityContextHolder.clearContext();
    }

    @Test
    void whenAssignedUserChanged_thenSetAssignedUserCalled() {
        Long newUserId = 99L;
        User newUser = User.builder().id(newUserId).name("New User").build();

        TaskRequestDto requestDto = new TaskRequestDto(
                "Task",
                TaskPriority.HIGH,
                TaskStatus.IN_ANALYSIS,
                "Desc",
                "Criteria",
                newUserId
        );

        task.setAssignedUser(user); // eski user farklı
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Mockito.when(userRepository.findById(newUserId)).thenReturn(Optional.of(newUser));
        Mockito.when(projectUserAssignmentRepository.findByProjectIdAndUserId(project.getId(), newUserId))
                .thenReturn(Optional.of(new ProjectUserAssignment()));

        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenAnswer(i -> i.getArgument(0));
        Mockito.when(taskMapper.toDto(Mockito.any(Task.class)))
                .thenAnswer(i -> {
                    Task saved = i.getArgument(0);
                    return new TaskResponseDto(
                            saved.getId(),
                            saved.getTitle(),
                            saved.getDescription(),
                            saved.getAcceptanceCriteria(),
                            saved.getPriority(),
                            saved.getStatus(),
                            saved.getAssignedUser().getName(),
                            saved.getProject().getTitle(),
                            List.of()
                    );
                });

        TaskResponseDto response = taskService.updateTask(task.getId(), requestDto);

        Assertions.assertEquals("New User", response.assignedUserName());
        Assertions.assertEquals(newUserId, task.getAssignedUser().getId()); // kontrol

        Mockito.verify(userRepository).findById(newUserId);
        Mockito.verify(projectUserAssignmentRepository).findByProjectIdAndUserId(project.getId(), newUserId);
        Mockito.verify(taskRepository).save(Mockito.any(Task.class));
    }







}
