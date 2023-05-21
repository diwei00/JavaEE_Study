package com.spring.demo.service;

import com.spring.demo.repository.Repository2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Service2 {

    @Autowired
    private Repository2 repository2;

    public void hello() {
        repository2.hello();
    }
}
