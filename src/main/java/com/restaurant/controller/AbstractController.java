package com.restaurant.controller;

import com.restaurant.entity.Data;
import com.restaurant.entity.DataWithLogo;
import com.restaurant.service.AbstractService;
import com.restaurant.service.CheckAccess;
import com.restaurant.utils.ResizeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static com.restaurant.utils.ManageFiles.createPhoto;
import static com.restaurant.utils.ManageFiles.saveFile;

public abstract class AbstractController<T extends AbstractService, V extends Data> {

    abstract T repository();

    abstract String prefix();

    protected static final String LIST_STRING = "/list";
    protected static final String ENTITY_STRING = "entity";
    protected static final String UPDATE_STRING = "/update";
    protected static final String RESTAURANT_STRING = "restaurant";
    protected static final String REDIRECT_STRING = "redirect:";
    protected static final String RESTAURANTID_STRING = "restaurantId";
    protected static final String HALLS_STRING = "halls";

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private CheckAccess checkAccess;

    @GetMapping("/add")
    public String showSignUpForm(V entity, @PathParam(value = "id") @Nullable long id, Model model) {
        model.addAttribute(RESTAURANTID_STRING, id);

        return prefix() + "/add";
    }

    @GetMapping("/list")
    public String index(Model model) {
        model.addAttribute("list", repository().findAll());
        return prefix() + LIST_STRING;
    }

    @PostMapping("/add")
    public String add(@Valid V entity, @Valid MultipartFile file, BindingResult result, Model model) throws IOException, NoSuchAlgorithmException {
        if (result.hasErrors()) {
            return prefix() + "/add";
        }

        if (entity instanceof DataWithLogo && !file.isEmpty()) {
            ((DataWithLogo) entity).setLogo(createPhoto(saveFile(file, ResizeImage.Size.LOGO)));
        }
        try {
            repository().save(entity);
        } catch (Exception e) {
            result.addError(new ObjectError("Error", "Такой элемент уже существует"));
            model.addAttribute(RESTAURANTID_STRING, (Long) getHttpSession().getAttribute(RESTAURANT_STRING));
            return prefix() + "/add";
        }
        if (httpSession.getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return httpSession.getAttribute("back") != null ? REDIRECT_STRING + httpSession.getAttribute("back") : prefix() + LIST_STRING;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) throws Throwable {
        V entity = (V) repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute(RESTAURANTID_STRING, getHttpSession().getAttribute(RESTAURANT_STRING));
        model.addAttribute(ENTITY_STRING, entity);
        return prefix() + UPDATE_STRING;
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid V entity,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            entity.setId(id);
            model.addAttribute(RESTAURANTID_STRING, getHttpSession().getAttribute(RESTAURANT_STRING));
            model.addAttribute(ENTITY_STRING, entity);
            return prefix() + UPDATE_STRING;
        }
        try {
            repository().save(entity);
        } catch (Exception e) {
            entity.setId(id);
            model.addAttribute(RESTAURANTID_STRING, getHttpSession().getAttribute(RESTAURANT_STRING));
            model.addAttribute(ENTITY_STRING, entity);
            result.addError(new ObjectError("error", "Ошибка сохранения. Такой элемент уже существует"));
            return prefix() + UPDATE_STRING;
        }
        if (httpSession.getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return httpSession.getAttribute("back") != null ? REDIRECT_STRING + httpSession.getAttribute("back") : prefix() + LIST_STRING;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) throws Throwable {
        V entity = (V) repository().findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid  Id:" + id));
        repository().delete(entity);
        if (httpSession.getAttribute("back") == null) {
            model.addAttribute("list", repository().findAll());
        }
        return httpSession.getAttribute("back") != null ? REDIRECT_STRING + httpSession.getAttribute("back") : prefix() + LIST_STRING;
    }


    public HttpSession getHttpSession() {
        return httpSession;
    }

    protected UserDetails getUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public CheckAccess getCheckAccess() {
        return checkAccess;
    }
}
