package com.restaurant.rest;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mobile")
@Api(value = "MobileEndpoint")
public class MobileEndpoint extends AbstractRemoteController {


}
