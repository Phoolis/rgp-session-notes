-- Insert mock admin user with hashed password --
INSERT INTO users (email, password, role, username) VALUES
('admin@example.com', '$2a$10$/vEmIJ/Oyrn4T9MbC/biROUs/o.IQmdFysoZiWCJM41HOGuFUH66u', 'ADMIN', 'admin'); -- admin --

-- Insert mock users with hashed passwords --
INSERT INTO users (email, password, role, username) VALUES
('user1@example.com', '$2a$10$A2lSi2qO7rUgR/.7jwe8OeFsp5A7MYRPOomPnguBRhmxP4UzHnfoK', 'USER', 'user1'),  -- user1 --
('user2@example.com', '$2a$10$KprrYvMd8DpHPmXhzU8MO.DZWtZwFBswvt9VoWAv4hYC0dgottzoK', 'USER', 'user2'),  -- user2 --
('user3@example.com', '$2a$10$fbgrVs3R4FzzGdpAb0JI9OooVhH3EwbyRPV1w7sjbyGY3lnSbi/K.', 'USER', 'user3'),  -- user3 --
('user4@example.com', '$2a$10$5tkpeyIt1Rp7cYZE.p8G..U3wFhsyrF8BE/FQgOiOfw.OuUtCsjrK', 'USER', 'user4');  -- user4 --


-- Insert mock campaigns --
INSERT INTO campaigns (description, name) VALUES
('A thrilling fantasy campaign', 'Dragon Quest'),
('A sci-fi adventure in space', 'Galactic Explorers'),
('A horror mystery in a small town', 'Shadows of Ravenwood');

-- Insert mock campaign users --
INSERT INTO campaign_users (campaign_id, user_id, campaign_role, screen_name) VALUES
(1, 1, 'PLAYER', 'Hero of the Realm'),
(1, 2, 'GM', 'Master of the Quest'),
(2, 3, 'PLAYER', 'Interstellar Wanderer'),
(2, 4, 'PLAYER', 'Cosmic Adventurer'),
(3, 1, 'PLAYER', 'Curious Investigator'),
(3, 2, 'GM', 'The Haunting Master');

-- Insert mock sessions --
INSERT INTO sessions (session_date, session_number, campaign_id) VALUES
('2024-10-01', 1, 1),
('2024-10-15', 2, 1),
('2024-11-01', 1, 2),
('2024-11-15', 2, 2),
('2024-12-01', 1, 3);

-- Insert mock notes --
INSERT INTO notes (created_at, session_id, user_id, text, user_role, user_screen_name) VALUES
(NOW(), 1, 1, 'The quest begins at the village.', 'PLAYER', 'Hero of the Realm'),
(NOW(), 1, 2, 'The players have arrived at the village.', 'GM', 'Master of the Quest'),
(NOW(), 2, 1, 'A dragon appears!', 'PLAYER', 'Hero of the Realm'),
(NOW(), 2, 3, 'We must prepare for battle.', 'PLAYER', 'Curious Investigator'),
(NOW(), 3, 3, 'The spaceship is ready for launch.', 'PLAYER', 'Interstellar Wanderer'),
(NOW(), 4, 4, 'We will explore the outer worlds.', 'PLAYER', 'Cosmic Adventurer'),
(NOW(), 5, 2, 'The mystery deepens as the night falls.', 'GM', 'The Haunting Master');

-- Insert mock invitations --
INSERT INTO invitations (campaign_id, created_at, expires_at, token, status_enum) VALUES
(1, NOW(), NOW() + INTERVAL '7 days', 'invitation-token-1', 'ACTIVE'),
(2, NOW(), NOW() + INTERVAL '7 days', 'invitation-token-2', 'ACTIVE'),
(3, NOW(), NOW() + INTERVAL '7 days', 'invitation-token-3', 'EXPIRED');


