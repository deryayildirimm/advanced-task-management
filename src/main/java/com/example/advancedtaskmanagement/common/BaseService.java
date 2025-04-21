package com.example.advancedtaskmanagement.common;

import com.example.advancedtaskmanagement.exception.ResourceNotFoundException;
import com.example.advancedtaskmanagement.exception.UserNotAuthenticatedException;
import com.example.advancedtaskmanagement.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public abstract class BaseService<T extends BaseEntity> {

    private final JpaRepository<T, Long> repository;

    protected BaseService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public T findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOT_FOUND + id));
    }

    public T save(T entity) {
        return repository.save(entity);
    }
    public void softDelete(Long id) {
        T entity = findById(id);
        entity.setDeleted(true);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(getCurrentUserId());
        repository.save(entity);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user.getId();
        }
        throw new UserNotAuthenticatedException(ErrorMessages.USER_NOT_AUTHENTICATED);
    }

}
