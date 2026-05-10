CREATE TABLE IF NOT EXISTS video_stats (
    id SERIAL PRIMARY KEY,
    video_id VARCHAR(100) NOT NULL,
    platform VARCHAR(100) NOT NULL,
    view_count BIGINT NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
