package beertech.becks.api.controllers;

import beertech.becks.api.entities.User;
import beertech.becks.api.security.service.JwtService;
import beertech.becks.api.service.UserService;
import beertech.becks.api.tos.request.LoginRequestTO;
import beertech.becks.api.tos.response.LoginResponseTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static beertech.becks.api.constants.Constants.*;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/login")
@Api(value = "Bank Becks Service")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = STATUS_200_GET_OK),
                    @ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
                    @ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
            })
    @PostMapping("/authentication")
    public ResponseEntity<LoginResponseTO> getAuthentication(@RequestBody LoginRequestTO loginRequestTO) throws Exception {
        User loggingInUser = userService.findUserByEmailAndPasswordForLogin(loginRequestTO.getEmail(), loginRequestTO.getPassword());
        return ok(getAuthenticationWithToken(loggingInUser));
    }

	private LoginResponseTO getAuthenticationWithToken(User loggingInUser) {
		return LoginResponseTO.builder().token(jwtService.getToken(loggingInUser)).build();
	}

}
