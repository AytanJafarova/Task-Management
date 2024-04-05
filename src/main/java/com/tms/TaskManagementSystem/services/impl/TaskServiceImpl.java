package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.domain.Task;
import com.tms.TaskManagementSystem.domain.Worker;
import com.tms.TaskManagementSystem.enums.TaskDegree;
import com.tms.TaskManagementSystem.enums.TaskStatus;
import com.tms.TaskManagementSystem.exception.DataNotFoundException;
import com.tms.TaskManagementSystem.exception.IllegalArgumentException;
import com.tms.TaskManagementSystem.mappers.TaskMapper;
import com.tms.TaskManagementSystem.repository.TaskRepository;
import com.tms.TaskManagementSystem.repository.WorkerRepository;
import com.tms.TaskManagementSystem.request.Task.*;
import com.tms.TaskManagementSystem.response.Task.TaskForWorkerResponse;
import com.tms.TaskManagementSystem.response.Task.TaskResponse;
import com.tms.TaskManagementSystem.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
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
        List<Task> tasks = taskRepository.findAll();
        boolean result = true;
        if (!header.isBlank()) {
            for (Task task : tasks) {
                if ((!ignoreHeader && task.getHeader().equalsIgnoreCase(header))) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    @Override
    public TaskResponse AddTask(CreateTaskRequest request) {
        if(checkingTaskInfo(request.getHeader(),false))
        {
            try{
                Task task = taskRepository.save(Task.builder()
                        .header(request.getHeader().toLowerCase())
                        .content(request.getContent().toLowerCase())
                        .degree(TaskDegree.definingDegree(request.getDegree()))
                        .status(TaskStatus.definingStatus(0))
                        .created(LocalDateTime.now())
                        .deadline(request.getDeadline()).build());
                return TaskResponse.builder()
                        .header(task.getHeader().toLowerCase())
                        .content(task.getContent().toLowerCase())
                        .degree(task.getDegree())
                        .status(task.getStatus())
                        .created(task.getCreated())
                        .build();
            }
            catch (Exception e)
            {
                throw new DataNotFoundException("Degree with int value of "+request.getDegree()+ " is not found");
            }
        }
        else{
            throw new IllegalArgumentException("Invalid operation!");
        }
    }

    @Override
    public TaskResponse UpdateTask(Long id,UpdateTaskRequest request) {
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
    public TaskResponse AssignTask(Long id,AssignTaskRequest request) {
        Optional<Task> selectedTask = taskRepository.findById(id);
        Optional<Worker> selectedWorker = workerRepository.findById(request.getId());
        if(selectedTask.isPresent())
        {
            if(selectedWorker.isPresent())
            {
                selectedTask.get().setWorker(selectedWorker.get());
                selectedTask.get().setStatus(TaskStatus.IN_PROGRESS);
                taskRepository.save(selectedTask.get());
                return TaskMapper.INSTANCE.taskToTaskResponse(selectedTask.get());
            }
            else{
                throw new DataNotFoundException("Worker with id of "+ request.getId()+" is not found!");
            }
        }
        else{
            throw new DataNotFoundException("Task with id of "+ id+" is not found!");
        }

    }

    @Override
    public TaskResponse UpdateTaskDegree(Long id,UpdateTaskDegreeRequest request) {
        Optional<Task> selectedTask = taskRepository.findById(id);
        if(selectedTask.isPresent())
        {
            if(selectedTask.get().getClosed() ==null)
            {
                try {
                    selectedTask.get().setDegree(TaskDegree.definingDegree(request.getDegree()));
                    taskRepository.save(selectedTask.get());
                    return TaskMapper.INSTANCE.taskToTaskResponse(selectedTask.get());
                }
                catch (Exception e)
                {
                    throw new DataNotFoundException("Degree with int value of "+request.getDegree()+ " is not found");
                }

            }
            else{
                throw new IllegalArgumentException("Invalid operation! Task is closed");
            }
        }
        else{
            throw new DataNotFoundException("Task with id of "+ id+" is not found!");
        }
    }

//    @Override
//    public TaskResponse UpdateTaskStatus(Long id,UpdateTaskStatusRequest request) {
//        Optional<Task> selectedTask = taskRepository.findById(id);
//        if(selectedTask.isPresent())
//        {
//            if(selectedTask.get().getClosed() ==null)
//            {
//                try{
//                    selectedTask.get().setStatus(TaskStatus.definingStatus(request.getStatus()));
//                    taskRepository.save(selectedTask.get());
//                    return TaskMapper.INSTANCE.taskToTaskResponse(selectedTask.get());
//                }
//                catch(Exception e)
//                {
//                    throw new DataNotFoundException("Status with int value of "+request.getStatus()+ " is not found");
//                }
//            }
//            else{
//                throw new IllegalArgumentException("Invalid operation! Task is closed");
//            }
//        }
//        else{
//            throw new DataNotFoundException("Task with id of "+ id+" is not found!");
//        }
//    }

    @Override
    public TaskResponse CloseTask(Long id) {
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
    public boolean DeleteTask(Long id) {
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
    public boolean DeadlineArrived(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent())
        {
            LocalDateTime deadline = task.get().getDeadline();
            LocalDateTime now = LocalDateTime.now();
            return deadline.isBefore(now); // Deadline arrived
        }
        else{
            throw new DataNotFoundException("Task with id of " + id + " is not found!");
        }
    }

    @Override
    public List<TaskResponse> GetTasks() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task task : tasks)
        {
            TaskResponse taskResponse = TaskMapper.INSTANCE.taskToTaskResponse(task);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    @Override
    public TaskResponse GetTaskById(Long id) {
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
    public TaskResponse GetTaskByHeader(String header) {
        Optional<Task> task = taskRepository.findByHeader(header.toLowerCase());
        if(task.isPresent())
        {
            return TaskMapper.INSTANCE.taskToTaskResponse(task.get());
        }
        else{
            throw new DataNotFoundException("Task with header of " + header + " is not found!");
        }
    }

    @Override
    public List<TaskForWorkerResponse> GetTasksByWorkerId(Long id) {
        List<Task> tasks = taskRepository.findByWorkerId(id);
        List<TaskForWorkerResponse> taskResponses = new ArrayList<>();
        for(Task task : tasks)
        {
            TaskForWorkerResponse taskResponse = TaskMapper.INSTANCE.taskToTaskForWorkerResponse(task);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }

    @Override
    public List<TaskResponse> GetClosedTasks() {
        List<Task> tasks = taskRepository.findAllClosedTasks();
        List<TaskResponse> taskResponses = new ArrayList<>();
        for(Task task : tasks)
        {
            TaskResponse taskResponse = TaskMapper.INSTANCE.taskToTaskResponse(task);
            taskResponses.add(taskResponse);
        }
        return taskResponses;
    }
}