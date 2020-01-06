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
}
