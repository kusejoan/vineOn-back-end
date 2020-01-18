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

package app.user.Model;

import app.user.Entity.User;
import app.user.Entity.Wine;
import app.user.Entity.WineGrade;
import app.user.Repo.WineGradesRepository;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class WineGradeModelTest {
    private WineGradesRepository wineGradesRepository = mock(WineGradesRepository.class);
    private WineGradeModel wineGradeModel = new WineGradeModelImpl(wineGradesRepository);
    @Test
    public void AverageForNoGradesIsMinusOne()
    {
        List<WineGrade> empty = new ArrayList<>();
        assertEquals(wineGradeModel.averageGrade(empty),-1,0);
    }

    @Test
    public void AverageGradeIsCalculatedProperly()
    {
        List<WineGrade> grades = new ArrayList<>();
        WineGrade first = new WineGrade();
        first.setGrade(5L);
        grades.add(first);
        WineGrade second = new WineGrade();
        second.setGrade(6L);
        grades.add(second);
        WineGrade third = new WineGrade();
        third.setGrade(8L);
        grades.add(third);

        assertEquals(wineGradeModel.averageGrade(grades),(5+6+8)/3.0, 0.01);
    }

    @Test
    public void CheckIfRightMethodsFromWineGradesRepositoryAreCalled()
    {
        User u = new User();
        Wine w = new Wine();
        wineGradeModel.findByUserAndWine(u, w);
        verify(wineGradesRepository,times(1)).findByUserAndWine(u,w);

        wineGradeModel.findByUser(u);
        wineGradeModel.findByWine(w);
        verify(wineGradesRepository,times(1)).findByUser(u);
        verify(wineGradesRepository,times(1)).findByWine(w);

        wineGradeModel.findAll();
        verify(wineGradesRepository,times(1)).findAll();

        WineGrade grade = new WineGrade();
        wineGradeModel.save(grade);
        verify(wineGradesRepository,times(1)).save(grade);
    }
}
