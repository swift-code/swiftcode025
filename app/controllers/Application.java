package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forms.LoginForm;
import forms.SignupForm;
import models.Profile;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by lubuntu on 8/21/16.
 */
public class Application extends Controller {
    @Inject
    FormFactory FormFactory;

    @Inject
    ObjectMapper objectMapper;

    public Result login(){
        Form<LoginForm> form = FormFactory.form(LoginForm.class).bindFromRequest();
        if(form.hasErrors()) {
            return ok(form.errorsAsJson());
        }
        return ok();
    }

    public Result signup() {

        Form<SignupForm>  form = FormFactory.form(SignupForm.class).bindFromRequest();
        if(form.hasErrors()) {
            return ok(form.errorsAsJson());
        }
        Profile profile = new Profile(form.data().get("firstname"), form.data().get("lastname"));
        Profile.db().save(profile);

        User user = new User(form.data().get("email"), form.data().get("password"));
        user.profile = profile;
        User.db().save(user);

        return ok((JsonNode) objectMapper.valueToTree(user));
    }

}
