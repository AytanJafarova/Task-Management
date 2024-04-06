package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.entity.Task;
import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.entity.enums.TaskPriority;
import com.tms.TaskManagementSystem.entity.enums.TaskStatus;
import com.tms.TaskManagementSystem.entity.enums.WorkerStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
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
        Optional<Task> taskSelect = taskRepository.findByHeader(header.toLowerCase());
        return !header.isBlank() && taskSelect.isEmpty();
    }
    List<TaskResponse> getByStatus(TaskStatus status,int pgNum,int pgSize)
    {
        Pageable pageable = PageRequest.of(pgNum, pgSize);
        List<Task> tasks = taskRepository.findByStatus(status,pageable);
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
                throw new DataNotFoundException("Degree with int value of "+request.getPriority()+ " is not found");
            }
        }
        else{
            throw new IllegalArgumentException("Invalid operation!");
        }
    }

    @Override
    public TaskResponse update(Long id,UpdateTaskRequest request) {
        Optional<Task> selectedTask = taskRepository.findById(id);
        boolean ignoreHeader=false;
        if(selectedTask.isPresent())
        {
            if(selectedTask.get().getHeader().equalsIgnoreCase(request.getHeader()))
            {
                ignoreHeader=true;
            }
            if(checkingTaskInfo(request.getHeader(), ignoreHeader))
            {
                selectedTask.get().setHeader(request.getHeader().toLowerCase());
                selectedTask.get().setContent(request.getContent().toLowerCase());
                selectedTask.get().setDeadline(request.getDeadline());
                selectedTask.get().setPriority(request.getPriority());
                taskRepository.save(selectedTask.get());
                return TaskMapper.INSTANCE.taskToTaskResponse(selectedTask.get());
            }
            else{
                throw new IllegalArgumentException("Invalid operation!");
            }
        }
        else{
            throw new DataNotFoundException("Task with id of "+ id+" is not found!");
        }
    }

    @Override
    public TaskResponse assign(Long id,Long workerId) {
        Optional<Task> selectedTask = taskRepository.findByIdAndStatus(id,TaskStatus.TO_DO);
        Optional<Worker> worker = workerRepository.findByIdAndStatus(workerId, WorkerStatus.ACTIVE);
        if(selectedTask.isPresent())
        {
            if(worker.isPresent())
            {
                selectedTask.get().setWorker(worker.get());
                selectedTask.get().setStatus(TaskStatus.IN_PROGRESS);
                taskRepository.save(selectedTask.get());
                return TaskMapper.INSTANCE.taskToTaskResponse(selectedTask.get());
            }
            else{
                throw new DataNotFoundException("Worker with id of "+ workerId+" is not found!");
            }
        }
        else{
            throw new DataNotFoundException("Task with id of "+ id+" is not found!");
        }

    }

    @Override
    public TaskResponse close(Long id) {
        Optional<Task> selectedTask = taskRepository.findById(id);
        if(selectedTask.isPresent())
        {
            if(selectedTask.get().getWorker() != null)
            {
                selectedTask.get().setClosed(LocalDateTime.now());
                selectedTask.get().setStatus(TaskStatus.DONE);
                taskRepository.save(selectedTask.get());
                return TaskMapper.INSTANCE.taskToTaskResponse(selectedTask.get());
            }
            else{
                throw new IllegalArgumentException("Invalid operation! Task is not assigned!");
            }
        }
        else{
            throw new DataNotFoundException("Task with id of "+ id+" is not found!");
        }
    }

    @Override
    public boolean delete(Long id) {
        Optional<Task> selectedTask = taskRepository.findById(id);
        if(selectedTask.isPresent())
        {
            taskRepository.delete(selectedTask.get());
            return true;
        }
        else{
            throw new DataNotFoundException("Task with id of "+ id+" is not found!");
        }
    }

    @Override
    public boolean arrived(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent())
        {
            LocalDateTime now = LocalDateTime.now();
            if(task.get().getDeadline() !=null)
            {
                return task.get().getDeadline().isBefore(now); // if true => Deadline arrived
            }

             else{
                 throw new IllegalArgumentException("Deadline is not specified");
            }
        }
        else{
            throw new DataNotFoundException("Task with id of " + id + " is not found!");
        }
    }

    @Override
    public List<TaskResponse> getTasks(int pgNum,int pgSize) {
        Pageable pageable = PageRequest.of(pgNum, pgSize);
        Page<Task> tasks = taskRepository.findAll(pageable);
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task task : tasks)
        {
            TaskResponse taskResponse = TaskMapper.INSTANCE.taskToTaskResponse(task);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    @Override
    public List<TaskResponse> getInprogress(int pgNum,int pgSize) {
        return getByStatus(TaskStatus.IN_PROGRESS,pgNum, pgSize);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent())
        {
            return TaskMapper.INSTANCE.taskToTaskResponse(task.get());
        }
        else{
            throw new DataNotFoundException("Task with id of " + id + " is not found!");
        }
    }
    @Override
    public List<TaskResponse> getByWorkerId(Long id,int pgNum,int pgSize) {
        Optional<Worker> worker = workerRepository.findByIdAndStatus(id, WorkerStatus.ACTIVE);
        Pageable pageable = PageRequest.of(pgNum, pgSize);
        if(worker.isPresent())
        {
            List<Task> tasks = taskRepository.findByWorkerId(id,pageable);
            List<TaskResponse> taskResponses = new ArrayList<>();
            for(Task task : tasks)
            {
                TaskResponse taskResponse = TaskMapper.INSTANCE.taskToTaskResponse(task);
                taskResponses.add(taskResponse);
            }
            return taskResponses;
        }
        else{
            throw new DataNotFoundException("Worker with the id of "+id+" is not found");
        }
    }

    @Override
    public List<TaskResponse> getClosed(int pgNum,int pgSize) {
        return getByStatus(TaskStatus.DONE,pgNum,pgSize);
    }

    @Override
    public List<TaskResponse> getTodo(int pgNum,int pgSize) {
        return getByStatus(TaskStatus.TO_DO,pgNum,pgSize);
    }
}