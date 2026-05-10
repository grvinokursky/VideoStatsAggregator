package videostats.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.SelectionQuery;
import videostats.model.VideoStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class VideoStatsRepository {

    private final SessionFactory sessionFactory;

    public VideoStatsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(VideoStats videoStats) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            if (videoStats.getUpdatedAt() == null) {
                videoStats.setUpdatedAt(LocalDateTime.now());
            }

            VideoStats existing = findByPlatformAndVideoId(
                session, 
                videoStats.getPlatform(), 
                videoStats.getVideoId()
            );

            if (existing != null) {
                existing.setViewCount(videoStats.getViewCount());
                existing.setUpdatedAt(LocalDateTime.now());
                session.merge(existing);
            } else {
                session.persist(videoStats);
            }

            tx.commit();

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Ошибка сохранения статистики видео: " + e.getMessage(), e);
        }
    }

    public List<VideoStats> findAll() {
        try (Session session = sessionFactory.openSession()) {
            SelectionQuery<VideoStats> query = session.createSelectionQuery(
                "SELECT vs FROM VideoStats vs", VideoStats.class
            );
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения всех записей статистики", e);
        }
    }

    public Optional<VideoStats> findByPlatformAndVideoId(String platform, String videoId) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(findByPlatformAndVideoId(session, platform, videoId));
        } catch (Exception e) {
            throw new RuntimeException(
                String.format("Ошибка поиска статистики для platform=%s, videoId=%s", platform, videoId), 
                e
            );
        }
    }

    public int updateViewCount(String platform, String videoId, long newViewCount) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            MutationQuery query = session.createMutationQuery(
                "UPDATE VideoStats vs SET vs.viewCount = :viewCount, vs.updatedAt = :updatedAt " +
                "WHERE vs.platform = :platform AND vs.videoId = :videoId"
            );
            query.setParameter("viewCount", newViewCount);
            query.setParameter("updatedAt", LocalDateTime.now());
            query.setParameter("platform", platform);
            query.setParameter("videoId", videoId);
            int updatedCount = query.executeUpdate();

            tx.commit();
            return updatedCount;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException(
                String.format("Ошибка обновления просмотров для platform=%s, videoId=%s", platform, videoId), 
                e
            );
        }
    }

    private VideoStats findByPlatformAndVideoId(Session session, String platform, String videoId) {
        SelectionQuery<VideoStats> query = session.createSelectionQuery(
            "SELECT vs FROM VideoStats vs WHERE vs.platform = :platform AND vs.videoId = :videoId",
            VideoStats.class
        );
        query.setParameter("platform", platform);
        query.setParameter("videoId", videoId);
        return query.uniqueResult();
    }
}
