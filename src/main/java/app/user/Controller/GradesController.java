package app.user.Controller;

import app.user.Controller.helpers.*;
import app.user.Entity.User;
import app.user.Entity.Wine;
import app.user.Entity.WineGrade;
import app.user.Model.SecurityModel;
import app.user.Model.User.CustomerModel;
import app.user.Model.User.UserModel;
import app.user.Model.WineGradeModel;
import app.user.Model.WineModel;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GradesController {
    public GradesController(WineModel wineModel, CustomerModel userModel, SecurityModel securityModel, WineGradeModel wineGradeModel) {
        this.wineModel = wineModel;
        this.userModel = userModel;
        this.securityModel = securityModel;
        this.wineGradeModel = wineGradeModel;
    }

    private WineModel wineModel;
    private UserModel userModel;
    private SecurityModel securityModel;
    private WineGradeModel wineGradeModel;

    /**
     * This method adds a rating for a wine if it exists in database and returns info whether rating was properly
     * added or not and what was the cause
     * @param jsonRating {wineName: , grade: , description: }
     * @return {success: true/false, message: }
     */
    @PostMapping("/user/customer/ratewine")
    public GradeReturn RateWine(@RequestBody String jsonRating)
    {
        GradeReturn ret = new GradeReturn();
        try
        {
            JSONObject jsonObject = JSONGetter.getParams(jsonRating);
            String wineName = jsonObject.getString("wineName");
            Long grade = jsonObject.getLong("grade");
            String description = jsonObject.getString("description");

            if(wineModel.findByName(wineName).isPresent())
            {
                Wine gradedWine = wineModel.findByName(wineName).get();
                User gradingUser = userModel.findByUsername(securityModel.findLoggedInUsername());
                WineGrade wineGrade;

                if(wineGradeModel.findByUserAndWine(gradingUser,gradedWine).isPresent())
                {
                    wineGrade = wineGradeModel.findByUserAndWine(gradingUser,gradedWine).get();
                    wineGrade.setGrade(grade);
                    wineGrade.setDescription(description);
                    ret.setMessage("Rating for " +wineName+" successfully changed");
                }
                else
                {
                    wineGrade = new WineGrade(gradingUser,gradedWine,grade,description);
                    ret.setMessage("Wine " +wineName+" successfully rated");
                }
                wineGradeModel.save(wineGrade);

                ret.setSuccess(true);
            }
            else
            {
                ret.setSuccess(false);
                ret.setMessage("Wine "+wineName+" not found in database");
            }



        }
        catch (Exception e)
        {
                ret.setSuccess(false);
                ret.setMessage("ABC");
                e.printStackTrace();
        }
        return ret;
    }

    /**
     * This method returns list of all ratings for given wine if it exists. If some errors occur on the way it returns
     * info about it.
     * @param jsonRating {wineName}
     * @return { grades: [{id, user, wine, grade, description},{...}], success: true/false }
     */
    @PostMapping("/user/ratingsofwine")
    public MultipleGradeReturn RatingOfWine(@RequestBody String jsonRating)
    {
        MultipleGradeReturn ret = new MultipleGradeReturn();
        try
        {
            JSONObject jsonObject = JSONGetter.getParams(jsonRating);
            String wineName = jsonObject.getString("wineName");

            if(wineModel.findByName(wineName).isPresent())
            {
                Wine wine = wineModel.findByName(wineName).get();
                ret.grades = wineGradeModel.findByWine(wine);
                ret.success = true;
            }
            else
            {
                ret.grades = null;
                ret.success = false;
            }
            return ret;
        }
        catch (Exception e)
        {
           ret.grades = null;
           ret.success = false;
           return ret;
        }
    }

    /**
     * This method calculates average grade for given wine. If it does not exist or has no grades
     * information about it is returned
     * @param jsonRating {wineName}
     * @return {grade, amountOfGrades, success: True/False, message}
     */
    @PostMapping("/user/averagerating")
    public AverageGradeReturn AverageRating(@RequestBody String jsonRating)
    {
        AverageGradeReturn ret = new AverageGradeReturn();
        try
        {
            JSONObject jsonObject = JSONGetter.getParams(jsonRating);
            String wineName = jsonObject.getString("wineName");

            if(wineModel.findByName(wineName).isPresent())
            {
                Wine wine = wineModel.findByName(wineName).get();

                List<WineGrade> grades = wineGradeModel.findByWine(wine);

                if(grades.size()==0)
                {
                    ret.amountOfGrades = 0;
                    ret.grade = -1;
                    ret.success = true;
                    ret.message = "No ratings yet";
                    return ret;
                }
                else
                {
                    double sum = 0;
                    for(WineGrade g: grades)
                    {
                        sum += g.getGrade();
                    }
                    sum = sum/grades.size();

                    ret.grade = sum;
                    ret.amountOfGrades = grades.size();
                    ret.success = true;
                    ret.message = "OK";
                    return ret;
                }
            }
            else
            {
                ret.success = false;
                ret.amountOfGrades = -1;
                ret.grade = -1;
                ret.message = "Wine "+wineName+" not found in database";
                return ret;
            }
        }
        catch (Exception ex)
        {
            ret.success = false;
            ret.amountOfGrades = -1;
            ret.grade = -1;
            ret.message = ex.getMessage();

            return ret;
        }
    }
}
