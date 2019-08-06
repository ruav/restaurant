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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.ArrayUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;

@RestController
@RequestMapping("/rest")
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
     * @param restaurantId id ресторана
     * @return jwt - токен.
     */

    @ApiOperation(value ="Возвращает jwt - токен в случае успешной авторизации", response = String.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping(value = "/authority/{restaurantId}")
    @ResponseBody
    public String authority(@ApiParam(value = "id ресторана.", example = "1", required = true) @PathVariable("restaurantId") long restaurantId,
                            @ApiParam(value = "логин", example = "test@test.com", required = true) @RequestParam String login,
                            @ApiParam(value = "пароль", example = "12345", required = true) @RequestParam String password,
                            HttpServletRequest request,
                            HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (checkAuth(cookies, restaurantId)) {
            response.setStatus(403);
            return "Already autorizated";
        }

        User user = userService.findByEmail(login);
        if (user == null) {
            response.setStatus(401);
            return "User not found";
        }
        if (user.getRole() != Role.RESTAURANT) {
            response.setStatus(401);
            return "User have not Restaurant role";
        }
        if (!passwordEncoder.matches(password, user.getPass())) {
            response.setStatus(401);
            return "Incorrect login + password";
        }

        String token = null;
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        try {
             token = JWT.create()
                     .withSubject("restaurant")
                     .withClaim("id", restaurantId)
                     .withIssuer("auth0")
                     .withExpiresAt(calendar.getTime())
                    .sign(ALGORITHM);
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        }

        Cookie cookie = new Cookie(AUTHORIZATION, token);
        cookie.setMaxAge(TTL_HOURS * 3600);
        response.addCookie(cookie);

        return token;
    }

    private boolean checkAuth(Cookie[] cookies, long id) {
        if (ArrayUtils.isEmpty(cookies)) return false;
        for (Cookie cookie : cookies) {
           if (checkAccess(cookie, id)) {
               return true;
           }
        }
        return false;
    }

    private boolean checkAccess(Cookie cookie, long id) {
        if (AUTHORIZATION.equals(cookie.getName())) {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(cookie.getValue());
            if (id == jwt.getClaim("id").asLong() && jwt.getExpiresAt().after(Calendar.getInstance().getTime())) {
                return true;
            }
        }
        return false;
    }

}
