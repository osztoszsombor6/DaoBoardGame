package boardgame.model;

import util.jpa.GenericJpaDao;

import javax.persistence.Persistence;
import java.util.List;
import javax.persistence.EntityManagerFactory;

/**
 * DAO class for the {@link GameResult} entity.
 */
public class GameResultDao extends GenericJpaDao<GameResult> {

    private static GameResultDao instance;

    private GameResultDao() {
        super(GameResult.class);
    }

    public static GameResultDao getInstance() {
        instance = new GameResultDao();
        instance.setEntityManager(Persistence.createEntityManagerFactory("BoardGamePU").createEntityManager());
        return instance;
    }

    /**
     * Returns the list of {@code n} latest results
     *
     * @param n the maximum number of results to be returned
     * @return the list of {@code n} latest results
     */
    public List<GameResult> findLatest(int n) {
        return entityManager.createQuery("SELECT r FROM GameResult r ORDER BY r.created DESC", GameResult.class)
                .setMaxResults(n)
                .getResultList();
    }

}
