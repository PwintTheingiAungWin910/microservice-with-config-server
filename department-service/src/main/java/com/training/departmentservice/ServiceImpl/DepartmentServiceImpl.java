package com.training.departmentservice.ServiceImpl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.training.departmentservice.Model.Department;
import com.training.departmentservice.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl {

    @Autowired
    DepartmentRepository departmentRepository;
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @HystrixCommand(fallbackMethod = "departmentServiceFallBackMethod")
    public Department findDepartmentById(Long id ){
        return departmentRepository.findByDepartmentId(id);
    }

    public Department departmentServiceFallBackMethod(Long id) {
        return new Department();
    }
}
