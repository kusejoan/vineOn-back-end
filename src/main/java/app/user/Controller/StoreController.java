package app.user.Controller;

import app.user.Entity.Store;
import app.user.Model.SecurityModel;
import app.user.Model.User.StoreModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreController  {
    private StoreModel storeModel;
    private SecurityModel securityModel;

    public StoreController(StoreModel storeModel, SecurityModel securityModel) {
        this.storeModel = storeModel;
        this.securityModel = securityModel;
    }

    @PostMapping("/user/store/update")
    public String UpdateProfile(@RequestBody String profileJSON)
    {
        String name = securityModel.findLoggedInUsername();
        Store profile = storeModel.findByUsername(name);

        profile.setCity("KRAKOW");

        storeModel.save(profile);
        return "WORKS";
    }


}

