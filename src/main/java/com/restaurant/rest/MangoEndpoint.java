package com.restaurant.rest;

import com.restaurant.dto.CallDto;
import com.restaurant.entity.Restaurant;
import com.restaurant.service.AllergenService;
import com.restaurant.service.CategoryService;
import com.restaurant.service.DishService;
import com.restaurant.service.IngredientService;
import com.restaurant.service.ProteinService;
import com.restaurant.service.RestaurantService;
import com.restaurant.service.SubCategoryService;
import com.restaurant.utils.DtoConverter;
import io.swagger.annotations.Api;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.restaurant.rest.MobileEndpoint.addElement;

@RestController
@RequestMapping("/mango")
@Api(value = "MangoEndpoint")
public class MangoEndpoint {

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

    private static final int LIMIT = 30;

    Map<String, String> map = new ConcurrentHashMap<>();


    @PostMapping(value = "/events/call")
    public void eventsCall(
            @RequestParam(name = "vpbx_api_key") String key,
            @RequestParam(name = "sign") String sign,
            @RequestParam(name = "json") String json,
//            @RequestParam(name = "entry_id") String entryId,
//                           @RequestParam(name = "call_id") String callId,
//                           @RequestParam(name = "timestamp") long timestamp,
//                           @RequestParam(name = "seq") long seq,
//                           @RequestParam(name = "call_state") String callState,
//                           @RequestParam(name = "location") String location,
                           HttpServletRequest request) {

        JSONObject obj = new JSONObject(json);
        JSONObject from = obj.getJSONObject("from");
        String fromNumber = from.getString("number");
        JSONObject to = obj.getJSONObject("to");
        String toNumber = from.getString("number");

        if (map.get(fromNumber) == null) {
            map.put(fromNumber, toNumber);
            CallDto callDto = new CallDto();
            callDto.setCall(true);
            callDto.setPhone(fromNumber);
            Restaurant restaurant = restaurantService.findByPhone(fromNumber);
            if (restaurant != null) {
                addElement(restaurant.getId(), new JSONObject(callDto).toString());
            }
        }

    }

    @PostMapping(value = "/events/sms")
    public void eventsSms(
            @RequestParam(name = "vpbx_api_key") String key,
            @RequestParam(name = "sign") String sign,
            @RequestParam(name = "json") String json,
//            @RequestParam(name = "command_id") String commandId,
//                           @RequestParam(name = "timestamp") long timestamp,
//                           @RequestParam(name = "reason") long reason,
                           HttpServletRequest request) {

    }

    @PostMapping(value = "/events/recording")
    public void eventsRecording(
            @RequestParam(name = "vpbx_api_key") String key,
            @RequestParam(name = "sign") String sign,
            @RequestParam(name = "json") String json,
//                           @RequestParam(name = "recording_id") String recordingId,
//                           @RequestParam(name = "recording_state") String recordingState,
//                           @RequestParam(name = "seq") long seq,
//                           @RequestParam(name = "entry_id") long entyId,
//                           @RequestParam(name = "call_id") String callId,
//                           @RequestParam(name = "extension") long extension,
//                           @RequestParam(name = "timestamp") long timestamp,
//                           @RequestParam(name = "completion_code") String completionCode,
//                           @RequestParam(name = "recipient") String recipient,
//                           @RequestParam(name = "command_id") String commandId,
                           HttpServletRequest request) {

    }

    @PostMapping(value = "/events/dtmf")
    public void eventsDtmf(
            @RequestParam(name = "vpbx_api_key") String key,
            @RequestParam(name = "sign") String sign,
            @RequestParam(name = "json") String json,
//            @RequestParam(name = "seq") long seq,
//                           @RequestParam(name = "dtmf") long dtmf,
//                           @RequestParam(name = "timestamp") long timestamp,
//                           @RequestParam(name = "call_id") String callId,
//                           @RequestParam(name = "entry_id") String entryId,
//                           @RequestParam(name = "location") String location,
//                           @RequestParam(name = "initiator") String initiator,
//                           @RequestParam(name = "from_number") String fromNumber,
//                           @RequestParam(name = "to_number") String toNumber,
//                           @RequestParam(name = "line_number") String lineNumber,
                           HttpServletRequest request) {

    }

    @PostMapping(value = "/events/summary")
    public void eventsSummary(
            @RequestParam(name = "vpbx_api_key") String key,
            @RequestParam(name = "sign") String sign,
            @RequestParam(name = "json") String json,
//    public void eventsSummary(@RequestParam(name = "entry_id") long entryId,
//                           @RequestParam(name = "call_direction") long callDirectin,
//                           @RequestParam(name = "from") long from,
//                           @RequestParam(name = "to") String to,
//                           @RequestParam(name = "line_number") String lineNumber,
//                           @RequestParam(name = "dct") String dct,
//                           @RequestParam(name = "create_time") String createTime,
//                           @RequestParam(name = "forward_time") String forwardTime,
//                           @RequestParam(name = "talk_time") String talkNumber,
//                           @RequestParam(name = "end_time") String endTime,
//                           @RequestParam(name = "entry_result") String entryResult,
//                           @RequestParam(name = "disconnect_reason") String disconnectReason,
                           HttpServletRequest request) {

        JSONObject obj = new JSONObject(json);
        JSONObject from = obj.getJSONObject("from");
        String fromNumber = from.getString("number");
        JSONObject to = obj.getJSONObject("to");
        String toNumber = from.getString("number");

        if (map.get(fromNumber) != null) {

            map.remove(fromNumber);
            CallDto callDto = new CallDto();
            callDto.setCall(true);
            callDto.setPhone(fromNumber);
            Restaurant restaurant = restaurantService.findByPhone(fromNumber);
            if (restaurant != null) {
                addElement(restaurant.getId(), new JSONObject(callDto).toString());
            }
        }

    }

}
