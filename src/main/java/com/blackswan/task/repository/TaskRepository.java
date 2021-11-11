package com.blackswan.task.repository;

import com.blackswan.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);
    Optional<Task> findByIdAndUserId(Long id, Long userId);

    @Query("select a from Task a where a.date_time <= :currentTime and a.status = :status")
    List<Task> findAllWithCreationDateTimeBefore(
            @Param("currentTime") LocalDateTime currentTime,
            @Param("status") String status);

}
