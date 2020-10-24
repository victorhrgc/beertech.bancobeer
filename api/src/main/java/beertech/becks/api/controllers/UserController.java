package beertech.becks.api.controllers;

import static beertech.becks.api.constants.Constants.*;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import beertech.becks.api.amqp.RabbitProducer;
import beertech.becks.api.exception.user.UserAlreadyExistsException;
import beertech.becks.api.service.UserService;
import beertech.becks.api.tos.request.UserRequestTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Api(value = "Bank Becks Service")
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RabbitProducer producer;

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = STATUS_201_CREATED),
            @ApiResponse(code = 400, message = STATUS_400_BAD_REQUEST),
            @ApiResponse(code = 500, message = STATUS_500_INTERNAL_SERVER_ERROR) })
    @PostMapping
    @ApiOperation(value = "Create user")
	public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequestTO userRequestTO)
			throws UserAlreadyExistsException {
		log.info("create user");
		return new ResponseEntity<>(userService.createUser(userRequestTO), HttpStatus.CREATED);
	}
}
