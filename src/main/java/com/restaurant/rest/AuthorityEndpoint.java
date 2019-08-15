package com.restaurant.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.restaurant.entity.User;
import com.restaurant.service.UserService;
import com.restaurant.vo.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;

@RestController
@RequestMapping("/mobile")
@Api(value = "AuthorityEndpoint")
public class AuthorityEndpoint {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static int TTL_HOURS = 24;
    private final static String AUTHORIZATION = "Authorization";

    private final static String SECRET_WORD = "secret";

    public final static Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_WORD);

    /**
     * Возвращает jwt - токен в случае успешной авторизации.
     * Так же устанавливает куки.
     * @return jwt - токен.
     */

    @ApiOperation(value ="Возвращает jwt - токен в случае успешной авторизации", response = String.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping(value = "/authority")
    @ResponseBody
    public String authority(@ApiParam(value = "логин", example = "test@test.com", required = true) @RequestParam String login,
                            @ApiParam(value = "пароль", example = "12345", required = true) @RequestParam String password,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        Boolean access = checkAccess(request.getHeader(AUTHORIZATION));

        if (access) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "Already autorizated";
        }

        User user = userService.findByEmail(login);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "User not found";
        }
        if (user.getRole() != Role.RESTAURANT) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "User have not Restaurant role";
        }
        if (!passwordEncoder.matches(password, user.getPass())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "Incorrect login + password";
        }

        String token = null;
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        if (user.getRestaurants().isEmpty()) {
            return "User haven't restaurant";
        }
        long restaurantId = user.getRestaurants().stream().findFirst().get().getId();
        try {
             token = JWT.create()
                     .withSubject("user")
                     .withClaim("id", user.getId())
                     .withClaim("restaurant", restaurantId)
                     .withIssuer("auth")
                     .withExpiresAt(calendar.getTime())
                    .sign(ALGORITHM);
        } catch (JWTCreationException e) {

        }

        return token;
    }

    private Boolean checkAccess(String auth) {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer("auth")
                    .build();
            DecodedJWT jwt = verifier.verify(auth);

            if (jwt.getClaim("id").isNull()) return false;
            if (jwt.getClaim("restaurant").isNull()) return false;

            if (jwt.getClaim("restaurant").asLong() != userService.findById(jwt.getClaim("id").asLong()).get().getRestaurants().stream().findFirst().get().getId()) {
                return true;
            }

            if (jwt.getExpiresAt().after(Calendar.getInstance().getTime())) {
                return true;
            }

        return false;
    }

}
