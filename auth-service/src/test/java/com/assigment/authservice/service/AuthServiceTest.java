package com.assigment.authservice.service;

import com.assigment.authservice.dto.request.RegisterRequest;
import com.assigment.authservice.service.impl.AuthServiceImpl;
import com.tracking.commonservice.service.UserServiceRpc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserServiceRpc userServiceRpc;

    @Test
    void testRegister_CaseUsernameAlreadyInUse() {
        String name = "Trịnh Trần Sỹ Đông";
        String username = "john9101";
        String password = "john9101";
        String email = "trinhdong1098@gmail.com";
        RegisterRequest request = new RegisterRequest();
        request.setName(name);
        request.setUsername(username);
        request.setPassword(password);
        request.setEmail(email);

        assertThrows(RuntimeException.class, () -> authService.register(request));
        verify(userServiceRpc, never()).createUser(anyMap(), anyList());
    }
}
