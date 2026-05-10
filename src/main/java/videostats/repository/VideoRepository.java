package videostats.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import videostats.model.Video;
import videostats.model.VideoStats;

public class VideoRepository {

    private final SessionFactory sessionFactory;

    public VideoRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Video video) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            Video existing = (Video) session
                .createSelectionQuery("FROM Video WHERE url = :url", Video.class)
                .setParameter("url", video.getUrl())
                .uniqueResult();

            if (existing != null) {
                existing.setVideoId(video.getVideoId());
                existing.setPlatform(video.getPlatform());
                existing.setUserId(video.getUserId());
                session.merge(existing);
            } else {
                session.persist(video);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Ошибка сохранения видео", e);
        }
    }

    public List<Video> findByUserId(long userId, VideoStatsRepository videoStatsRepository) {
        try (Session session = sessionFactory.openSession()) {
            var videos = session
                .createQuery("FROM Video WHERE userId = :userId", Video.class)
                .setParameter("userId", userId)
                .list();
            videos.stream()
                .forEach(video ->
                    video.setVideoStats(
                        videoStatsRepository.findByPlatformAndVideoId(
                            video.getPlatform(),
                            video.getVideoId()
                        ).get()
                    )
                    
                );
            return videos;
        }
    }
}
