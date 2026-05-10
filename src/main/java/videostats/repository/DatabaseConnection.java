package videostats.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import videostats.model.Video;
import videostats.model.VideoStats;

public class DatabaseConnection {
    private SessionFactory sessionFactory;

    private static DatabaseConnection instance;


    private DatabaseConnection(String url, String user, String password) {
        try {
            Configuration configuration = new Configuration();
            
            configuration.addAnnotatedClass(Video.class);
            configuration.addAnnotatedClass(VideoStats.class);
            
            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", url);
            configuration.setProperty("hibernate.connection.username", user);
            configuration.setProperty("hibernate.connection.password", password);
            
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            var serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
            
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            
        } catch (Exception e) {
            System.err.println("Ошибка инициализации SessionFactory: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory(String url, String user, String password) {
        if (instance == null) {
            instance = new DatabaseConnection(url, user, password);
        }
        return instance.sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        if (instance == null) {
            throw new IllegalStateException("DatabaseConnection not initialized.");
        }
        return instance.sessionFactory;
    }

    public static void shutdown() {
        if (instance != null && instance.sessionFactory != null && !instance.sessionFactory.isClosed()) {
            instance.sessionFactory.close();
        }
    }
}