-- Insert notes for the Dragon Quest campaign --
INSERT INTO notes (created_at, session_id, user_id, text, user_role, user_screen_name) VALUES
(NOW(), 1, 1, 'After gathering supplies, we set off toward the dragon''s lair. The path was treacherous, filled with rocky terrain and dense underbrush. "Keep your eyes peeled for any signs of danger," cautioned the GM, as we ventured deeper into the wilderness. Just as we began to feel at ease, we heard the distant roar of the dragon.', 'PLAYER', 'Hero of the Realm'),
(NOW(), 1, 2, 'The party reached the base of the mountain, where the dragon resides. "This is it," the GM announced, building the tension. Each member of the party readied their weapons, hearts racing. The air grew colder as they approached the lair, shadows lurking behind every rock.', 'GM', 'Master of the Quest'),
(NOW(), 2, 1, 'The confrontation with the dragon was intense! Flames erupted as the beast roared in fury. "Aim for its underbelly!" shouted the Hero of the Realm, as the battle raged on. We dodged fireballs and countered with our spells and arrows, desperately trying to gain the upper hand.', 'PLAYER', 'Hero of the Realm'),
(NOW(), 2, 3, 'With strategy and teamwork, we managed to weaken the dragon. "This is our moment!" cried the Curious Investigator as they launched a final attack. The air crackled with energy as we landed the decisive blow, the dragon falling to the ground with a mighty crash.', 'PLAYER', 'Curious Investigator');

-- Insert notes for the Galactic Explorers campaign --
INSERT INTO notes (created_at, session_id, user_id, text, user_role, user_screen_name) VALUES
(NOW(), 3, 3, 'As the spaceship hurtled through the stars, the crew felt a mix of excitement and apprehension. "What lies beyond the next galaxy?" pondered the Interstellar Wanderer. They scanned the surrounding space, marveling at the beauty of distant worlds. Suddenly, an alert rang out; they had detected an unidentified object ahead.', 'PLAYER', 'Interstellar Wanderer'),
(NOW(), 3, 4, 'The Cosmic Adventurer activated the shields, ready for anything. "We should approach with caution," they advised. The crew prepared for an encounter, their hearts racing. As they got closer, the object revealed itself to be a derelict spacecraft, drifting silently in the void.', 'PLAYER', 'Cosmic Adventurer'),
(NOW(), 4, 2, 'The GM described the eerie silence of the abandoned ship. "It''s almost as if time has stopped here," they said, building suspense. The team cautiously boarded, flashlights illuminating the dark corridors. Strange sounds echoed, and they soon realized they were not alone.', 'GM', 'The Haunting Master');

-- Insert notes for the Shadows of Ravenwood campaign --
INSERT INTO notes (created_at, session_id, user_id, text, user_role, user_screen_name) VALUES
(NOW(), 5, 1, 'The investigation led us to the old cemetery at the edge of town. "There have been reports of strange lights at night," said the Curious Investigator. The GM painted a vivid picture of fog rolling in, creating an atmosphere thick with tension. As they approached, they felt a chilling presence lurking just out of sight.', 'PLAYER', 'Curious Investigator'),
(NOW(), 5, 2, 'The Haunting Master described the eerie sounds of whispers carried by the wind. "You must decide whether to press on or turn back," they warned, heightening the suspense. The players debated, adrenaline pumping, as they ventured deeper into the graveyard.', 'GM', 'The Haunting Master'),
(NOW(), 5, 1, 'In the heart of the cemetery, they found an ancient mausoleum. "Something is definitely off about this place," noted the Curious Investigator. As they examined the structure, they uncovered a hidden passage leading underground. A sense of dread washed over the party as they prepared to enter.', 'PLAYER', 'Curious Investigator'),
(NOW(), 5, 2, 'Descending into the darkness, they encountered a series of puzzles guarding a hidden treasure. "These traps seem too elaborate for a simple grave," said the GM, as they set the stage. The tension mounted as the players worked together to solve each challenge, knowing the stakes had never been higher.', 'GM', 'The Haunting Master');