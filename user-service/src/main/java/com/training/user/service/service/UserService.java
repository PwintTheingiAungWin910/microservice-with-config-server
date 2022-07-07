package com.training.user.service.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.training.user.service.Model.User;
import com.training.user.service.VO.Department;
import com.training.user.service.VO.ResponseTemplateVO;
import com.training.user.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public User saveUser(User user) {
        log.info("Inside saveUser of UserService");
        return userRepository.save(user);
    }

    @HystrixCommand(fallbackMethod = "userServiceFallBackMethod")
    public ResponseTemplateVO getUserWithDepartment(Long userId) {
        log.info("Inside getUserWithDepartment of UserService");
        ResponseTemplateVO vo = new ResponseTemplateVO();
        User user = userRepository.findByUserId(userId);

        Department department =
                restTemplate.getForObject("http://DEPARTMENT-SERVICE/departments/" + user.getDepartmentId()
                        ,Department.class);

        vo.setUser(user);
        vo.setDepartment(department);

        return  vo;
    }
    public ResponseTemplateVO userServiceFallBackMethod(Long userId)  {
        return new ResponseTemplateVO(new User(0L,"","","",0L),
                new Department());
    }
}
