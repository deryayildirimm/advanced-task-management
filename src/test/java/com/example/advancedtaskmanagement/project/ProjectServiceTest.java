package com.example.advancedtaskmanagement.project;

import com.example.advancedtaskmanagement.common.ErrorMessages;
import com.example.advancedtaskmanagement.department.Department;
import com.example.advancedtaskmanagement.department.DepartmentService;
import com.example.advancedtaskmanagement.exception.ResourceNotFoundException;
import com.example.advancedtaskmanagement.exception.UserAlreadyAssignedException;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignment;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignmentRepository;
import com.example.advancedtaskmanagement.project.project_user_assignment.ProjectUserAssignmentRequestDto;
import com.example.advancedtaskmanagement.user.Role;
import com.example.advancedtaskmanagement.user.User;
import com.example.advancedtaskmanagement.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @BeforeEach
    void setUp() {

        User fakeUser = new User();
        fakeUser.setId(1L); // Test için örnek kullanıcı ID’si
        fakeUser.setName("testUser");
        fakeUser.setPassword("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(fakeUser, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Mock
    DepartmentService departmentService;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    ProjectMapper projectMapper;

    @Mock
    UserService userService;

    @Mock
    ProjectUserAssignmentRepository projectUserAssignmentRepository;

    @InjectMocks
    ProjectService projectService;


    @Test
    void shouldThrowExceptionWhenStatusIsCancelledOrCompleted() {

        ProjectRequestDto requestDto = new ProjectRequestDto(
                "Test Project",
                "Cancelled or Completed",
                LocalDate.of(2024,11,12),
                LocalDate.of(2025,1,12),
                ProjectStatus.CANCELLED,
                1L
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(requestDto);
        });

        assertEquals(ErrorMessages.PROJECT_STATUS_CANNOT_BE_CANCELLED_OR_COMPLETED_ON_CREATE, exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenStartDateBeforeEndDate() {
        ProjectRequestDto requestDto = new ProjectRequestDto(
                "Test Project",
                "Start&End date comparison",
                LocalDate.of(2025,1,11),
                LocalDate.of(2024,1,11),
                ProjectStatus.IN_PROGRESS,
                1L

        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(requestDto);
        });

        assertEquals(ErrorMessages.START_DATE_BEFORE_END_DATE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDepartmentIsNotFound() {
        ProjectRequestDto requestDto = new ProjectRequestDto(
                "Test Project",
                "Check the department",
                LocalDate.of(2025,1,11),
                LocalDate.of(2025,1,16),
                ProjectStatus.IN_PROGRESS,
                99L
        );

        when(departmentService.findDepartmentById(99L)).thenThrow(
         new ResourceNotFoundException(ErrorMessages.DEPARTMENT_NOT_FOUND)
        );

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            projectService.createProject(requestDto);
        });

        assertEquals(ErrorMessages.DEPARTMENT_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldCreateProjectSuccessfully() {

        ProjectRequestDto requestDto = new ProjectRequestDto(
                "Test Project",
                "Create successfully ",
                LocalDate.of(2025,1,12),
                LocalDate.of(2025,3,20),
                ProjectStatus.IN_PROGRESS,
                1L
        );

        Department department = new Department();
        Project project = Project.builder()
                .title(requestDto.title())
                .department(department)
                .startDate(requestDto.startDate())
                .endDate(requestDto.endDate())
                .status(requestDto.status())
                .build();

        ProjectResponseDto responseDto = new ProjectResponseDto(
                1L,
                requestDto.title(),
                requestDto.description(),
                requestDto.startDate(),
                requestDto.endDate(),
                requestDto.status(),
                "IT DEPARTMENT"
        );

        when(departmentService.findDepartmentById(1L)).thenReturn(department);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toResponseDto(any(Project.class))).thenReturn(responseDto);

        ProjectResponseDto projectResponseDto = projectService.createProject(requestDto);

        assertNotNull(projectResponseDto);
        assertEquals(responseDto, projectResponseDto);
        assertEquals("IT DEPARTMENT" , projectResponseDto.departmentName());

        verify(departmentService).findDepartmentById(1L);
        verify(projectRepository).save(any(Project.class));
        verify(projectMapper).toResponseDto(any(Project.class));

        }

    @Test
    void shouldReturnAllProjects() {

        Department department = new Department();

        Project newProject1 = Project.builder()
                .title("Test Project")
                .description("Test Description")
                .startDate(LocalDate.of(2025,1,12))
                .endDate(LocalDate.of(2025,1,16))
                .status(ProjectStatus.IN_PROGRESS)
                .department(department)
                .build();

        Project newProject2 = Project.builder()
                .title("Test Project2")
                .description("Test Description2")
                .startDate(LocalDate.of(2025,1,13))
                .endDate(LocalDate.of(2025,2,16))
                .status(ProjectStatus.IN_PROGRESS)
                .department(department)
                .build();

        List<Project> projects = Arrays.asList(newProject1, newProject2);

        ProjectResponseDto responseDto = new ProjectResponseDto(
                4L,
                "Test Project",
                "Test Description",
                LocalDate.of(2025,1,12),
                LocalDate.of(2025,1,16),
                ProjectStatus.IN_PROGRESS,
                department.getName()
        );
        ProjectResponseDto responseDto2 = new ProjectResponseDto(
                6L,
                "Test Project2",
                "Test Description2",
                LocalDate.of(2025,1,13),
                LocalDate.of(2025,1,16),
                ProjectStatus.IN_PROGRESS,
                department.getName()
        );

        when(projectRepository.findByIsDeletedFalse()).thenReturn(projects);
        when(projectMapper.toResponseDto(newProject1)).thenReturn(responseDto);
        when(projectMapper.toResponseDto(newProject2)).thenReturn(responseDto2);

        List<ProjectResponseDto> projectResponseDtoList = projectService.getAllProjects();

        assertEquals(2, projectResponseDtoList.size());
        assertEquals("Test Project", projectResponseDtoList.getFirst().title());
        assertEquals("Test Description", projectResponseDtoList.getFirst().description());
        assertEquals("Test Description2", projectResponseDtoList.get(1).description());
        assertEquals("Test Project2", projectResponseDtoList.get(1).title());
        assertEquals(responseDto2, projectResponseDtoList.get(1));

        verify(projectRepository).findByIsDeletedFalse();
        verify(projectMapper, times(2)).toResponseDto(any(Project.class));


    }

    @Test
    void shouldReturnProjectById() {

        Long projectId = 1L;

        Department department = Department.builder()
                .name("Test Department")
                .build();

        Project project = Project.builder()
                .title("Test Project")
                .description("Test Description")
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 1, 10))
                .status(ProjectStatus.IN_PROGRESS)
                .department(department)
                .build();

        project.setId(projectId);

        ProjectResponseDto responseDto = new ProjectResponseDto(
                projectId,
                "Test Project",
                "Test Description",
                project.getStartDate(),
                project.getEndDate(),
                ProjectStatus.IN_PROGRESS,
                "Test Department"
        );
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectMapper.toResponseDto(project)).thenReturn(responseDto);

        ProjectResponseDto result = projectService.getProjectById(projectId);

        assertNotNull(result);
        assertEquals("Test Project", result.title());
        assertEquals("Test Department", result.departmentName());

        verify(projectRepository).findById(projectId);
        verify(projectMapper).toResponseDto(project);

    }

    @Test
    void updateProject() {
    }

    @Test
    void getTasksByProject() {
    }

    @Test
    void shouldSoftDeleteProject() {

        Department department = Department.builder()
                .name("Test Department")
                .build();

        Project project = Project.builder()
                .title("Test Project")
                .description("Test Description")
                .startDate(LocalDate.of(2025,1,12))
                .endDate(LocalDate.of(2025,1,16))
                .status(ProjectStatus.IN_PROGRESS)
                .department(department)
                .build();

        project.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.deleteProject(project.getId());

        assertTrue(project.isDeleted());
        assertNotNull(project.getDeletedAt());
        assertNotNull(project.getDeletedBy());
        assertEquals(1L, project.getDeletedBy());

        //Bu kısımda, gerçekten belirttiğin metodlar çağrıldı mı onu kontrol ediyorsun.
        verify(projectRepository).findById(1L);
        verify(projectRepository).save(project);

    }

    @Test
    void shouldAssignUserToProject() {

        Long projectId = 1L;
        Long userId = 2L;

        Department department = Department.builder()
                .name("Test Department")
                .build();

        Project project = Project.builder()
                .title("Test Project")
                .description("Test Description")
                .startDate(LocalDate.of(2025,1,12))
                .endDate(LocalDate.of(2025,1,16))
                .status(ProjectStatus.IN_PROGRESS)
                .department(department)
                .build();
        project.setId(projectId);

        User user = User.builder()
                .id(userId)
                .name("Test User")
                .username("Test User")
                .password("Test Password")
                .build();

        ProjectUserAssignmentRequestDto requestDto = new ProjectUserAssignmentRequestDto(projectId,userId,Role.ROLE_TEAM_MEMBER);

        ProjectUserAssignment projectUserAssignment = ProjectUserAssignment.builder()
                .user(user)
                .role(Role.ROLE_TEAM_MEMBER)
                .project(project)
                .assignedAt(LocalDate.now())
                .build();

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userService.findByUserId(userId)).thenReturn(user);
        when(projectUserAssignmentRepository.findByProjectIdAndUserId(projectId,userId)).thenReturn(Optional.empty());
        when(projectUserAssignmentRepository.save(any(ProjectUserAssignment.class))).thenReturn(projectUserAssignment);

        ProjectUserAssignment result = projectService.assignUserToProject(projectId, requestDto);

        assertNotNull(result);
        assertEquals(project, result.getProject());
        assertEquals(user, result.getUser());
        assertEquals(Role.ROLE_TEAM_MEMBER, result.getRole());

        verify(projectRepository).findById(projectId);
        verify(userService).findByUserId(userId);
        verify(projectUserAssignmentRepository).findByProjectIdAndUserId(projectId, userId);
        verify(projectUserAssignmentRepository).save(any(ProjectUserAssignment.class));


    }

    @Test
    void shouldNotAssignUserToProject() {
        Long projectId = 1L;
        Long userId = 2L;

        Department department = Department.builder()
                .name("Test Department")
                .build();

        Project project = Project.builder()
                .title("Test Project")
                .description("Test Description")
                .startDate(LocalDate.of(2025,1,12))
                .endDate(LocalDate.of(2025,1,16))
                .status(ProjectStatus.IN_PROGRESS)
                .department(department)
                .build();
        project.setId(projectId);

        User user = User.builder()
                .id(userId)
                .name("Test User")
                .username("Test User")
                .password("Test Password")
                .build();

        ProjectUserAssignmentRequestDto requestDto = new ProjectUserAssignmentRequestDto(
                projectId, userId, Role.ROLE_TEAM_MEMBER
        );

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userService.findByUserId(userId)).thenReturn(user);
        when(projectUserAssignmentRepository.findByProjectIdAndUserId(projectId, userId))
                .thenReturn(Optional.of(new ProjectUserAssignment())); // kullanıcı zaten atanmış

        UserAlreadyAssignedException exception = assertThrows(UserAlreadyAssignedException.class, () ->
                projectService.assignUserToProject(projectId, requestDto)
        );

        assertEquals(ErrorMessages.USER_ALREADY_ASSIGNED, exception.getMessage());

        verify(projectRepository).findById(projectId);
        verify(userService).findByUserId(userId);
        verify(projectUserAssignmentRepository).findByProjectIdAndUserId(projectId, userId);

    }


    @Test
    void shouldUpdateUserRoleInProjectSuccessfully() {

        Long userId = 1L;
        Long projectId = 2L;

        User user = User.builder()
                .id(userId)
                .name("user test")
                .username("user_test")
                .password("selena_gomez")
                .build();

        Project project = Project.builder()
                .title("Test Project")
                .description("Test Description")
                .startDate(LocalDate.of(2025,1,12))
                .endDate(LocalDate.of(2025,1,16))
                .status(ProjectStatus.IN_PROGRESS)
                .department(Department.builder().name("Test Department").build())
                .build();
        project.setId(projectId);

        ProjectUserAssignment assignment = ProjectUserAssignment.builder()
                .project(project)
                .user(user)
                .role(Role.ROLE_TEAM_MEMBER)
                .assignedAt(LocalDate.of(2025,1,12))
                .build();

        when(projectUserAssignmentRepository.findByProjectIdAndUserId(projectId, userId)).thenReturn(Optional.of(assignment));
        when(projectUserAssignmentRepository.save(any(ProjectUserAssignment.class))).thenReturn(assignment);

        ProjectUserAssignment projectUserAssignment = projectService.updateUserToProject(projectId,userId, Role.ROLE_PROJECT_GROUP_MANAGER);

        assertNotNull(projectUserAssignment);
        assertEquals(project, projectUserAssignment.getProject());
        assertEquals(user, projectUserAssignment.getUser());
        assertEquals(Role.ROLE_PROJECT_GROUP_MANAGER, projectUserAssignment.getRole());
        verify(projectUserAssignmentRepository).findByProjectIdAndUserId(projectId, userId);
        verify(projectUserAssignmentRepository).save(any(ProjectUserAssignment.class));

    }

    //Eğer kullanıcı bu projeye atanmadıysa, ResourceNotFoundException fırlatılmalı.
    @Test
    void shouldNotUpdateUserRoleInProjectFail() {
        Long userId = 1L;
        Long projectId = 2L;

        when(projectUserAssignmentRepository.findByProjectIdAndUserId(projectId, userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            projectService.updateUserToProject(projectId, userId, Role.ROLE_PROJECT_MANAGER);
        });

        assertEquals(ErrorMessages.USER_NOT_ASSIGNED, exception.getMessage());


        verify(projectUserAssignmentRepository).findByProjectIdAndUserId(projectId, userId);
        verify(projectUserAssignmentRepository, never()).save(any());

    }


    @Test
    void deleteUserFromProject() {
    }
}