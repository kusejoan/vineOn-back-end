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


import app.user.Entity.Wine;
import app.user.Repo.WineRepository;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class WineModelTest {
    private WineRepository wineRepository = mock(WineRepository.class);
    private WineModel wineModel = new WineModelImpl(wineRepository);

    @Test
    public void checkIfRightMethodsFromWineRepositoryAreCalled()
    {
        wineModel.findByName("NAME");
        verify(wineRepository,times(1)).findByWineName("NAME");

        Wine w = new Wine();
        wineModel.save(w);
        verify(wineRepository,times(1)).save(w);

        wineModel.findAll();
        verify(wineRepository,times(1)).findAll();

        wineModel.findByType(w.getType());
        verify(wineRepository,times(1)).findByType(w.getType());

        wineModel.findByColor(w.getColor());
        verify(wineRepository,times(1)).findByColor(w.getColor());

    }

}
