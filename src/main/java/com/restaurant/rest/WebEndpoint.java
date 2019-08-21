package com.restaurant.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.entity.*;
import com.restaurant.service.*;
import com.restaurant.utils.DtoConverter;
import com.restaurant.utils.ResizeImage;
import com.restaurant.vo.StatusEnum;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.restaurant.rest.AuthorityEndpoint.ALGORITHM;
import static com.restaurant.utils.DtoConverter.*;
import static com.restaurant.utils.ManageFiles.createIcon;
import static com.restaurant.utils.ManageFiles.createPhoto;
import static com.restaurant.utils.ManageFiles.saveFile;

@RestController
@RequestMapping("/web")
@Api(value = "MobileEndpoint")
public class WebEndpoint {

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
    HostesService hostesService;
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
    StatusService statusService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    ReplacementService replacementService;
    @Autowired
    IconService iconService;

    private static final String AUTHORIZATION = "Authorization";
    private static final String LOCALHOST = "localhost";

    private Map<Long, Queue<String>> map = new ConcurrentHashMap<>();
    private static ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(WebEndpoint.class);

    @GetMapping("/hostes")
    public List<Hostess> hostesList(@PathParam("from") int from,
                                    @PathParam("to") int to,
                                    @PathParam("limit") int limit,
                                    @PathParam("offset") int offset,
                                    @PathParam("restaurantId") int restaurantId,
                                    HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return hostesService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.emptyList();

    }

    @GetMapping("/tags")
    public List<Tag> tagsList(@PathParam("from") int from,
                              @PathParam("to") int to,
                              @PathParam("limit") int limit,
                              @PathParam("offset")int offset,
                              @PathParam("restaurantId")int restaurantId,
                              HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return tagService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.emptyList();
    }

    @GetMapping("/clients")
    public List<Client> clientList(@PathParam("from") int from,
                                   @PathParam("to") int to,
                                   @PathParam("limit") int limit,
                                   @PathParam("offset")int offset,
                                   @PathParam("restaurantId")int restaurantId,
                                   HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return clientService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.emptyList();
    }

    @GetMapping("/halls")
    public List<Hall> hallList(@PathParam("from") int from,
                                   @PathParam("to") int to,
                                   @PathParam("limit") int limit,
                                   @PathParam("offset")int offset,
                                   @PathParam("restaurantId")int restaurantId,
                                   HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return hallService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.emptyList();
    }

    @GetMapping("/desks")
    public List<Desk> deskList(@PathParam("from") int from,
                               @PathParam("to") int to,
                               @PathParam("limit") int limit,
                               @PathParam("offset")int offset,
                               @PathParam("restaurantId")int restaurantId,
                               HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return deskService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.emptyList();
    }

    @GetMapping("/cards")
    public List<Card> cardList(@PathParam("from") int from,
                               @PathParam("to") int to,
                               @PathParam("limit") int limit,
                               @PathParam("offset")int offset,
                               @PathParam("restaurantId")int restaurantId,
                               HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return cardService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.emptyList();
    }

