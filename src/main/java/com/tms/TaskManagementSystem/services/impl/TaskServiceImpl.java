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
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import com.tms.TaskManagementSystem.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final WorkerRepository workerRepository;
    boolean checkingTaskInfo(String header, boolean ignoreHeader) {
        if (header.isBlank()) {
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_TASK_HEADER);
        }

        taskRepository.findByHeader(header.toLowerCase())
                .ifPresent(task -> {
                    throw new IllegalArgumentException(ResponseMessage.ERROR_TASK_ALREADY_EXISTS);
                });
        return true;
    }
    List<TaskResponse> getByStatus(TaskStatus status,Pageable pageable)
    {
        List<Task> tasks = taskRepository.findByStatus(status,PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task task : tasks)
        {
            TaskResponse taskResponse = TaskMapper.INSTANCE.taskToTaskResponse(task);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }
    @Override
    public TaskResponse save(CreateTaskRequest request) {
        if(checkingTaskInfo(request.getHeader(),false))
        {
            try{
                Task task = taskRepository.save(Task.builder()
                        .header(request.getHeader().toLowerCase())
                        .content(request.getContent().toLowerCase())
                        .priority(TaskPriority.definingDegree(request.getPriority().getIntValue()))
                        .status(TaskStatus.definingStatus(0))
                        .created(LocalDateTime.now())
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
            throw new IllegalArgumentException(ResponseMessage.ERROR_INVALID_OPERATION);
        }
    }

    @Override
    public TaskResponse update(Long id,UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));
        boolean ignoreHeader= task.getHeader().equalsIgnoreCase(request.getHeader());

        if(checkingTaskInfo(request.getHeader(), ignoreHeader))
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
                    taskRepository.save(selectedTask);
                    return TaskMapper.INSTANCE.taskToTaskResponse(selectedTask);

                case TaskStatus.IN_PROGRESS:
                case TaskStatus.DONE:
                default:
                    throw new IllegalArgumentException(ResponseMessage.ERROR_TASK_ASSIGNED);
        }
    }

    @Override
    public TaskResponse close(Long id) {
        Task selectedTask = taskRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));

        switch (selectedTask.getStatus()){
                case TaskStatus.IN_PROGRESS: {
                        selectedTask.setClosed(LocalDateTime.now());
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
    public boolean arrived(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));
        LocalDateTime now = LocalDateTime.now();
        if(task.getDeadline() !=null)
        {
            return task.getDeadline().isBefore(now); // if true => Deadline arrived
        }
        else{
            throw new IllegalArgumentException(ResponseMessage.ERROR_DEADLINE_NOT_SPECIFIED);
        }
    }

    @Override
    public List<TaskResponse> getTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task task : tasks)
        {
            TaskResponse taskResponse = TaskMapper.INSTANCE.taskToTaskResponse(task);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    @Override
    public List<TaskResponse> getInprogress(Pageable pageable) {
        return getByStatus(TaskStatus.IN_PROGRESS,PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));
        return TaskMapper.INSTANCE.taskToTaskResponse(task);
    }
    @Override
    public List<TaskResponse> getByWorkerId(Long id,Pageable pageable) {
        Worker worker = workerRepository.findByIdAndStatus(id, WorkerStatus.ACTIVE)
                .orElseThrow(()->new DataNotFoundException(ResponseMessage.ERROR_TASK_NOT_FOUND_BY_ID));

        List<Task> tasks = taskRepository.findByWorkerId(id,PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task task : tasks)
        {
            TaskResponse taskResponse = TaskMapper.INSTANCE.taskToTaskResponse(task);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    @Override
    public List<TaskResponse> getClosed(Pageable pageable) {
        return getByStatus(TaskStatus.DONE,PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }

    @Override
    public List<TaskResponse> getTodo(Pageable pageable) {
        return getByStatus(TaskStatus.TO_DO,PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
    }
}