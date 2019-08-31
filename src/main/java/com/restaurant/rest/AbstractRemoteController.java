package com.restaurant.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.*;
import com.restaurant.dto.editable.ClientModel;
import com.restaurant.entity.*;
import com.restaurant.service.*;
import com.restaurant.utils.CustomSseEmitter;
import com.restaurant.utils.DtoConverter;
import com.restaurant.vo.StatusEnum;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.restaurant.rest.AuthorityEndpoint.ALGORITHM;
import static com.restaurant.utils.DtoConverter.*;

public abstract class AbstractRemoteController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    DishService dishService;
    @Autowired
    SubCategoryService subCategoryService;
    @Autowired
    AllergenService allergenService;
    @Autowired
    ProteinService proteinService;
    @Autowired
    DtoConverter dtoConverter;
    @Autowired
    IngredientService ingredientService;
    @Autowired
    HostessService hostessService;
    @Autowired
    TagService tagService;
    @Autowired
    ClientService clientService;
    @Autowired
    UserService userService;
    @Autowired
    HallService hallService;
    @Autowired
    DeskService deskService;
    @Autowired
    CardService cardService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    StatusService statusService;
    @Autowired
    ReplacementService replacementService;


    protected static final String AUTHORIZATION = "Authorization";
    protected static final String ISSUER = "auth";
    protected static final String LOCALHOST = "localhost";
    protected static final String RESTAURANT = "restaurant";

    protected static final String BEARER = "Bearer";

    protected static ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/hostess")
    public List<HostessDto> hostessList(@PathParam("from") long from,
                                        @PathParam("to") long to,
                                        @PathParam("limit") int limit,
                                        @PathParam("offset") int offset,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws IOException {
        if (checkToken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            try {
                return hostessService.findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(restaurantId, from, to, limit, offset)
                        .stream().map(h -> getHostessDto(h, getUrl(request))).collect(Collectors.toList());
            } catch (Exception e) {
                response.getOutputStream().println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return null;
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return Collections.emptyList();
    }

    @GetMapping("/tags")
    public List<TagDto> tagsList(@PathParam("from") long from,
                                 @PathParam("to") long to,
                                 @PathParam("limit") int limit,
                                 @PathParam("offset")int offset,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        if (checkToken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            try {
                return tagService.findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                        .stream().map(DtoConverter::getTagDto).collect(Collectors.toList());
            } catch (Exception e) {
                response.getOutputStream().println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return null;
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return Collections.emptyList();
    }

    @GetMapping("/clients")
    public List<ClientDto> clientList(@PathParam("from") long from,
                                      @PathParam("to") long to,
                                      @PathParam("limit") int limit,
                                      @PathParam("offset")int offset,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {
        if (checkToken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            try {
                return clientService.findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                        .stream().map(DtoConverter::getClientDto).collect(Collectors.toList());
            } catch (Exception e) {
                response.getOutputStream().println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return null;
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return Collections.emptyList();
    }

    @GetMapping("/halls")
    public List<HallDto> hallList(@PathParam("from") long from,
                                  @PathParam("to") long to,
                                  @PathParam("limit") int limit,
                                  @PathParam("offset")int offset,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        if (checkToken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            try {
            return hallService.findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(DtoConverter::getHallDto).collect(Collectors.toList());
            } catch (Exception e) {
                response.getOutputStream().println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return null;
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return Collections.emptyList();
    }

    @GetMapping("/desks")
    public List<DeskDto> deskList(@PathParam("from") long from,
                                  @PathParam("to") long to,
                                  @PathParam("limit") int limit,
                                  @PathParam("offset")int offset,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        if (checkToken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            try {
            return deskService.findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(DtoConverter::getDeskDto).collect(Collectors.toList());
            } catch (Exception e) {
                response.getOutputStream().println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return null;
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return Collections.emptyList();
    }

    @GetMapping("/cards")
    public List<CardDto> cardList(@PathParam("from") long from,
                                  @PathParam("to") long to,
                                  @PathParam("limit") int limit,
                                  @PathParam("offset")int offset,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        if (checkToken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            try {
            return cardService.findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(DtoConverter::getCardDto).collect(Collectors.toList());
            } catch (Exception e) {
                response.getOutputStream().println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return null;
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return Collections.emptyList();
    }

    @GetMapping("/reservations")
    public List<ReservationDto> reservationList(@PathParam("from") long from,
                                                @PathParam("to") long to,
                                                @PathParam("limit") int limit,
                                                @PathParam("offset")int offset,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws IOException {
        if (checkToken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            try {
            return reservationService.findAllByRestaurantIdAndLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(DtoConverter::getReservationDto).collect(Collectors.toList());
            } catch (Exception e) {
                response.getOutputStream().println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return null;
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return Collections.emptyList();
    }

    @GetMapping("/menu")
    public RestaurantMenuModel menu(@PathParam("lastChange") long lastchange,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        String url = getUrl(request);
        long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));

        RestaurantMenuModel restaurantMenuModel = new RestaurantMenuModel();

        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);

        if (!restaurant.isPresent() || !restaurant.get().isActive())
            return restaurantMenuModel;

        restaurantMenuModel.setLogo(restaurant.get().getLogo() == null ? null : (url + "/image/" + restaurant.get().getLogo().getUrl()));
        List<AllergenDto> allergens = allergenService.findAll().stream().map(DtoConverter::getAllergenDto).collect(Collectors.toList());
        restaurantMenuModel.setAllergens(allergens);
        List<ProteinDto> proteins = proteinService.findAll().stream().map(DtoConverter::getProteinDto).collect(Collectors.toList());
        restaurantMenuModel.setProteins(proteins);

        List<String> actions = restaurant.get().getActions().stream().map(action -> url + "/image/" + action.getUrl()).collect(Collectors.toList());

        restaurantMenuModel.setActions(actions);
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categoryService.findActiveByRestaurant(restaurantId)) {

            List<SubCategory> subCategories = subCategoryService.findActiveByCategoryId(category.getId());
            Map<Long, List<Dish>> collect = subCategories.stream().collect(Collectors.toMap(SubCategory::getId, subCategory -> dishService.findActiveBySubCategoryId(subCategory.getId()), (a, b) -> b));
            CategoryDto categoryDto = DtoConverter.getCategoryDto(category, subCategories,
                    collect, url);
            categoryDtos.add(categoryDto);
        }

        restaurantMenuModel.setCategories(categoryDtos);

        return restaurantMenuModel;

    }

    @PostMapping("/create/client")
    @Transactional
    public long createClient(@RequestBody ClientModel clientModel,
                             HttpServletRequest request,
                             HttpServletResponse response
                            ) throws JsonProcessingException {

        if (checkToken(request.getHeader(AUTHORIZATION))) {

            Client client = new Client();
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            client.setName(clientModel.getName());
            client.setPhone(clientModel.getPhone());
            client.setVip(clientModel.isVip());
            client.setLastChange(getTimeStamp());
            client.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
            client.setTags(new HashSet<>());

            if (clientModel.getTags() != null && !clientModel.getTags().isEmpty()) {
                for (Long tag : clientModel.getTags()) {
                    if (tag == null) continue;
                    Optional<Tag> optionalTag = tagService.findById(tag);
                    optionalTag.ifPresent(value -> client.getTags().add(value));
                }
            }
            long id = clientService.save(client).getId();

            if (clientModel.getNewTags() != null &&  !clientModel.getNewTags().isEmpty()) {
                for (String newTag : clientModel.getNewTags()) {
                    if (newTag == null || newTag.isEmpty()) continue;
                    Tag tag1 = new Tag();
                    tag1.setName(newTag);
                    tag1.setRestaurantId(restaurantId);
                    tag1.setClientId(id);
                    tag1.setLastChange(getTimeStamp());
                    tag1 = tagService.save(tag1);
                    client.getTags().add(tag1);
                }
            }
            clientService.save(client);
            notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                    new SseMessage("client",
                    mapper.writeValueAsString(getClientDto(clientService.findById(id).get()))));
            return id;
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return -1;
    }

    @PostMapping("/update/client")
    @Transactional
    public long updateClient(@RequestBody ClientModel clientModel,
                             HttpServletRequest request,
                             HttpServletResponse response
                            ) throws JsonProcessingException {

        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }

        long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
        Client client = clientService.findById(clientModel.getId()).get();
        if (client == null) return -1;
        client.setName(clientModel.getName());
        client.setPhone(clientModel.getPhone());
        client.setVip(clientModel.isVip());
        client.setLastChange(getTimeStamp());
        client.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        if (client.getTags() == null) client.setTags(new HashSet<>());

        if (clientModel.getTags() != null && !clientModel.getTags().isEmpty()) {
            for (Long tag : clientModel.getTags()) {
                if (tag == null) continue;
                Optional<Tag> optionalTag = tagService.findById(tag);
                optionalTag.ifPresent(value -> client.getTags().add(value));
            }
        }
        if (clientModel.getNewTags() != null && !clientModel.getNewTags().isEmpty()) {
            for (String newTag : clientModel.getNewTags()) {
                if (newTag == null || newTag.isEmpty()) continue;
                Tag tag1 = new Tag();
                tag1.setName(newTag);
                tag1.setRestaurantId(restaurantId);
                tag1.setClientId(client.getId());
                tag1.setLastChange(getTimeStamp());
                tag1 = tagService.save(tag1);
                client.getTags().add(tag1);
            }
        }
        clientService.save(client);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("client",
                mapper.writeValueAsString(getClientDto(clientService.findById(client.getId()).get()))));
        return client.getId();
    }


    @PostMapping("/create/hostess")
    @Transactional
    public long createHostess(@RequestParam String name,
                              HttpServletRequest request,
                              HttpServletResponse response) throws JsonProcessingException {

        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }

        String url;
        if (request.getServerName().equalsIgnoreCase(LOCALHOST) || request.getServerName().equalsIgnoreCase("127.0.0.1")) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        } else {
            url = "https://" + request.getServerName();
        }

        Hostess hostess = new Hostess();
        hostess.setName(name);
        hostess.setLastChange(getTimeStamp());
        hostess.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = hostessService.save(hostess).getId();
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("hostess",
                mapper.writeValueAsString(getHostessDto(hostessService.findById(id).get(), url))));
        return id;
    }

    @PostMapping("/update/hostess")
    @Transactional
    public long updateHostess(@RequestParam long id,
                             @RequestParam @NotEmpty String name,
                             HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        String url;
        if (request.getServerName().equalsIgnoreCase(LOCALHOST) || request.getServerName().equalsIgnoreCase("127.0.0.1")) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        } else {
            url = "https://" + request.getServerName();
        }
        Hostess hostess = hostessService.findById(id).get();
        hostess.setName(name);
        hostess.setLastChange(getTimeStamp());
        hostess.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        hostessService.save(hostess);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("hostess",
                mapper.writeValueAsString(getHostessDto(hostessService.findById(id).get(), url))));
        return id;
    }

    @PostMapping("/create/clientTag")
    @Transactional
    public long createClientTag(@RequestParam String name,
                                @RequestParam long clientId,
                                HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        Tag tag = new Tag();
        tag.setClientId(clientId);
        tag.setName(name);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = tagService.save(tag).getId();
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("tag",
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get()))));
        return id;
    }

    @PostMapping("/update/clientTag")
    @Transactional
    public long updateClientTag(@RequestParam long id,
                                @RequestParam String name,
                                @RequestParam long clientId,
                                HttpServletRequest request,
                                HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        Tag tag = new Tag();
        tag.setClientId(clientId);
        tag.setName(name);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        tagService.save(tag);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("clientTag",
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get()))));
        return id;
    }

    @PostMapping("/create/hall")
    @Transactional
    public long createHall(@RequestParam String name,
                           @RequestParam boolean active,
                           @RequestParam boolean online,
                           HttpServletRequest request,
                           HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        Hall hall = new Hall();
        hall.setName(name);
        hall.setActive(active);
        hall.setOnline(online);
        hall.setLastChange(getTimeStamp());
        hall.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = hallService.save(hall).getId();
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("hall",
                mapper.writeValueAsString(getHallDto(hallService.findById(id).get()))));
        return id;
    }

    @PostMapping("/update/hall")
    @Transactional
    public long updateHall(@RequestParam long id,
                           @RequestParam String name,
                           @RequestParam boolean active,
                           @RequestParam boolean online,
                           HttpServletRequest request,
                           HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        Hall hall = hallService.findById(id).get();
        hall.setName(name);
        hall.setActive(active);
        hall.setOnline(online);
        hall.setLastChange(getTimeStamp());
        hall.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        hallService.save(hall);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("hall",
                mapper.writeValueAsString(getHallDto(hallService.findById(id).get()))));
        return id;
    }

    @PostMapping("/create/desk")
    @Transactional
    public long createDesk(@RequestParam long hall,
                           @RequestParam int number,
                           @RequestParam int capacity,
                           HttpServletRequest request,
                           HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        Desk desk = new Desk();
        desk.setHall(hall);
        desk.setNumber(number);
        desk.setCapacity(capacity);
        desk.setLastChange(getTimeStamp());
        desk.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = deskService.save(desk).getId();
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("desk",
                mapper.writeValueAsString(getDeskDto(deskService.findById(id).get()))));
        return id;
    }

    @PostMapping("/update/desk")
    @Transactional
    public long updateDesk(@RequestParam long id,
                           @RequestParam long hall,
                           @RequestParam int number,
                           @RequestParam int capacity,
                           HttpServletRequest request,
                           HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        Desk desk = deskService.findById(id).get();
        desk.setHall(hall);
        desk.setNumber(number);
        desk.setCapacity(capacity);
        desk.setLastChange(getTimeStamp());
        desk.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        deskService.save(desk);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("desk",
                mapper.writeValueAsString(getDeskDto(deskService.findById(id).get()))));
        return id;
    }

    @PostMapping("/create/card")
    @Transactional
    public long createCard(@RequestParam long hall,
                           @RequestParam String map,
                           @RequestParam Date relevantFrom,
                           HttpServletRequest request,
                           HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        Card card = new Card();
        card.setHall(hall);
        card.setMap(map);
        card.setRelevantFrom(relevantFrom);
        card.setLastChange(getTimeStamp());
        card.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = cardService.save(card).getId();
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("card",
                mapper.writeValueAsString(getCardDto(cardService.findById(id).get()))));
        return id;
    }

    @PostMapping("/update/card")
    @Transactional
    public long updateCard(@RequestParam long id,
                           @RequestParam long hall,
                           @RequestParam String map,
                           @RequestParam Date relevantFrom,
                           HttpServletRequest request,
                           HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        Optional<Card> card = cardService.findById(id);
        if (!card.isPresent() || card.get().getRelevantFrom().before(new Date())) {
            return -1;
        }
        card.get().setHall(hall);
        card.get().setMap(map);
        card.get().setRelevantFrom(relevantFrom);
        card.get().setLastChange(getTimeStamp());
        card.get().setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        cardService.save(card.get()).getId();
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("card",
                        mapper.writeValueAsString(getCardDto(cardService.findById(id).get()))));
        return id;
    }

    @PostMapping("/create/reservationTag")
    @Transactional
    public long createReservationTag(@RequestParam String name,
                                     @RequestParam long reservationId,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        Tag tag = new Tag();
        tag.setName(name);
        tag.setReservationId(reservationId);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = tagService.save(tag).getId();
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("reservationTag",
                        mapper.writeValueAsString(getTagDto(tagService.findById(id).get()))));
        return id;
    }

    @PostMapping("/update/reservationTag")
    @Transactional
    public long updateReservationTag(@RequestParam long id,
                                     @RequestParam String name,
                                     @RequestParam long reservationId,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }
        Tag tag = new Tag();
        tag.setName(name);
        tag.setReservationId(reservationId);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        tagService.save(tag);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("reservationTag",
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get()))));
        return id;
    }

    @PostMapping("/create/reservation")
    @Transactional
    public String createReservation(@RequestBody String body,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "Access denied";
        }

        Reservation reservation = new Reservation();
        JSONObject data = new JSONObject(body);
        Long hostessId = data.getLong("hostess");
        Long clientId = data.getLong("client");
        if (hostessId == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Hostess is empty";
        }
        if (!hostessService.findById(hostessId).isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Hostess is wrong";
        }
        if (clientId == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Client is empty";
        }
        if (!clientService.findById(clientId).isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Client is wrong";
        }
        reservation.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));

        reservation.setGuests(data.getInt("guests"));
        reservation.setTimeFrom(new Date(data.getLong("timeFrom")));
        reservation.setTimeTo(new Date(data.getLong("timeTo")));
        reservation.setClientId(clientId);
        reservation.setLastChange(getTimeStamp());

        reservation.setTables(new HashSet<>());
        reservation.setTags(new HashSet<>());
        long id = reservationService.save(reservation).getId();

        if (!data.getJSONArray("tables").isEmpty()) {
            for (Object table : data.getJSONArray("tables")) {
                if (table == null) continue;
                Optional<Desk> optionalDesk = deskService.findById(Long.valueOf((Integer) table));
                if (optionalDesk.isPresent()) {
                    reservation.getTables().add(optionalDesk.get());
                }
            }
        }
        if (!data.getJSONArray("tags").isEmpty()) {
            for (Object tag : data.getJSONArray("tags")) {
                if (tag == null) continue;
                Optional<Tag> optionalTag = tagService.findById(Long.valueOf((Integer) tag));
                if (optionalTag.isPresent()) {
                    optionalTag.get().setReservationId(reservation.getId());
                    tagService.save(optionalTag.get());
                    reservation.getTags().add(optionalTag.get());
                    notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                            new SseMessage("reservationTag",
                                    mapper.writeValueAsString(getTagDto(optionalTag.get()))));
                }
            }
        }

        reservation = reservationService.findById(id).get();

        reservation.setStatuses(new ArrayList<>());
        Status status = new Status();
        status.setReservation(id);
        status.setHostess(hostessId);
        status.setLastChange(getTimeStamp());
        status.setStatus(StatusEnum.DRAFT);
        status = statusService.save(status);
        reservation.getStatuses().add(0, status);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("status",
                        mapper.writeValueAsString(getStatusDto(status))));

        if (!data.getJSONArray("newtags").isEmpty()) {
            for (Object newTag : data.getJSONArray("newtags")) {
                if (newTag == null || ((String) newTag).isEmpty()) continue;
                Tag tag1 = new Tag();
                tag1.setName((String) newTag);
                tag1.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
                tag1.setClientId(data.getLong("client"));
                tag1.setLastChange(getTimeStamp());
                tag1 = tagService.save(tag1);
                reservation.getTags().add(tag1);
                notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                        new SseMessage("reservationTag",
                                mapper.writeValueAsString(getTagDto(tag1))));
            }
        }

        reservationService.save(reservation);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("reservation",
                mapper.writeValueAsString(getReservationEventDto(reservationService.findById(id).get(),
                        clientService.findById(reservation.getClientId()).get()))));
        return String.valueOf(id);
    }

    @PostMapping("/update/reservation")
    @Transactional
    public String updateReservation(@RequestBody String body,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "Access denied";
        }
        JSONObject data = new JSONObject(body);
        long id = data.getLong("id");
        Optional<Reservation> reservation = reservationService.findById(id);
        Long clientId = data.getLong("client");
        Long hostessId = data.getLong("hostess");
        if (hostessId == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "Hostess is empty";
        }
        if (!hostessService.findById(hostessId).isPresent()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "Hostess is wrong";
        }
        if (clientId == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Client is empty";
        }
        if (!clientService.findById(clientId).isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "Client is wrong";
        }
        if (reservation.isPresent()) {
// примерная логика записи в UTC
//            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));;
//            calendar.setTimeInMillis(System.currentTimeMillis());

            reservation.get().setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
            reservation.get().setGuests(data.getInt("guests"));
            reservation.get().setTimeFrom(new Date(data.getLong("timeFrom")));
            reservation.get().setTimeTo(new Date(data.getLong("timeTo")));
            reservation.get().setClientId(clientId);

            reservation.get().setTables(new HashSet<>());
            reservation.get().setTags(new HashSet<>());
            if (!data.getJSONArray("tables").isEmpty()) {
                for (Object table : data.getJSONArray("tables")) {
                    if (table == null) continue;
                    Optional<Desk> optionalDesk = deskService.findById(Long.valueOf((Integer) table));
                    optionalDesk.ifPresent(desk -> reservation.get().getTables().add(desk));
                }
            }
            if (!data.getJSONArray("tags").isEmpty()) {
                for (Object tag : data.getJSONArray("tags")) {
                    if (tag == null) continue;
                    Optional<Tag> optionalTag = tagService.findById(Long.valueOf((Integer) tag));
                    optionalTag.ifPresent(value -> reservation.get().getTags().add(value));
                }
            }

            reservation.get().setStatuses(new ArrayList<>());
            Status status = new Status();
            status.setReservation(id);
            status.setHostess(hostessId);
            status.setLastChange(getTimeStamp());
            status.setStatus(StatusEnum.WAITING);
            status = statusService.save(status);
            reservation.get().getStatuses().add(0, status);
            notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                    new SseMessage("status",
                            mapper.writeValueAsString(getStatusDto(status))));

            if (!data.getJSONArray("newtags").isEmpty()) {
                for (Object newTag : data.getJSONArray("newtags")) {
                    if (newTag == null || ((String) newTag).isEmpty()) continue;
                    Tag tag1 = new Tag();
                    tag1.setName((String) newTag);
                    tag1.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
                    tag1.setClientId(data.getLong("client"));
                    tag1.setLastChange(getTimeStamp());
                    tag1 = tagService.save(tag1);
                    reservation.get().getTags().add(tag1);
                    notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                            new SseMessage("reservationTag",
                                    mapper.writeValueAsString(getTagDto(tag1))));
                }
            }

            reservationService.save(reservation.get());
            notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                    new SseMessage("reservation",
                            mapper.writeValueAsString(getReservationEventDto(reservationService.findById(id).get(),
                            clientService.findById(reservation.get().getClientId()).get()))));
            return String.valueOf(id);
        }
        return "-1";
    }

    @PostMapping("/update/status")
    @Transactional
    public long updateStatus(@RequestParam long id,
                             @RequestParam int status,
                             @RequestParam long hostess,
                             HttpServletRequest request,
                             HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }

        Status oldStatus = statusService.findFirstByReservationOrderByLastChangeDesc(id);
        Status newStatus = new Status();
        Optional<StatusEnum> newStatusEnum = Arrays.stream(StatusEnum.values()).filter(e -> e.getNum() == status).findFirst();
        if (!newStatusEnum.isPresent()) return -1;
        switch (oldStatus.getStatus()) {
            case ORDER:
                if (newStatusEnum.get().equals(StatusEnum.DRAFT)) {
                    newStatus.setStatus(newStatusEnum.get());
                }
                else {
                    return -1;
                }
                break;
            case DRAFT:
                if (newStatusEnum.get().equals(StatusEnum.DRAFT)
                        || newStatusEnum.get().equals(StatusEnum.WAITING)
                        || newStatusEnum.get().equals(StatusEnum.BOOKED)
                        || newStatusEnum.get().equals(StatusEnum.COME)
                        || newStatusEnum.get().equals(StatusEnum.REFUSAL)
                ) {
                    newStatus.setStatus(newStatusEnum.get());
                }
                else {
                    return -1;
                }
                break;
            case WAITING:
                if (newStatusEnum.get().equals(StatusEnum.BOOKED)
                        || newStatusEnum.get().equals(StatusEnum.REFUSAL)
                ) {
                    newStatus.setStatus(newStatusEnum.get());
                }
                else {
                    return -1;
                }
                break;
            case BOOKED:
                if (newStatusEnum.get().equals(StatusEnum.COME)
                        || newStatusEnum.get().equals(StatusEnum.NOT_COME)
                        || newStatusEnum.get().equals(StatusEnum.REFUSAL)
                ) {
                    newStatus.setStatus(newStatusEnum.get());
                }
                else {
                    return -1;
                }
                break;
            case COME:
                if (newStatusEnum.get().equals(StatusEnum.GET_AWAY)) {
                    newStatus.setStatus(newStatusEnum.get());
                }
                else {
                    return -1;
                }
                break;
        }
        newStatus.setReservation(id);
        newStatus.setLastChange(new Date().getTime());
        newStatus.setHostess(hostess);


        newStatus = statusService.save(newStatus);
        Reservation reservation = reservationService.findById(id).get();
        reservation.getStatuses().add(0, newStatus);
        reservationService.save(reservation);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                new SseMessage("status",
                        mapper.writeValueAsString(getStatusDto(newStatus))));
        return id;
    }

    @PostMapping("/update/replacement")
    @Transactional
    public long updateReplacement(@RequestParam long id,
                                  @RequestParam Date when,
                                  @RequestParam int tableFrom,
                                  @RequestParam int tableTo,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws JsonProcessingException {
        if (!checkToken(request.getHeader(AUTHORIZATION))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return -1;
        }

        Optional<Reservation> reservation = reservationService.findById(id);
        if (reservation.isPresent()) {

            Replacement replacement = new Replacement();
            replacement.setReservation(id);
            replacement.setTime(when);
            replacement.setDeskFrom(tableFrom);
            replacement.setDeskTo(tableTo);
            replacement = replacementService.save(replacement);
            reservation.get().getReplacements().add(replacement);
            notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                    new SseMessage("replacement",
                            mapper.writeValueAsString(getReplacementDto(replacement))));
            return id;
        }
        return -1;
    }

    @GetMapping("/sync")
    public SseEmitter streamSseMvc(HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        if (!checkToken(request.getHeader(AUTHORIZATION), true)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        SseEmitter emitter = new SseEmitter(1_000_000L);
        long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));

        CustomSseEmitter customSseEmitter = new CustomSseEmitter(emitter, restaurantId, notificationService);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("UTC")));
        SseEmitter.SseEventBuilder event = SseEmitter.event()
                .data(calendar.getTimeInMillis())
                .id(String.valueOf(System.currentTimeMillis()))
                .reconnectTime(3 * 1_000)
                .name("timestamp");
        customSseEmitter.send(event);
        notificationService.addEmitter(customSseEmitter);

        return emitter;
    }

    protected boolean checkToken(String auth, boolean checkAccess) {
        if (auth == null) return false;
        if (!auth.startsWith(BEARER)) return false;
        String[] authArray = auth.split(" ");
        if (authArray.length != 2) return false;
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt;
        try {
         jwt = verifier.verify(authArray[1]);
        } catch (Exception e) {
            return false;
        }
        if (jwt.getClaim("id").isNull()) return false;
        if (jwt.getClaim(RESTAURANT).isNull()) return false;

        if (!checkAccess &&
                jwt.getClaim(RESTAURANT).asLong() != userService.findById(jwt.getClaim("id").asLong()).get().getRestaurants().stream().findFirst().get().getId()) {
            return true;
        }

        if (checkAccess &&
                jwt.getClaim(RESTAURANT).asLong() == userService.findById(jwt.getClaim("id").asLong()).get().getRestaurants().stream().findFirst().get().getId()) {
            return true;
        }

        if (jwt.getExpiresAt().after(Calendar.getInstance().getTime())) {
            return true;
        }

        return false;
    }

    protected boolean checkToken(String auth) {
        return checkToken(auth, false);
    }

    protected long getRestaurantId(String auth) {
        String[] authArray = auth.split(" ");

        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(authArray[1]);
        return jwt.getClaim(RESTAURANT).asLong();
    }

    protected long getTimeStamp() {
        return System.currentTimeMillis();
    }

    protected String getUrl(HttpServletRequest request) {
        String url;
        if (request.getServerName().equalsIgnoreCase(LOCALHOST) || request.getServerName().equalsIgnoreCase("127.0.0.1")) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        } else {
            url = "https://" + request.getServerName();
        }
        return url;
    }

}
