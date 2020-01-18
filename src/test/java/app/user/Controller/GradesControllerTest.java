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

import app.user.Controller.helpers.AverageGradeReturn;
import app.user.Controller.helpers.GradeReturn;
import app.user.Controller.helpers.MultipleGradeReturn;
import app.user.Entity.Customer;
import app.user.Entity.User;
import app.user.Entity.Wine;
import app.user.Entity.WineGrade;
import app.user.Model.SecurityModel;
import app.user.Model.User.CustomerModel;
import app.user.Model.WineGradeModel;
import app.user.Model.WineModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GradesControllerTest {

    @Mock
    private WineModel mockWineModel;
    @Mock
    private CustomerModel mockUserModel;
    @Mock
    private SecurityModel mockSecurityModel;
    @Mock
    private WineGradeModel mockWineGradeModel;

    private GradesController gradesControllerUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        gradesControllerUnderTest = new GradesController(mockWineModel, mockUserModel, mockSecurityModel, mockWineGradeModel);
    }

    @Test
    public void testRateWine() {
        // Setup

        Wine w = new Wine("wineName", "country", 0L, "color", "type");
        Customer c = new Customer(new User("username", "password", "role"));
        // Configure WineModel.findByName(...).
        final Optional<Wine> wine = Optional.of(w);
        when(mockWineModel.findByName("wineName")).thenReturn(wine);

        when(mockUserModel.findByUsername("username")).thenReturn(c);
        when(mockSecurityModel.findLoggedInUsername()).thenReturn("result");

        // Configure WineGradeModel.findByUserAndWine(...).
        WineGrade grade = new WineGrade(new User("username", "password", "role"),
                new Wine("wineName", "country", 0L, "color", "type"), 0L, "description");
        final Optional<WineGrade> wineGrade = Optional.of(grade);
        when(mockWineGradeModel.findByUserAndWine(any(), any())).thenReturn(wineGrade);

        // Run the test
        String jsonRating = "{\"params\": {\n" +
                "        \"wineName\": wineName,\n" +
                "        \"grade\": 6,\n" +
                "        \"description\": \"Opis wina 1\"\n" +
                "    }}";
        final GradeReturn result = gradesControllerUnderTest.RateWine(jsonRating);

        assertTrue(result.success);

        // Verify the results
        verify(mockWineGradeModel).save(grade);
    }

    @Test
    public void testRatingOfWine() {
        // Setup

        // Configure WineModel.findByName(...).
        final Optional<Wine> wine = Optional.of(new Wine("wineName", "country", 0L, "color", "type"));
        when(mockWineModel.findByName("wineName")).thenReturn(wine);

        // Configure WineGradeModel.findByWine(...).
        final List<WineGrade> grades = Arrays.asList(new WineGrade(new User("username", "password", "role"), new Wine("wineName", "country", 0L, "color", "type"), 0L, "description"));
        when(mockWineGradeModel.findByWine(new Wine("wineName", "country", 0L, "color", "type"))).thenReturn(grades);


        String JSON = "{'params': {'wineName': \"wineName\"}}";
        // Run the test
        final MultipleGradeReturn result = gradesControllerUnderTest.RatingOfWine(JSON);

        assertTrue(result.success);

        // Verify the results
    }

    @Test
    public void testAverageRating() {
        // Setup

        // Configure WineModel.findByName(...).
        final Optional<Wine> wine = Optional.of(new Wine("wineName", "country", 0L, "color", "type"));
        when(mockWineModel.findByName("wineName")).thenReturn(wine);

        // Configure WineGradeModel.findByWine(...).
        final List<WineGrade> grades = Arrays.asList(new WineGrade(new User("username", "password", "role"), new Wine("wineName", "country", 0L, "color", "type"), 3L, "description"));
        when(mockWineGradeModel.findByWine(new Wine("wineName", "country", 0L, "color", "type"))).thenReturn(grades);

        when(mockWineGradeModel.averageGrade(Arrays.asList(new WineGrade(new User("username", "password", "role"),
                new Wine("wineName", "country", 0L, "color", "type"), 3L, "description")))).thenReturn(3.0);

        String JSON = "{'params': {'wineName': \"wineName\"}}";
        // Run the test
        final AverageGradeReturn result = gradesControllerUnderTest.AverageRating(JSON);

        assertTrue(result.success);
        assertEquals(result.amountOfGrades,1);
        assertEquals(result.grade,3.0,0.0);

        // Verify the results
    }
}
