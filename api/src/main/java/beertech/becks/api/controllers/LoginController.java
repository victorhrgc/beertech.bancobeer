package beertech.becks.api.controllers;

import beertech.becks.api.entities.User;
import beertech.becks.api.tos.request.LoginRequestTO;
import beertech.becks.api.tos.response.LoginResponseTO;
import beertech.becks.api.victorauth.service.JwtService;
import beertech.becks.api.victorauth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static beertech.becks.api.constants.Constants.*;

@RestController
@RequestMapping("/login")
@Api(value = "Bank Becks Service")
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
	public ResponseEntity<LoginResponseTO> getToken(@RequestBody LoginRequestTO loginRequestTO) throws Exception {

		User loggingInUser = User.builder().email(loginRequestTO.getUsername()).password(loginRequestTO.getPassword())
				.build();

		userService.authenticate(loggingInUser);

		return new ResponseEntity<>(LoginResponseTO.builder().token(jwtService.getToken(loggingInUser)).build(),
				HttpStatus.OK);

	}
}
