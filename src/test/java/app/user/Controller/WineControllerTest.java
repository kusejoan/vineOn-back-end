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

import app.user.Controller.helpers.MultipleStoresReturn;
import app.user.Controller.helpers.MultipleWinesReturn;
import app.user.Controller.helpers.WineReturn;
import app.user.Entity.Store;
import app.user.Entity.Wine;
import app.user.Model.WineModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.junit.Assert.*;

public class WineControllerTest {

    @Mock
    private WineModel mockWineModel;

    private WineController wineControllerUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        wineControllerUnderTest = new WineController(mockWineModel);
    }

    @Test
    public void testAdd() {
        // Setup

        // Configure WineModel.findByName(...).
        final Optional<Wine> wine = Optional.of(new Wine("wineName", "country", 1993L, "color", "type"));
        when(mockWineModel.findByName("wineName")).thenReturn(wine);

        String JSON = "{\"params\": {\n" +
                "        \"wineName\": \"wineName\",\n" +
                "        \"country\": \"country\",\n" +
                "        \"color\": \"color\",\n" +
                "        \"type\": \"type\",\n" +
                "        \"year\": 1993,\n" +
                "        \"description\": \"Opis wina 1\"\n" +
                "    }}";
        // Run the test
        final WineReturn result = wineControllerUnderTest.add(JSON);

        // Verify the results
        verify(mockWineModel,times(0)).save(new Wine("wineName", "country", 1993L, "color", "type"));
        assertFalse(result.success);
        assertEquals("Wine wineName already is in database",result.message);

        String JSON2 = "{\"params\": {\n" +
                "        \"wineName\": \"otherName\",\n" +
                "        \"country\": \"country\",\n" +
                "        \"color\": \"color\",\n" +
                "        \"type\": \"type\",\n" +
                "        \"year\": 1993,\n" +
                "        \"description\": \"Opis wina 1\"\n" +
                "    }}";
        final WineReturn result2 = wineControllerUnderTest.add(JSON2);

        verify(mockWineModel).save(new Wine("otherName", "country", 1993L, "color", "type"));
    }

    @Test
    public void testGetAllWines() {
        // Setup

        // Configure WineModel.findAll(...).
        final List<Wine> wines = Arrays.asList(new Wine("wineName", "country", 0L, "color", "type"));
        when(mockWineModel.findAll()).thenReturn(wines);

        // Run the test
        final MultipleWinesReturn result = wineControllerUnderTest.getAllWines();

        assertTrue(result.success);
        assertEquals(wines.size(),1);
    }

    @Test
    public void testGetStoresOfWine() {
        // Setup

        // Configure WineModel.findByName(...).
        Wine w = new Wine("wineName", "country", 0L, "color", "type");
        w.addStore(new Store());
        w.addStore(new Store());
        final Optional<Wine> wine = Optional.of(w);
        when(mockWineModel.findByName("wineName")).thenReturn(wine);

        String JSON = "{'params': {'wineName': \"wineName\"}}";
        // Run the test
        final MultipleStoresReturn result = wineControllerUnderTest.getStoresOfWine(JSON);

        assertTrue(result.success);
        assertEquals(result.stores.size(),w.getStore().size());
    }

    @Test
    public void testSearchWine() {
        // Setup

        // Configure WineModel.findByName(...).
        final Optional<Wine> wine = Optional.of(new Wine("wineName", "country", 0L, "color", "type"));
        when(mockWineModel.findByName("wineName")).thenReturn(wine);

        // Configure WineModel.findByColorAndType(...).
        final List<Wine> wines = Arrays.asList(new Wine("wineName", "country", 0L, "color", "type"));
        when(mockWineModel.findByColorAndType("color", "type")).thenReturn(wines);

        // Configure WineModel.findByColor(...).
        final List<Wine> wines1 = Arrays.asList(new Wine("wineName", "country", 0L, "color", "type"));
        when(mockWineModel.findByColor("color")).thenReturn(wines1);

        // Configure WineModel.findByType(...).
        final List<Wine> wines2 = Arrays.asList(new Wine("wineName", "country", 0L, "color", "type"));
        when(mockWineModel.findByType("type")).thenReturn(wines2);

        String JSONname = "{'params': {'wineName': \"wineName\"}}";
        String JSONtype = "{'params': {'type': \"type\"}}";
        String JSONcolor = "{'params': {'color': \"color\"}}";
        String JSONcolorandtype = "{'params': {'color': \"color\",'type': \"type\"}}";

        // Run the test
        final MultipleWinesReturn resultName = wineControllerUnderTest.searchWine(JSONname);
        assertTrue(resultName.success);
        final MultipleWinesReturn resultType = wineControllerUnderTest.searchWine(JSONtype);
        final MultipleWinesReturn resultColor = wineControllerUnderTest.searchWine(JSONcolor);
        final MultipleWinesReturn resultColorAndType = wineControllerUnderTest.searchWine(JSONcolorandtype);

        // Verify the results
    }
}
