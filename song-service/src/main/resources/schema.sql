CREATE TABLE IF NOT EXISTS songs (
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    artist VARCHAR(50) NOT NULL,
    label VARCHAR(50) NOT NULL,
    released INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS playlists (
    id SERIAL PRIMARY KEY,
    owner_id VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    is_private BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS playlists_songs (
    playlist INTEGER REFERENCES playlists(id),
    song INTEGER REFERENCES songs(id) ON DELETE CASCADE,
    PRIMARY KEY (playlist, song)
);