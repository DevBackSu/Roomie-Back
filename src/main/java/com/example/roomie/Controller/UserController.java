package com.example.roomie.Controller;

import com.example.roomie.SwaggerForm.UserControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController implements UserControllerDocs {
    public List<String> getUser() {
        List<String> data = Arrays.asList("리스트", "모음", "한 번에" , "반환");
        return data;
    }

}
