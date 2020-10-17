package beertech.becks.api.security.authentication.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationService {



    void addAuthentication(HttpServletResponse response, String username) throws IOException;

    Authentication getAuthentication(HttpServletRequest request);
}
