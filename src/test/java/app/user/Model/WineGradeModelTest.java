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
