package beertech.becks.api.controllers;

import beertech.becks.api.entities.Account;
import beertech.becks.api.entities.User;
import beertech.becks.api.exception.account.AccountAlreadyExistsException;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.service.UserService2;
import beertech.becks.api.tos.request.AccountRequestTO;
import beertech.becks.api.tos.request.UserRequestTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static beertech.becks.api.constants.Constants.*;
import static beertech.becks.api.constants.Constants.STATUS_500_INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/users")
@Api(value = "Bank Becks Service")
public class UserController {

    @Autowired
    private UserService2 userService2;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = STATUS_200_POST_OK),
            @ApiResponse(code = 201, message = STATUS_201_CREATED),
            @ApiResponse(code = 400, message = STATUS_400_BAD_REQUEST),
            @ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR) })
    @PostMapping
    public ResponseEntity<Object> createAccount(@Valid @RequestBody UserRequestTO userRequestTO)
            throws AccountAlreadyExistsException {
        User createdUser = userService2.createUser(userRequestTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
