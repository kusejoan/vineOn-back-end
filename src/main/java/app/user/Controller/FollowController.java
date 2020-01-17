package app.user.Controller;

import app.user.Controller.helpers.FollowReturn;
import app.user.Controller.helpers.JSONGetter;
import app.user.Controller.helpers.MultipleWinesReturn;
import app.user.Entity.*;
import app.user.Model.FollowModel;
import app.user.Model.SecurityModel;
import app.user.Model.User.UserModel;
import app.user.Model.User.UserModelImpl;
import app.user.Model.WineGradeModel;
import org.javatuples.Pair;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class FollowController {
    private FollowModel followModel;
    private SecurityModel securityModel;
    private UserModel userModel;
    private WineGradeModel wineGradeModel;

    public FollowController(FollowModel followModel, SecurityModel securityModel, UserModelImpl userModel, WineGradeModel wineGradeModel) {
        this.followModel = followModel;
        this.securityModel = securityModel;
        this.userModel = userModel;
        this.wineGradeModel = wineGradeModel;
    }

    /**
     * Ta metoda służy do dodawania użytkownika do listy obserwowanych. Jeżeli szukany użytkownik nie istnieje, bądź próbujemy
     * obserwować samych siebie zwracany jest stosowny komunikat oraz flaga success ustawiana jest na false.
     * @param followJSON
     * {
     *     username:
     * }
     * @return
     * {
     *     success: true/false
     *     message:
     * }
     */

    @PostMapping("/user/follow")
    public FollowReturn follow(@RequestBody String followJSON)
    {
        FollowReturn ret = new FollowReturn();
        try
        {
            JSONObject jsonObject = JSONGetter.getParams(followJSON);
            String username = jsonObject.getString("username");
            System.out.println(username);
            User toFollow = userModel.findByUsername(username);
            User follower = userModel.findByUsername(securityModel.findLoggedInUsername());

            ret.success =  followModel.follow(follower,toFollow);
            if(ret.success)
            {
                if(toFollow instanceof Store)
                {
                    ret.message = "You started following " + ((Store)toFollow).getStoreName();
                }
            }
            else
            {
                ret.message = "User "+toFollow+" does not exist";
            }
        }
        catch (Exception ex)
        {
                ret.message = ex.getMessage();
                ret.success = false;
        }
        return ret;

    }

    /**
     * Ta metoda służy do usuwania użytkownika do listy obserwowanych. Jeżeli szukany użytkownik nie istnieje, nie jest
     * przez nas obserwowany bądź próbujemy obserwować samych siebie zwracany jest stosowny komunikat
     * oraz flaga success ustawiana jest na false.
     * @param unfollowJSON
     * {
     *     username:
     * }
     * @return
     * {
     *     success: true/false
     *     message:
     * }
     */
    @PostMapping("/user/unfollow")
    public FollowReturn unfollow(@RequestBody String unfollowJSON)
    {
        FollowReturn ret = new FollowReturn();
        try
        {
            JSONObject jsonObject = JSONGetter.getParams(unfollowJSON);
            String username = jsonObject.getString("username");
            System.out.println(username);
            User toFollow = userModel.findByUsername(username);
            User follower = userModel.findByUsername(securityModel.findLoggedInUsername());

            ret.success =  followModel.unfollow(follower,toFollow);
            if(ret.success)
            {
                ret.message = "You stopped following "+toFollow.getUsername();
            }
            else
            {
                ret.message = "User "+toFollow+" does not exist";
            }
        }
        catch (Exception ex)
        {
            ret.message = ex.getMessage();
            ret.success = false;ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Ta metoda zwraca listę rekomendowanych dla użytkownika win bazując na ocenach innych użytkowników oraz na kryteriach,
     * które zostały przesłane jako parametry. Każdy z parametrów zapytania jest opcjonalny i gdy go nie wyślemy to wykonuje
     * się ono dla wartości domyślnych czyli wyświetla 3 rekomendacje na podstawie wszystkich dostępnych ocen wszystkich win.
     * Parametryzować można ilość wyników, to czy brać pod uwagę tylko oceny osób obserwowanych oraz kolor wina i kraj pochodzenia.
     * Jeżeli żadne wina nie spełniają kryteriów, zwracana jest stosowna informacja
     * @param wineJSON
     * {
     *     onlyFollowed: true/false
     *     limit:
     *     color:
     *     country:
     * }
     * @return
     * {
     *     wines: [
     *            ...
     *            ]
     *     success: true/false
     * }
     */

    @PostMapping("/user/customer/recommendations")
    public MultipleWinesReturn Recommendations(@RequestBody String wineJSON)
    {
        MultipleWinesReturn ret = new MultipleWinesReturn();
        User me = userModel.findByUsername(securityModel.findLoggedInUsername());
        boolean onlyFollowed = false;
        int limit = 3;
        String color = null;
        String country = null;

        try
        {
            JSONObject json = JSONGetter.getParams(wineJSON);
            if(json.has("limit"))
            {
                limit = json.getInt("limit");
            }
            if(json.has("onlyFollowed"))
            {
                onlyFollowed = json.getBoolean("onlyFollowed");
            }
            if(json.has("color"))
            {
                color = json.getString("color");
            }
            if(json.has("country"))
            {
                country = json.getString("country");
            }
        }
        catch (Exception ex)
        {
            ret.setSuccess(false);
            ret.setMessage(ex.getMessage());
            return ret;
        }
        //JSON PARAMS


        List<Customer> customers;
        List<WineGrade> grades;
        if(onlyFollowed)
        {
            customers = followModel.getAllUsersFollowedBy(me, Customer.class);
            grades = new ArrayList<>();
            for(Customer c: customers)
            {
                grades.addAll(wineGradeModel.findByUser(c));
            }
        }
        else
        {
            grades = wineGradeModel.findAll();
            grades.removeIf(c -> c.getUser() == me);
        }


        Set<Wine> uniqueWines = new HashSet<>();

        for(WineGrade grade: grades)
        {
            if((grade.getWine().getColor().equals(color) || color == null) &&
               (grade.getWine().getCountry().equals(color) || country == null))
            {
                uniqueWines.add(grade.getWine());
            }
        }
        int numberOfWines = uniqueWines.size();
        if(0==numberOfWines)
        {
            ret.success = false;
            ret.wines = null;
            ret.message = "There are no wines that match criteria";
        }

        if(limit > numberOfWines)
        {
            limit = numberOfWines;
        }
        List<Pair<Double, Wine>> winesWithAvgGrade = new ArrayList<>();
        for(Wine w: uniqueWines)
        {
            double avg = wineGradeModel.averageGrade(grades.stream().filter(c -> c.getWine() == w).collect(Collectors.toList()));
            winesWithAvgGrade.add(new Pair<>(avg,w));
        }

        winesWithAvgGrade.sort(Comparator.comparing(Pair::getValue0,Comparator.reverseOrder()));

        ret.wines = new ArrayList<>();
        for(int i=0; i<limit; i++)
        {
            ret.wines.add(winesWithAvgGrade.get(i).getValue1());
        }
        ret.success = true;

        return ret;



    }
}
