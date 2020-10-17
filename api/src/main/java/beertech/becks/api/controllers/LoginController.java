package beertech.becks.api.controllers;

import beertech.becks.api.entities.User;
import beertech.becks.api.tos.request.LoginRequestTO;
import beertech.becks.api.victorauth.service.JwtService;
import beertech.becks.api.victorauth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/authentication")
    public ResponseEntity<Object> getToken(@RequestBody LoginRequestTO loginRequestTO) throws Exception {
        //try {
            User loggingInUser = User.builder()
                    //.login(loginRequestTO.getUsername())
                    //.password(loginRequestTO.getPassword())
                    .build();

            UserDetails authenticatedUser = userService.authenticate(loggingInUser);

            //return TokenTO.builder().token(jwtService.getToken(loggingInUser)).username(authenticatedUser.getUsername()).build();
            return null;

        //} catch (UsernameNotFoundException | InvalidPasswordException | Exception e) {
        //    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        //}
    }
}
