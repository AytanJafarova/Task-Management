package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.TaskPriority;
import com.tms.TaskManagementSystem.entity.enums.TaskStatus;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.exception.response.ResponseMessage;
import com.tms.TaskManagementSystem.mappers.TaskMapper;
import com.tms.TaskManagementSystem.repository.TaskRepository;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskListResponse;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import com.tms.TaskManagementSystem.services.TaskService;
import com.tms.TaskManagementSystem.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final WorkerRepository workerRepository;
    boolean checkingTaskInfo(String header) {
        if (header.isBlank()) {
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_TASK_HEADER);
        }
        taskRepository.findByHeader(header.toLowerCase())
                .ifPresent(task -> {
                    throw new IllegalArgumentException(ResponseMessage.ERROR_TASK_ALREADY_EXISTS);
                });
        return true;
    }
    TaskListResponse getListByStatus(TaskStatus status, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByStatus(status, pageable);
        TaskListResponse response = TaskListResponse.builder().build();
        response.setItems(tasks.getContent().stream().map(TaskMapper.INSTANCE::taskToTaskResponse).collect(Collectors.toList()));
        response.setPaginationInfo(PaginationUtil.getPaginationInfo(tasks));
        return response;
    }
    @Override
    public TaskResponse save(CreateTaskRequest request) {
        if(checkingTaskInfo(request.getHeader()))
        {
            if(request.getDeadline()!=null && request.getDeadline().isAfter(LocalDateTime.now(ZoneId.of("Asia/Baku"))))
            {
                try{
                    Task task = taskRepository.save(Task.builder()
                            .header(request.getHeader().toLowerCase())
                            .content(request.getContent().toLowerCase())
                            .priority(TaskPriority.definingDegree(request.getPriority().getIntValue()))
                            .status(TaskStatus.TO_DO)
                            .created(LocalDateTime.now(ZoneId.of("Asia/Baku")))
                            .deadline(request.getDeadline()).build());
                    return TaskResponse.builder()
                            .id(task.getId())
                            .header(task.getHeader().toLowerCase())
                            .content(task.getContent().toLowerCase())
                            .priority(task.getPriority())
                            .status(task.getStatus())
                            .created(task.getCreated())
                            .deadline(task.getDeadline())
                            .closed(task.getClosed())
                            .build();
                }
                catch (Exception e)
                {
                    throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_PRIORITY_PROVIDED);
                }
            }
            else{
                throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_DEADLINE_PROVIDED);
            }
        }
        else{
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION);
        }
    }

    @Override
    public TaskResponse update(Long id,UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));

        if(checkingTaskInfo(request.getHeader()) && request.getDeadline()!=null && request.getDeadline().isAfter(LocalDateTime.now(ZoneId.of("Asia/Baku"))))
        {
            task.setHeader(request.getHeader().toLowerCase());
            task.setContent(request.getContent().toLowerCase());
            task.setDeadline(request.getDeadline());
            task.setPriority(request.getPriority());
            taskRepository.save(task);
            return TaskMapper.INSTANCE.taskToTaskResponse(task);
        }
        else{
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION);
        }
    }

    @Override
    public TaskResponse assign(Long id,Long workerId) {
        Task selectedTask = taskRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));

        Worker worker = workerRepository.findByIdAndStatus(workerId, WorkerStatus.ACTIVE)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_WORKER_NOT_FOUND_BY_ID));

        switch (selectedTask.getStatus()){
                case TaskStatus.TO_DO:
                    selectedTask.setWorker(worker);
                    selectedTask.setStatus(TaskStatus.IN_PROGRESS);
                    if(selectedTask.getDeadline() == null)
                    {
                        selectedTask.setDeadline(LocalDateTime.now(ZoneId.of("Asia/Baku")).plusWeeks(1));
                    }
                    taskRepository.save(selectedTask);
                    return TaskMapper.INSTANCE.taskToTaskResponse(selectedTask);
                case TaskStatus.IN_PROGRESS:
                case TaskStatus.DONE:
                case TaskStatus.OVERDUE:
                case TaskStatus.DELAYED:
                default:
                    throw new IllegalArgumentException(ResponseMessage.ERROR_TASK_ASSIGNED);
        }
    }

    @Override
    public TaskResponse close(Long id) {
        Task selectedTask = taskRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));

        switch (selectedTask.getStatus()){
                case TaskStatus.IN_PROGRESS:
                case TaskStatus.OVERDUE:
                case TaskStatus.DELAYED:
                {
                        selectedTask.setClosed(LocalDateTime.now(ZoneId.of("Asia/Baku")));
                        selectedTask.setStatus(TaskStatus.DONE);
                        taskRepository.save(selectedTask);
                        return TaskMapper.INSTANCE.taskToTaskResponse(selectedTask);
                }
                case TaskStatus.TO_DO:
                    throw new IllegalArgumentException(ResponseMessage.ERROR_TASK_NOT_ASSIGNED);
                case TaskStatus.DONE:
                    throw new IllegalArgumentException(ResponseMessage.ERROR_TASK_CLOSED);
                default:
                    throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION);
            }
    }

    @Override
    public boolean delete(Long id) {
        Task selectedTask = taskRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));
        taskRepository.delete(selectedTask);
        return true;
    }
    @Override
    public TaskListResponse getTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        TaskListResponse response = TaskListResponse.builder().build();
        response.setItems(tasks.getContent().stream().map(TaskMapper.INSTANCE::taskToTaskResponse).collect(Collectors.toList()));
        response.setPaginationInfo(PaginationUtil.getPaginationInfo(tasks));
        return response;
    }

    @Override
    public TaskListResponse getByTaskStatus(TaskStatus status, Pageable pageable) {
        return getListByStatus(status,PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }
    @Override
    public List<TaskResponse> getByTaskStatus(TaskStatus status) {
        return getByStatus(status);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));
        return TaskMapper.INSTANCE.taskToTaskResponse(task);
    }
    @Override
    public TaskListResponse getByWorkerId(Long id,Pageable pageable) {
        Worker worker = workerRepository.findByIdAndStatus(id, WorkerStatus.ACTIVE)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));

        Page<Task> tasks = taskRepository.findByWorkerId(worker.getId(),PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        TaskListResponse response = TaskListResponse.builder().build();
        response.setItems(tasks.getContent().stream().map(TaskMapper.INSTANCE::taskToTaskResponse).collect(Collectors.toList()));
        response.setPaginationInfo(PaginationUtil.getPaginationInfo(tasks));
        return response;
    }
    List<TaskResponse> getByStatus(TaskStatus status) {
        List<Task> tasks = taskRepository.findByStatus(status);
        List<TaskResponse> taskResponses = new ArrayList<>();
        for (Task task : tasks) {
            taskResponses.add(TaskMapper.INSTANCE.taskToTaskResponse(task));
        }
        return taskResponses;
    }

    public List<TaskResponse> deadlineCheckingInProgress() {
        List<TaskResponse> inProgress = getByTaskStatus(TaskStatus.IN_PROGRESS);
        if (!inProgress.isEmpty()) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Baku"));
            for (TaskResponse task : inProgress) {
                if(task.getDeadline().isBefore(now)) {
                        Task taskExe = taskRepository.findById(task.getId()).get();
                        taskExe.setDeadline(task.getDeadline().plusDays(3));
                        taskExe.setStatus(TaskStatus.OVERDUE);
                        taskRepository.save(taskExe);
                }
            }
        }
        return inProgress;
    }

    public List<TaskResponse> deadlineCheckingOverdue()
    {
        List<TaskResponse> overDue = getByTaskStatus(TaskStatus.OVERDUE);
        if (!overDue.isEmpty()) {
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Baku"));
            for (TaskResponse task : overDue) {
                if(task.getDeadline().isBefore(now)) {
                        Task taskExe = taskRepository.findById(task.getId()).get();
                        taskExe.setStatus(TaskStatus.DELAYED);
                        taskRepository.save(taskExe);
                }
            }
        }
        return overDue;
    }
}