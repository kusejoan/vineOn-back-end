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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WineGradeModelImpl implements WineGradeModel {

    public WineGradeModelImpl(WineGradesRepository wineGradesRepository) {
        this.wineGradesRepository = wineGradesRepository;
    }

    private WineGradesRepository wineGradesRepository;
    @Override
    public void save(WineGrade wineGrade) {
        wineGradesRepository.save(wineGrade);
    }

    @Override
    public List<WineGrade> findAll() {
        return wineGradesRepository.findAll();
    }

    @Override
    public List<WineGrade> findByWine(Wine wine) {
        return wineGradesRepository.findByWine(wine);
    }

    @Override
    public List<WineGrade> findByUser(User user) {
        return wineGradesRepository.findByUser(user);
    }

    @Override
    public Optional<WineGrade> findByUserAndWine(User user, Wine wine) {
        return wineGradesRepository.findByUserAndWine(user,wine);
    }

    /**
     *
     * @param grades Lista ocen z których ma zostać wyliczona średnia
     * @return Jeżeli nie ma ocen, zwracana jest wartość -1, w przeciwnym razie średnia arytmetyczna wszystkich ocen z listy
     */
    @Override
    public double averageGrade(List<WineGrade> grades) {
        double sum = 0;
        if(grades.size()==0)
        {
            return -1;
        }
        for(WineGrade g: grades)
        {
            sum += g.getGrade();
        }
        sum = sum/grades.size();

        return sum;
    }
}
