-- Insert sample users with UUIDs
INSERT INTO users (uuid, name, email, phone, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'John Doe', 'john.doe@example.com', '555-0101', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440002', 'Jane Smith', 'jane.smith@example.com', '555-0102', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440003', 'Mike Johnson', 'mike.johnson@example.com', '555-0103', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440004', 'Sarah Williams', 'sarah.williams@example.com', '555-0104', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440005', 'David Brown', 'david.brown@example.com', '555-0105', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440006', 'Emily Davis', 'emily.davis@example.com', '555-0106', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440007', 'Robert Miller', 'robert.miller@example.com', '555-0107', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440008', 'Lisa Anderson', 'lisa.anderson@example.com', '555-0108', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440009', 'James Taylor', 'james.taylor@example.com', '555-0109', NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440010', 'Maria Garcia', 'maria.garcia@example.com', '555-0110', NOW(), NOW());

INSERT INTO chats (uuid, chat_from, chat_to, message, created_at) VALUES
('650e8400-e29b-41d4-a716-446655440001', 1, 2, 'Hey Jane! How are you doing today?', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
('650e8400-e29b-41d4-a716-446655440002', 2, 1, 'Hi John! I am doing great, thanks for asking!', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
('650e8400-e29b-41d4-a716-446655440003', 1, 2, 'That is awesome! Do you want to grab coffee later?', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('650e8400-e29b-41d4-a716-446655440004', 2, 1, 'Sure! How about 3 PM at the downtown cafe?', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('650e8400-e29b-41d4-a716-446655440005', 3, 4, 'Sarah, did you finish the project report?', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
-- Messages from Mike (3) to Sarah (4)
('650e8400-e29b-41d4-a716-446655440006', 4, 3, 'Yes Mike! I just sent it to your email.', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
('650e8400-e29b-41d4-a716-446655440007', 3, 4, 'Perfect! Thanks for the quick turnaround.', DATE_SUB(NOW(), INTERVAL 4 HOUR)),

-- David (5) to Emily (6)
('650e8400-e29b-41d4-a716-446655440008', 5, 6, 'Emily, are you coming to the team meeting tomorrow?', DATE_SUB(NOW(), INTERVAL 8 HOUR)),
('650e8400-e29b-41d4-a716-446655440009', 6, 5, 'Yes, I will be there at 10 AM sharp!', DATE_SUB(NOW(), INTERVAL 7 HOUR)),

-- Robert (7) and Lisa (8) conversation
('650e8400-e29b-41d4-a716-446655440010', 7, 8, 'Lisa, can you help me with the database design?', DATE_SUB(NOW(), INTERVAL 10 HOUR)),
('650e8400-e29b-41d4-a716-446655440011', 8, 7, 'Of course! Let me know when you are free.', DATE_SUB(NOW(), INTERVAL 9 HOUR)),
('650e8400-e29b-41d4-a716-446655440012', 7, 8, 'How about in 30 minutes?', DATE_SUB(NOW(), INTERVAL 8 HOUR)),
('650e8400-e29b-41d4-a716-446655440013', 8, 7, 'Sounds good! See you then.', DATE_SUB(NOW(), INTERVAL 7 HOUR)),

-- James (9) to Maria (10)
('650e8400-e29b-41d4-a716-446655440014', 9, 10, 'Maria, happy birthday! Hope you have an amazing day!', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
('650e8400-e29b-41d4-a716-446655440015', 10, 9, 'Thank you so much James! That is very kind of you!', DATE_SUB(NOW(), INTERVAL 11 HOUR)),

-- Group discussions
('650e8400-e29b-41d4-a716-446655440016', 1, 3, 'Mike, did you see the game last night?', DATE_SUB(NOW(), INTERVAL 15 HOUR)),
('650e8400-e29b-41d4-a716-446655440017', 3, 1, 'Yes! It was incredible!', DATE_SUB(NOW(), INTERVAL 14 HOUR)),
('650e8400-e29b-41d4-a716-446655440018', 2, 4, 'Sarah, let me know if you need any help with the presentation.', DATE_SUB(NOW(), INTERVAL 13 HOUR)),
('650e8400-e29b-41d4-a716-446655440019', 4, 2, 'Thanks Jane! I might need your feedback later.', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
('650e8400-e29b-41d4-a716-446655440020', 5, 7, 'Robert, the code review is scheduled for Friday.', DATE_SUB(NOW(), INTERVAL 10 HOUR)),

-- Recent messages
('650e8400-e29b-41d4-a716-446655440021', 6, 8, 'Lisa, can you send me the API documentation?', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('650e8400-e29b-41d4-a716-446655440022', 8, 6, 'Sure Emily, I will email it right away.', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
('650e8400-e29b-41d4-a716-446655440023', 9, 1, 'John, are we still on for lunch tomorrow?', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('650e8400-e29b-41d4-a716-446655440024', 1, 9, 'Absolutely! See you at noon.', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('650e8400-e29b-41d4-a716-446655440025', 10, 2, 'Jane, I loved your presentation today!', DATE_SUB(NOW(), INTERVAL 1 HOUR));
