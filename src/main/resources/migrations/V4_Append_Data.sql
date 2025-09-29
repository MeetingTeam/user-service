-- Insert data into user table
insert into `user` (`id`, `birthday`, `email`, `gender`, `is_online`, `last_active`, `nick_name`, `phone_number`, `url_icon`) 
    values ('94f8f498-3071-700b-036b-8b64fc52a716', '2004-06-28', 'tienhung17092004@gmail.com', b'0', b'0', '2025-08-24 12:04:53.610918', 'Hung Tran', '0846851172', NULL);
insert into `user` (`id`, `birthday`, `email`, `gender`, `is_online`, `last_active`, `nick_name`, `phone_number`, `url_icon`) 
    values ('a4e804c8-60a1-7021-21ee-393fdfbe2492', '2004-06-28', '22520527@gm.uit.edu.vn', b'0', b'0', '2025-09-21 10:23:05.103318', 'Quang Teo', '0946851172', 'https://meeting-team-storage.s3.amazonaws.com/uiticon_21578.png');
insert into `user` (`id`, `birthday`, `email`, `gender`, `is_online`, `last_active`, `nick_name`, `phone_number`, `url_icon`) 
    values ('44780438-7041-70ed-1f7a-47c6f180b343', '2025-09-25', 'taolao@gmail.com', b'1', b'0', '2025-09-25 02:48:37.910779', 'Tao Lao', NULL, NULL);
insert into `user` (`id`, `birthday`, `email`, `gender`, `is_online`, `last_active`, `nick_name`, `phone_number`, `url_icon`) 
    values ('940844a8-30e1-7091-1ca0-370a4e0d8d4c', '2025-09-25', 'kobiet@gmail.com', b'1', b'0', '2025-09-25 02:46:53.550178', 'An danh', NULL, NULL);


-- Insert data into friend_relation table
insert into `friend_relation` (`id`, `status`, `friend1id`, `friend2id`) 
    values ('10f0b214-af70-4270-8f67-3df54976b7c9', 'FRIEND', 'a4e804c8-60a1-7021-21ee-393fdfbe2492', '94f8f498-3071-700b-036b-8b64fc52a716');
insert into `friend_relation` (`id`, `status`, `friend1id`, `friend2id`) 
    values ('10f0b214-af70-4270-8f67-3df54976b7c8', 'FRIEND', 'a4e804c8-60a1-7021-21ee-393fdfbe2492', '940844a8-30e1-7091-1ca0-370a4e0d8d4c');
insert into `friend_relation` (`id`, `status`, `friend1id`, `friend2id`) 
    values ('10f0b214-af70-4270-8f67-3df54976b7c7', 'FRIEND', 'a4e804c8-60a1-7021-21ee-393fdfbe2492', '44780438-7041-70ed-1f7a-47c6f180b343');



