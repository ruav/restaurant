package com.restaurant.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.*;
import com.restaurant.entity.*;
import com.restaurant.service.*;
import com.restaurant.utils.CustomSseEmitter;
import com.restaurant.utils.DtoConverter;
import com.restaurant.vo.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
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

    protected static ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/hostess")
    public List<HostessDto> hostessList(@PathParam("from") int from,
                                        @PathParam("to") int to,
                                        @PathParam("limit") int limit,
                                        @PathParam("offset") int offset,
                                        HttpServletRequest request) {
        if (checkTocken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            return hostessService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(h -> getHostessDto(h, getUrl(request))).collect(Collectors.toList());
        }
        return Collections.emptyList();

    }

    @GetMapping("/tags")
    public List<TagDto> tagsList(@PathParam("from") int from,
                                 @PathParam("to") int to,
                                 @PathParam("limit") int limit,
                                 @PathParam("offset")int offset,
                                 HttpServletRequest request) {
        if (checkTocken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            return tagService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(DtoConverter::getTagDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @GetMapping("/clients")
    public List<ClientDto> clientList(@PathParam("from") int from,
                                      @PathParam("to") int to,
                                      @PathParam("limit") int limit,
                                      @PathParam("offset")int offset,
                                      HttpServletRequest request) {
        if (checkTocken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            return clientService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(DtoConverter::getClientDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @GetMapping("/halls")
    public List<HallDto> hallList(@PathParam("from") int from,
                                  @PathParam("to") int to,
                                  @PathParam("limit") int limit,
                                  @PathParam("offset")int offset,
                                  HttpServletRequest request) {
        if (checkTocken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            return hallService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(DtoConverter::getHallDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @GetMapping("/desks")
    public List<DeskDto> deskList(@PathParam("from") int from,
                                  @PathParam("to") int to,
                                  @PathParam("limit") int limit,
                                  @PathParam("offset")int offset,
                                  HttpServletRequest request) {
        if (checkTocken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            return deskService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(DtoConverter::getDeskDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @GetMapping("/cards")
    public List<CardDto> cardList(@PathParam("from") int from,
                                  @PathParam("to") int to,
                                  @PathParam("limit") int limit,
                                  @PathParam("offset")int offset,
                                  HttpServletRequest request) {
        if (checkTocken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            return cardService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(DtoConverter::getCardDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @GetMapping("/reservations")
    public List<ReservationDto> reservationList(@PathParam("from") int from,
                                                @PathParam("to") int to,
                                                @PathParam("limit") int limit,
                                                @PathParam("offset")int offset,
                                                HttpServletRequest request) {
        if (checkTocken(request.getHeader(AUTHORIZATION))) {
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            return reservationService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset)
                    .stream().map(DtoConverter::getReservationDto).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @GetMapping("/menu")
    public RestaurantMenuModel menu(@PathParam("lastChange") long lastchange,
                                    HttpServletRequest request) {

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
    public long createClient(@RequestParam String name,
                             @RequestParam @NotNull String phone,
                             @RequestParam Boolean vip,
                             @RequestParam(required = false) Long[] tags,
                             @RequestParam(required = false) String[] newTags,
                             HttpServletRequest request
    ) throws JsonProcessingException {

        if (checkTocken(request.getHeader(AUTHORIZATION))) {

            Client client = new Client();
            long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
            client.setName(name);
            client.setPhone(phone);
            client.setVip(vip);
            client.setLastChange(getTimeStamp());
            client.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
            client.setTags(new HashSet<>());
            if (tags != null && tags.length > 0) {
                Arrays.stream(tags).filter(Objects::nonNull).forEach(tag -> tagService.findById(tag).ifPresent(tag1 -> client.getTags().add(tag1)));
            }
            long id = clientService.save(client).getId();
            if (newTags != null && newTags.length > 0) {
                Arrays.stream(newTags).filter(tag -> tag != null && !tag.isEmpty()).forEach(tag -> {
                    Tag tag1 = new Tag();
                    tag1.setName(tag);
                    tag1.setRestaurantId(restaurantId);
                    tag1.setClientId(id);
                    tag1.setLastChange(System.currentTimeMillis());
                    tag1 = tagService.save(tag1);
                    client.getTags().add(tag1);
                });
            }
            clientService.save(client);
            notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                    mapper.writeValueAsString(getClientDto(clientService.findById(id).get())));
            return id;
        }
        return -1;
    }

    @PostMapping("/update/client")
    public long updateClient(@RequestParam long id,
                             @RequestParam String name,
                             @RequestParam @NotNull String phone,
                             @RequestParam boolean vip,
                             @RequestParam(required = false) Long[] tags,
                             @RequestParam(required = false) String[] newTags,
                             HttpServletRequest request
    ) throws JsonProcessingException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));
        Client client = clientService.findById(id).get();
        client.setId(id);
        client.setName(name);
        client.setPhone(phone);
        client.setVip(vip);
        client.setLastChange(getTimeStamp());
        client.setRestaurantId(restaurantId);
        if (tags != null && tags.length > 0) {
            Arrays.stream(tags).forEach(tag -> tagService.findById(tag).ifPresent(tag1 -> client.getTags().add(tag1)));
        }
        if (newTags != null && newTags.length > 0) {
            Arrays.stream(newTags).forEach(tag -> {
                Tag tag1 = new Tag();
                tag1.setName(tag);
                tag1.setRestaurantId(restaurantId);
                tag1.setClientId(id);
                tag1.setLastChange(System.currentTimeMillis());
                tag1 = tagService.save(tag1);
                client.getTags().add(tag1);
            });        }
        clientService.save(client);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getClientDto(clientService.findById(id).get())));
        return id;
    }


    @PostMapping("/create/hostess")
    public long createHostess(@RequestParam String name,
                              HttpServletRequest request) throws JsonProcessingException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
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
                mapper.writeValueAsString(getHostessDto(hostessService.findById(id).get(), url)));
        return id;
    }

    @PostMapping("/update/hostes")
    public long updateHostes(@RequestParam long id,
                             @RequestParam @NotEmpty String name,
                             HttpServletRequest request) throws JsonProcessingException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
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
                mapper.writeValueAsString(getHostessDto(hostessService.findById(id).get(), url)));
        return id;
    }

    @PostMapping("/create/clientTag")
    public long createClientTag(@RequestParam String name,
                                @RequestParam long clientId,
                                HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Tag tag = new Tag();
        tag.setClientId(clientId);
        tag.setName(name);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = tagService.save(tag).getId();
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/clientTag")
    public long updateClientTag(@RequestParam long id,
                                @RequestParam String name,
                                @RequestParam long clientId,
                                HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Tag tag = new Tag();
        tag.setClientId(clientId);
        tag.setName(name);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        tagService.save(tag);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get())));
        return id;
    }

    @PostMapping("/create/hall")
    public long createHall(@RequestParam String name,
                           @RequestParam boolean active,
                           @RequestParam boolean online,
                           HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
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
                mapper.writeValueAsString(getHallDto(hallService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/hall")
    public long updateHall(@RequestParam long id,
                           @RequestParam String name,
                           @RequestParam boolean active,
                           @RequestParam boolean online,
                           HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
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
                mapper.writeValueAsString(getHallDto(hallService.findById(id).get())));
        return id;
    }

    @PostMapping("/create/desk")
    public long createDesk(@RequestParam long hall,
                           @RequestParam int number,
                           @RequestParam int capacity,
                           HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
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
                mapper.writeValueAsString(getDeskDto(deskService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/desk")
    public long updateDesk(@RequestParam long id,
                           @RequestParam long hall,
                           @RequestParam int number,
                           @RequestParam int capacity,
                           HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
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
                mapper.writeValueAsString(getDeskDto(deskService.findById(id).get())));
        return id;
    }

    @PostMapping("/create/card")
    public long createCard(@RequestParam long hall,
                           @RequestParam String map,
                           @RequestParam Date relevantFrom,
                           HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
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
                mapper.writeValueAsString(getCardDto(cardService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/card")
    public long updateCard(@RequestParam long id,
                           @RequestParam long hall,
                           @RequestParam String map,
                           @RequestParam Date relevantFrom,
                           HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
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
                mapper.writeValueAsString(getCardDto(cardService.findById(id).get())));
        return id;
    }

    @PostMapping("/create/reservationTag")
    public long createReservationTag(@RequestParam String name,
                                     @RequestParam long reservationId,
                                     HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Tag tag = new Tag();
        tag.setName(name);
        tag.setReservationId(reservationId);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = tagService.save(tag).getId();
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/reservationTag")
    public long updateReservationTag(@RequestParam long id,
                                     @RequestParam String name,
                                     @RequestParam long reservationId,
                                     HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Tag tag = new Tag();
        tag.setName(name);
        tag.setReservationId(reservationId);
        tag.setLastChange(getTimeStamp());
        tag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        tagService.save(tag);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get())));
        return id;
    }

    @PostMapping("/create/reservation")
    public long createReservation(@RequestParam int guests,
                                  @RequestParam long hostes,
                                  @RequestParam Date date,
                                  @RequestParam Date timeFrom,
                                  @RequestParam Date timeTo,
                                  @RequestParam long client,
                                  @RequestParam Long[] tables,
                                  @RequestParam int[] tags,
                                  @RequestParam String[] newTags,
                                  HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Reservation reservation = new Reservation();

        reservation.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        reservation.setGuests(guests);
        reservation.setDate(date);
        reservation.setTimeFrom(timeFrom);
        reservation.setTimeTo(timeTo);
        reservation.setClientId(client);

        reservation.setTables(new HashSet<>());
        reservation.setTags(new HashSet<>());
        if (tables != null && tables.length > 0) {
            for (long table : tables) {
                Optional<Desk> desk = deskService.findById(table);
                desk.ifPresent(reservation.getTables()::add);
            }
        }

        if (tags != null && tags.length > 0) {
            for (int tag : tags) {
                Optional<Tag> tag1 = tagService.findById(tag);
                tag1.ifPresent(reservation.getTags()::add);
            }
        }

        long id = reservationService.save(reservation).getId();

        reservation = reservationService.findById(id).get();

        reservation.setStatuses(new ArrayList<>());
        Status status = new Status();
        status.setReservation(id);
        status.setDateTime(new Date());
        status.setHostess(hostes);
        status.setLastChange(System.currentTimeMillis());
        status.setStatus(StatusEnum.WAITING);
        status = statusService.save(status);
        reservation.getStatuses().add(0, status);

        if (newTags != null && newTags.length > 0) {
            for (String tag : newTags) {
                Tag newTag = new Tag();
                newTag.setName(tag);
                newTag.setClientId(client);
                newTag.setReservationId(id);
                newTag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
                newTag.setLastChange(System.currentTimeMillis());
                reservation.getTags().add(newTag);
            }
        }

        reservationService.save(reservation);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getReservationEventDto(reservationService.findById(id).get(),
                        clientService.findById(reservation.getClientId()).get())));
        return id;
    }

    @PostMapping("/update/reservation")
    public long updateReservation(@RequestParam long id,
                                  @RequestParam int guests,
                                  @RequestParam long hostes,
                                  @RequestParam Date date,
                                  @RequestParam Date timeFrom,
                                  @RequestParam Date timeTo,
                                  @RequestParam long client,
                                  @RequestParam Long[] tables,
                                  @RequestParam int[] tags,
                                  @RequestParam String[] newTags,
                                  HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Optional<Reservation> reservation = reservationService.findById(id);
        if (reservation.isPresent()) {

            reservation.get().setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
            reservation.get().setGuests(guests);
            reservation.get().setDate(date);
            reservation.get().setTimeFrom(timeFrom);
            reservation.get().setTimeTo(timeTo);
            reservation.get().setClientId(client);

            reservation.get().setStatuses(new ArrayList<>());
            reservation.get().setTables(new HashSet<>());
            reservation.get().setTags(new HashSet<>());
            if (tables != null && tables.length > 0) {
                for (long table : tables) {
                    Optional<Desk> desk = deskService.findById(table);
                    desk.ifPresent(reservation.get().getTables()::add);
                }
            }

            if (tags != null && tags.length > 0) {
                for (int tag : tags) {
                    Optional<Tag> tag1 = tagService.findById(tag);
                    tag1.ifPresent(reservation.get().getTags()::add);
                }
            }

            if (newTags != null && newTags.length > 0) {
                for (String tag : newTags) {
                    Tag newTag = new Tag();
                    newTag.setName(tag);
                    newTag.setClientId(client);
                    newTag.setReservationId(id);
                    newTag.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
                    newTag.setLastChange(System.currentTimeMillis());
                    reservation.get().getTags().add(newTag);
                }
            }

            reservationService.save(reservation.get());
            notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                    mapper.writeValueAsString(getReservationEventDto(reservationService.findById(id).get(),
                            clientService.findById(reservation.get().getClientId()).get())));
            return id;
        }
        return -1l;
    }

    @PostMapping("/update/status")
    public long updateStatus(@RequestParam long id,
                             @RequestParam int status,
                             @RequestParam long hostes,
                             HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Status oldStatus = statusService.findFirstByReservationOrderByLastChangeDesc(id);
        Status newStatus = new Status();
        Optional<StatusEnum> newStatusEnum = Arrays.stream(StatusEnum.values()).filter(e -> e.getNum() == status).findFirst();
        if (!newStatusEnum.isPresent()) return -1;
        switch (oldStatus.getStatus()) {
            case ORDER:
                if (newStatusEnum.equals(StatusEnum.DRAFT)) {
                    newStatus.setStatus(newStatusEnum.get());
                }
                else {
                    return -1;
                }
                break;
            case DRAFT:
                if (newStatusEnum.equals(StatusEnum.DRAFT)
                        || newStatusEnum.equals(StatusEnum.WAITING)
                        || newStatusEnum.equals(StatusEnum.BOOKED)
                        || newStatusEnum.equals(StatusEnum.COME)
                        || newStatusEnum.equals(StatusEnum.REFUSAL)
                ) {
                    newStatus.setStatus(newStatusEnum.get());
                }
                else {
                    return -1;
                }
                break;
            case WAITING:
                if (newStatusEnum.equals(StatusEnum.BOOKED)
                        || newStatusEnum.equals(StatusEnum.REFUSAL)
                ) {
                    newStatus.setStatus(newStatusEnum.get());
                }
                else {
                    return -1;
                }
                break;
            case BOOKED:
                if (newStatusEnum.equals(StatusEnum.COME)
                        || newStatusEnum.equals(StatusEnum.NOT_COME)
                        || newStatusEnum.equals(StatusEnum.REFUSAL)
                ) {
                    newStatus.setStatus(newStatusEnum.get());
                }
                else {
                    return -1;
                }
                break;
            case COME:
                if (newStatusEnum.equals(StatusEnum.GET_AWAY)) {
                    newStatus.setStatus(newStatusEnum.get());
                }
                else {
                    return -1;
                }
                break;
        }
        newStatus.setReservation(id);
        newStatus.setDateTime(new Date());
        newStatus.setLastChange(newStatus.getDateTime().getTime());
        newStatus.setHostess(hostes);


        newStatus = statusService.save(newStatus);
        Reservation reservation = reservationService.findById(id).get();
        reservation.getStatuses().add(0, newStatus);
        reservationService.save(reservation);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getStatusDto(newStatus)));
        return id;
    }

    @PostMapping("/update/replacement")
    public long updateReplacement(@RequestParam long id,
                                  @RequestParam Date when,
                                  @RequestParam int tableFrom,
                                  @RequestParam int tableTo,
                                  HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Replacement replacement = new Replacement();
        replacement.setReservation(id);
        replacement.setTime(when);
        replacement.setDeskFrom(tableFrom);
        replacement.setDeskTo(tableTo);
        replacement = replacementService.save(replacement);

        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getReplacementDto(replacement)));
        return id;
    }

    @GetMapping("/sync")
    public SseEmitter streamSseMvc(HttpServletRequest request,
                                   HttpServletResponse response) {
        if (!checkTocken(request.getHeader(AUTHORIZATION), true)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        SseEmitter emitter = new SseEmitter(1_000 * 1_000L);
        long restaurantId = getRestaurantId(request.getHeader(AUTHORIZATION));

        notificationService.addEmitter(new CustomSseEmitter(emitter, restaurantId, notificationService));

        return emitter;
    }

    protected boolean checkTocken(String auth, boolean checkAccess) {
        if (auth == null) return false;
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(auth);

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

    protected boolean checkTocken(String auth) {
        return checkTocken(auth, false);
    }

    protected long getRestaurantId(String auth) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(auth);
        return jwt.getClaim(RESTAURANT).asLong();
    }

    protected long getTimeStamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return calendar.getTimeInMillis();
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
