CREATE TABLE `USER` (
  `id` int(11) UNSIGNED NOT NULL,
  `name` varchar(50) NOT NULL,
  `username` varchar(25) NOT NULL,
  `password` varchar(25) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
);

--ALTER TABLE `USER`
--  ADD PRIMARY KEY (`id`),
--  ADD UNIQUE KEY `username` (`username`),
--  ADD UNIQUE KEY `email` (`email`);