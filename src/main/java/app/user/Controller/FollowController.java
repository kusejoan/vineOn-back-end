package app.user.Controller;

import app.user.Controller.helpers.JSONGetter;
import app.user.Entity.User;
import app.user.Model.FollowModel;
import app.user.Model.SecurityModel;
import app.user.Model.User.UserModel;
import app.user.Model.User.UserModelImpl;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FollowController {
    private FollowModel followModel;
    private SecurityModel securityModel;
    private UserModel userModel;

    public FollowController(FollowModel followModel, SecurityModel securityModel, UserModelImpl userModel) {
        this.followModel = followModel;
        this.securityModel = securityModel;
        this.userModel = userModel;
    }

    @PostMapping("/user/follow")
    public boolean follow(@RequestBody String followJSON)
    {
        try
        {
            JSONObject jsonObject = JSONGetter.getParams(followJSON);
            String username = jsonObject.getString("username");
            System.out.println(username);
            User toFollow = userModel.findByUsername(username);
            User follower = userModel.findByUsername(securityModel.findLoggedInUsername());

            return followModel.follow(follower,toFollow);
        }
        catch (Exception ex)
        {
                ex.printStackTrace();
                return false;
        }

    }
}