    @GetMapping("/reservations")
    public List<Reservation> reservationList(@PathParam("from") int from,
                                             @PathParam("to") int to,
                                             @PathParam("limit") int limit,
                                             @PathParam("offset")int offset,
                                             @PathParam("restaurantId")int restaurantId,
                                             HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return reservationService.findAllByLastChangeBetweenOrderByLastChangeAsc(from, to, restaurantId, limit, offset);
        }
        return Collections.emptyList();
    }

    @PostMapping("/create/client")
    public long createClient(@RequestParam String name,
                             @RequestParam @NotNull String phone,
                             @RequestParam boolean vip,
                             @RequestParam Long[] tags,
                             @RequestParam Long[] newTags,
                             HttpServletRequest request
    ) throws JsonProcessingException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {

            Client client = new Client();

            client.setName(name);
            client.setPhone(phone);
            client.setVip(vip);
            client.setLastChange(getTimeStamp());
            client.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
            client.setTags(new HashSet<>());
            if (tags != null && tags.length > 0) {
                Arrays.stream(tags).forEach(tag -> client.getTags().add(tagService.findById(tag).get()));
            }
            if (newTags != null && newTags.length > 0) {
                Arrays.stream(newTags).forEach(tag -> client.getTags().add(tagService.findById(tag).get()));
            }
            long id = clientService.save(client).getId();
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
                             @RequestParam Long[] tags,
                             @RequestParam Long[] newTags,
                             HttpServletRequest request
    ) throws JsonProcessingException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }
        Client client = clientService.findById(id).get();
        client.setId(id);
        client.setName(name);
        client.setPhone(phone);
        client.setVip(vip);
        client.setLastChange(getTimeStamp());
        client.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        if (tags != null && tags.length > 0) {
            Arrays.stream(tags).forEach(tag -> client.getTags().add(tagService.findById(tag).get()));
        }
        if (newTags != null && newTags.length > 0) {
            Arrays.stream(newTags).forEach(tag -> client.getTags().add(tagService.findById(tag).get()));
        }
        clientService.save(client);
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getClientDto(clientService.findById(id).get())));
        return id;
    }


    @PostMapping("/create/hostes")
    public long createHostes(@RequestParam String name,
                             HttpServletRequest request) throws JsonProcessingException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        String url;
        if (request.getServerName().equalsIgnoreCase("localhost") || request.getServerName().equalsIgnoreCase("127.0.0.1")) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        } else {
            url = "https://" + request.getServerName();
        }

        Hostess hostess = new Hostess();
        hostess.setName(name);
        hostess.setLastChange(getTimeStamp());
        hostess.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        long id = hostesService.save(hostess).getId();
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getHostesDto(hostesService.findById(id).get(), url)));
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
        if (request.getServerName().equalsIgnoreCase("localhost") || request.getServerName().equalsIgnoreCase("127.0.0.1")) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        } else {
            url = "https://" + request.getServerName();
        }
        Hostess hostess = hostesService.findById(id).get();
        hostess.setName(name);
        hostess.setLastChange(getTimeStamp());
        hostess.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        hostesService.save(hostess);
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getHostesDto(hostesService.findById(id).get(), url)));
        return id;
    }

    @PostMapping(value = "/upload/hostes")
    public String uploadRestaurantHandler(@RequestParam("file") MultipartFile file,
                                          @PathVariable("id") long id,
                                          HttpServletRequest request) throws IOException, NoSuchAlgorithmException {

        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return "Unauthorizated";
        }

        if (file.isEmpty()) {
            return  "file is empty";
        } else {
            Optional<Hostess> hostes = hostesService.findById(id);
            if (hostes.isPresent()) {
                Photo photo = createPhoto(saveFile(file, ResizeImage.Size.PHOTO));
                hostes.get().setPhoto(photo);
                hostesService.save(hostes.get());
                return photo.getUrl();
            }
        }
        return "";
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
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
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
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
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
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
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
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
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
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
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
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
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
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
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
        addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getCardDto(cardService.findById(id).get())));
        return id;
    }

    @PostMapping("/create/reservationTag")
    public long createReservationTag(@RequestParam String name,
                                     @RequestParam long clientId,
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
        tag.setClientId(clientId);
        long id = tagService.save(tag).getId();
        notificationService.addElement(getRestaurantId(request.getHeader(AUTHORIZATION)),
                mapper.writeValueAsString(getTagDto(tagService.findById(id).get())));
        return id;
    }

    @PostMapping("/update/reservationTag")
    public long updateReservationTag(@RequestParam long id,
                                     @RequestParam String name,
                                     @RequestParam long clientId,
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
        tag.setClientId(clientId);
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

    @PostMapping("/create/category")
    public long createCategory(@RequestParam boolean active,
                               @RequestParam String name,
                               @RequestParam MultipartFile file,
                               HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Category category = new Category();
        if (!file.isEmpty()) {
            category.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        category.setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        category.setActive(active);
        category.setName(name);
        return categoryService.save(category).getId();
    }

    @GetMapping("/get/category")
    public String getCategory(@RequestParam long id,
                               HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return "Access denied";
        }

        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) return "Not found";
        List<SubCategory> subCategories = subCategoryService.findActiveByCategoryId(category.get().getId());

        return mapper.writeValueAsString(DtoConverter.getCategoryDto(category.get(),
                subCategories,
                subCategories.stream().collect(Collectors.toMap(SubCategory::getId, subCategory -> dishService.findActiveBySubCategoryId(subCategory.getId()), (a, b) -> b)),
                getUrl(request)));
    }

    @PostMapping("/update/category")
    public long updateCategory(@RequestParam long id,
                               @RequestParam boolean active,
                               @RequestParam(required = false) MultipartFile file,
                               @RequestParam String name,
                               HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) return -1l;
        if (!file.isEmpty()) {
            category.get().setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        category.get().setRestaurantId(getRestaurantId(request.getHeader(AUTHORIZATION)));
        category.get().setActive(active);
        category.get().setName(name);
        categoryService.save(category.get());
        return id;
    }

    @PostMapping("/delete/category")
    public long deleteCategory(@RequestParam long id,
                               HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) return -1l;
        categoryService.delete(category.get());
        return id;
    }

    @PostMapping("/create/subcategory")
    public long createSubcategory(@RequestParam boolean active,
                                  @RequestParam String name,
                                  @RequestParam long categoryId,
                                  @RequestParam(required = false) MultipartFile file,
                                  HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setCategoryId(categoryId);
        subCategory.setActive(active);
        if (!file.isEmpty()) {
            subCategory.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        return subCategoryService.save(subCategory).getId();
    }

    @GetMapping("/get/subcategory")
    public String getSubcategory(@RequestParam long id,
                              HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return "Access denied";
        }

        Optional<SubCategory> subCategory = subCategoryService.findById(id);
        if (!subCategory.isPresent()) return "Not found";

        return mapper.writeValueAsString(DtoConverter.getSubCategoryDto(subCategory.get(), dishService.findActiveBySubCategoryId(subCategory.get().getId()),
                getUrl(request)));
    }

    @PostMapping("/update/subcategory")
    public long updateSubcategory(@RequestParam long id,
                               @RequestParam boolean active,
                               @RequestParam String name,
                               @RequestParam long categoryId,
                               @RequestParam(required = false) MultipartFile file,
                               HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<SubCategory> subCategory = subCategoryService.findById(id);
        if (!subCategory.isPresent()) return -1l;
        subCategory.get().setCategoryId(categoryId);
        subCategory.get().setActive(active);
        subCategory.get().setName(name);
        if (!file.isEmpty()) {
            subCategory.get().setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        subCategoryService.save(subCategory.get());
        return id;
    }

    @PostMapping("/delete/subcategory")
    public long deleteSubcategory(@RequestParam long id,
                               HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<SubCategory> subCategory = subCategoryService.findById(id);
        if (!subCategory.isPresent()) return -1l;
        subCategoryService.delete(subCategory.get());
        return id;
    }


    @PostMapping("/create/dish")
    public long createDish(@RequestParam boolean active,
                                  @RequestParam String name,
                                  @RequestParam String description,
                                  @RequestParam String video,
                                  @RequestParam long subCategoryId,
                                  @RequestParam int calories,
                                  @RequestParam int carbohydrates,
                                  @RequestParam int fats,
                                  @RequestParam int fiber,
                                  @RequestParam float price,
                                  @RequestParam int salt,
                                  @RequestParam int cellulose,
                                  @RequestParam int sugar,
                                  @RequestParam int saturatedFats,
                                  @RequestParam int weight,
                                  @RequestParam List<Long> proteins,
                                  @RequestParam List<Long> ingredients,
                                  @RequestParam List<Long> allergens,
                                  @RequestParam(required = false) MultipartFile file,
                                  HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Dish dish = new Dish();
        dish.setSubCategoryId(subCategoryId);
        dish.setVideo(video);
        dish.setActive(active);
        dish.setCalories(calories);
        dish.setCarbohydrates(carbohydrates);
        dish.setCellulose(cellulose);
        dish.setDescription(description);
        dish.setFats(fats);
        dish.setFiber(fiber);
        dish.setName(name);
        dish.setPrice(price);
        dish.setSalt(salt);
        dish.setSugar(sugar);
        dish.setSaturatedFats(saturatedFats);
        dish.setWeight(weight);

        Set<Protein> proteinList = new HashSet<>();
        Set<Ingredient> ingredientList = new HashSet<>();
        Set<Allergen> allergenList = new HashSet<>();
        proteins.stream().map(protein -> proteinService.findById(protein)).forEach(optional -> optional.ifPresent(proteinList::add));
        ingredients.stream().map(ingredient -> ingredientService.findById(ingredient)).forEach(optional -> optional.ifPresent(ingredientList::add));
        allergens.stream().map(allergen -> allergenService.findById(allergen)).forEach(optional -> optional.ifPresent(allergenList::add));

        dish.setProteins(proteinList);
        dish.setIngredients(ingredientList);
        dish.setAllergens(allergenList);

        if (!file.isEmpty()) {
            dish.setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        return dishService.save(dish).getId();
    }

    @GetMapping("/get/dish")
    public String getDish(@RequestParam long id,
                                 HttpServletRequest request) throws JsonProcessingException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return "Access denied";
        }

        Optional<Dish> dish = dishService.findById(id);
        if (!dish.isPresent()) return "Not found";

        return mapper.writeValueAsString(DtoConverter.getDishDto(dish.get(), getUrl(request)));
    }

    @PostMapping("/update/dish")
    public long updateDish(@RequestParam long id,
                                  @RequestParam boolean active,
                                  @RequestParam String name,
                                  @RequestParam String description,
                                  @RequestParam String video,
                                  @RequestParam long subCategoryId,
                                  @RequestParam int calories,
                                  @RequestParam int carbohydrates,
                                  @RequestParam int fats,
                                  @RequestParam int fiber,
                                  @RequestParam float price,
                                  @RequestParam int salt,
                                  @RequestParam int cellulose,
                                  @RequestParam int sugar,
                                  @RequestParam int saturatedFats,
                                  @RequestParam int weight,
                                  @RequestParam List<Long> proteins,
                                  @RequestParam List<Long> ingredients,
                                  @RequestParam List<Long> allergens,
                                  @RequestParam(required = false) MultipartFile file,
                                  HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<Dish> dish = dishService.findById(id);
        if (!dish.isPresent()) return -1l;
        dish.get().setSubCategoryId(subCategoryId);
        dish.get().setVideo(video);
        dish.get().setActive(active);
        dish.get().setCalories(calories);
        dish.get().setCarbohydrates(carbohydrates);
        dish.get().setCellulose(cellulose);
        dish.get().setDescription(description);
        dish.get().setFats(fats);
        dish.get().setFiber(fiber);
        dish.get().setName(name);
        dish.get().setPrice(price);
        dish.get().setSalt(salt);
        dish.get().setSugar(sugar);
        dish.get().setSaturatedFats(saturatedFats);
        dish.get().setWeight(weight);

        Set<Protein> proteinList = new HashSet<>();
        Set<Ingredient> ingredientList = new HashSet<>();
        Set<Allergen> allergenList = new HashSet<>();
        proteins.stream().map(protein -> proteinService.findById(protein)).forEach(optional -> optional.ifPresent(proteinList::add));
        ingredients.stream().map(ingredient -> ingredientService.findById(ingredient)).forEach(optional -> optional.ifPresent(ingredientList::add));
        allergens.stream().map(allergen -> allergenService.findById(allergen)).forEach(optional -> optional.ifPresent(allergenList::add));

        dish.get().setProteins(proteinList);
        dish.get().setIngredients(ingredientList);
        dish.get().setAllergens(allergenList);

        if (!file.isEmpty()) {
            dish.get().setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        dishService.save(dish.get()).getId();
        return id;
    }

    @PostMapping("/delete/dish")
    public long deleteDish(@RequestParam long id,
                                  HttpServletRequest request) {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<Dish> dish = dishService.findById(id);
        if (!dish.isPresent()) return -1l;
        dishService.delete(dish.get());
        return id;
    }

    @PostMapping("/create/ingredient")
    public long createIngredient(@RequestParam String name,
                               @RequestParam(required = false) MultipartFile file,
                               @RequestParam(required = false) Long iconId,
                               HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        if (!file.isEmpty()) {
            ingredient.setLogo(iconService.save(createIcon(saveFile(file, ResizeImage.Size.FULL))));
        } else if (iconId != null) {
            ingredient.setLogo(iconService.findById(iconId).get());
        }

        return ingredientService.save(ingredient).getId();
    }

    @PostMapping("/update/ingredient")
    public long updateCategory(@RequestParam long id,
                               @RequestParam String name,
                               @RequestParam(required = false) MultipartFile file,
                               @RequestParam(required = false) Long iconId,
                               HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if (!checkTocken(request.getHeader(AUTHORIZATION))) {
            return -1;
        }

        Optional<Ingredient> ingredient = ingredientService.findById(id);
        if (!ingredient.isPresent()) return -1l;
        if (!file.isEmpty()) {
            ingredient.get().setLogo(iconService.save(createIcon(saveFile(file, ResizeImage.Size.FULL))));
        } else if (iconId != null) {
            ingredient.get().setLogo(iconService.findById(iconId).get());
        }
        ingredientService.save(ingredient.get());
        return id;
    }

    private boolean checkTocken(String auth, boolean checkAccess) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer("auth")
                .build();
        DecodedJWT jwt = verifier.verify(auth);

        if (jwt.getClaim("id").isNull()) return false;
        if (jwt.getClaim("restaurant").isNull()) return false;

        if (!checkAccess && jwt.getClaim("restaurant").asLong() != userService.findById(jwt.getClaim("id").asLong()).get().getRestaurants().stream().findFirst().get().getId()) {
            return true;
        }

        if (checkAccess &&
                jwt.getClaim("restaurant").asLong() == userService.findById(jwt.getClaim("id").asLong()).get().getRestaurants().stream().findFirst().get().getId()) {
            return true;
        }

        if (jwt.getExpiresAt().after(Calendar.getInstance().getTime())) {
            return true;
        }

        return false;
    }

    private boolean checkTocken(String auth) {
        return checkTocken(auth, false);
    }

    private long getRestaurantId(String auth) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .withIssuer("auth")
                .build();
        DecodedJWT jwt = verifier.verify(auth);
        return jwt.getClaim("restaurant").asLong();
    }

    private long getTimeStamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return calendar.getTimeInMillis();
    }

    private void addElement(long restaurantId, String element) {
        notificationService.addElement(restaurantId, element);
    }

    private String getUrl(HttpServletRequest request) {
        String url;
        if (request.getServerName().equalsIgnoreCase(LOCALHOST) || request.getServerName().equalsIgnoreCase("127.0.0.1")) {
            url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        } else {
            url = "https://" + request.getServerName();
        }
        return url;
    }

}
