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
