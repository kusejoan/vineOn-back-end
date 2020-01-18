/*
 * Copyright (c) 2020.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  3. Neither the name of Vineon nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */

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
     * Ta metoda dodaje ocenę dla istniejącego w bazie danych wina oraz zwraca informację czy została ona dodana
     * poprawnie czy nie, oraz dlaczego. Dodanie oceny drugi raz dla tego samego wina przez tego samego użytkownika
     * powoduje nadpisanie poprzedniej oceny
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
     * Ta metoda zwraca listę wszystkich ocen danego wina o ile istnieje ono w bazie danych. Jeżeli wystąpią jakieś
     * błędy to zwracana jest stosowana informacja
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
                ret = new MultipleGradeReturn(wineGradeModel.findByWine(wine));
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
     * Ta metoda oblicza średnią ocenę danego wina, na podstawie wszystkich jego ocen. Jeśli wino nie istnieje,
     * albo nie było ocenione zwracana jest stosowna informacja
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
                    ret.grade = 0;
                    ret.success = true;
                    ret.message = "No ratings yet";
                    return ret;
                }
                else
                {
                    double sum = wineGradeModel.averageGrade(grades);

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
