package beertech.becks.api.controllers;

import beertech.becks.api.tos.request.LoginRequestTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static beertech.becks.api.constants.Constants.*;

@RestController
@RequestMapping("/login")
@Api(value = "Bank Becks Service")
public class LoginController {

    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = STATUS_200_GET_OK),
                    @ApiResponse(code = 404, message = STATUS_404_NOT_FOUND),
                    @ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR)
            })
    @GetMapping("/authentication")
    public ResponseEntity<Object> getToken(@RequestBody LoginRequestTO loginRequestTO){
        System.out.println(loginRequestTO);
        return null;
    }
}
