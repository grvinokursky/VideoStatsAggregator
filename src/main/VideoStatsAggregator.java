import videostats.services.vk.VkVideoService;

public class VideoStatsAggregator {
    public static void main(String[] args) {
        // Получаем ключ API для VK из переменных окружения
        String vkApiKey = System.getenv("VK_API_KEY");
        VkVideoService vkVideoService = new VkVideoService(vkApiKey);  // Создаем сервис для VK

        // Пример использования сервиса для получения статистики
        String videoId = "123456789";  // Пример ID видео
        long views = vkVideoService.getVideoViews(videoId);  // Получаем количество просмотров

        System.out.println("Видео ID " + videoId + " имеет " + views + " просмотров.");
    }
}

