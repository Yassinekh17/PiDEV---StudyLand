package services;

import java.sql.SQLException;
import java.util.List;

public interface EvaluationService<T> {

    public int ajouter(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public void supprimer(T t) throws SQLException;
    public List<T> afficher()throws SQLException ;
}
