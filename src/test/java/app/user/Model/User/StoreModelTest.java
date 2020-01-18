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

package app.user.Model.User;

import app.user.Entity.Store;
import app.user.Entity.Wine;
import app.user.Repo.StoreRepository;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class StoreModelTest {
    private StoreRepository storeRepository = mock(StoreRepository.class);
    private StoreModel storeModel = new StoreModelImpl(storeRepository);

    @Test
    public void checkIfModelCallsRightRepoMethods()
    {
        storeModel.findByStorename("ABC");
        verify(storeRepository,times(1)).findByStoreName("ABC");

        storeModel.findAll();
        verify(storeRepository,times(1)).findAll();

        storeModel.findStoresOfCity("CITY");
        verify(storeRepository,times(1)).findByCity("CITY");


        storeModel.findByUsername("USERNAME");
        verify(storeRepository,times(1)).findByUsername("USERNAME");

        storeModel.findById(1L);
        verify(storeRepository,times(1)).findById(1L);

        Store s = new Store();
        storeModel.save(s);
        verify(storeRepository,times(1)).save(s);

    }
    @Test
    public void checkIfWinesAreProperlyAddedToStore()
    {
        Store s = new Store();
        Wine w  = new Wine();
        w.setWineName("ABC");

        Wine w2 = new Wine();
        w.setWineName("CBA");

        storeModel.addWine(s,w);
        storeModel.addWine(s,w2);

        assertEquals(s.getWines().size(),2);
    }
}
