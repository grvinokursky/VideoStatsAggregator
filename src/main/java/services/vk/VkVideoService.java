package videostats.services.vk;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.queries.video.VideoGetStatsQuery;

public class VkVideoService {
    private final String apiKey;

    public VkVideoService(String apiKey) {
        this.apiKey = apiKey;
    }

    public long getVideoViews(String videoId) {
        try {
            // Инициализация VK API клиента
            VK vk = VK.getInstance();
            GroupActor actor = new GroupActor(0, apiKey);  // Создаем актера с токеном

            // Запрос статистики по видео
            VideoGetStatsQuery statsQuery = vk.video().getStats(actor, Long.parseLong(videoId));
            long views = statsQuery.execute().getCount();  // Получаем количество просмотров

            System.out.println("Просмотров: " + views);
            return views;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
